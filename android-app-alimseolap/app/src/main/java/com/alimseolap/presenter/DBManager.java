package com.alimseolap.presenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.alimseolap.model.NotificationDTO;
import com.alimseolap.model.NounDTO;
import com.alimseolap.utils.ApiCommUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class DBManager {

    private SQLiteDatabase db;

    public DBManager(SQLiteDatabase getDP) {

        db = getDP;

        if(db == null){
            Log.e("=====", "DB was not opened");
            return;
        }

        try{
            db.execSQL("create table noun( id integer primary key autoincrement, noun text not null unique, weight integer not null default 0, views integer not null default 0);");
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        try{
            db.execSQL("create table notification( id integer primary key autoincrement, title text, msg text not null, nlink text, isread integer default 0, pkgname text );");
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        Log.e("=====", "DB created");

    }



    public ArrayList<NounDTO> getNounList() {

        if(db == null){
            Log.e("=====", "데이터베이스가 생성되지 않았습니다. <createDatabase()>");
            return null;
        }

        ArrayList<NounDTO> result = new ArrayList<>();

        Cursor cursor = db.rawQuery("select id, noun, weight, views from noun ", null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            result.add(new NounDTO(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }



    public ArrayList<NotificationDTO> getNotificationList() {
        return getNotificationList("");
    }



    public ArrayList<NotificationDTO> getNotificationList(String query) {

        if(db == null){
            Log.e("=====", "데이터베이스가 생성되지 않았습니다. <createDatabase()>");
            return null;
        }

        ArrayList<NotificationDTO> result = new ArrayList<>();

        Cursor cursor = db.rawQuery("select id, title, msg, nlink, isread, pkgname from notification "+query, null);
        cursor.moveToFirst();


        for(int i=0; i<cursor.getCount(); i++){

            int tmpWeight = 0;

            // Safe coding
            if ( cursor.getString(3) != null ) { // nLink가 null이 아닌 경우만 weight 구함

                String[] nLinks = cursor.getString(3).split(",");
                for(int j=0; j<nLinks.length; j++) {
                    int nounId = Integer.parseInt(nLinks[j]);
                    Cursor c = db.rawQuery("select weight from noun where id="+nounId,null);
                    c.moveToFirst();
                    tmpWeight += c.getInt(0);
                    c.close();
                }

            } else {
                tmpWeight = 0;
            }

            result.add(new NotificationDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(5), cursor.getString(2), tmpWeight, cursor.getInt(4) == 1 ? true : false));
            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }



    public void insertNoun(String nounValue) {
        db.execSQL("insert into noun(noun) values (\""+ nounValue +"\")");
    }



    public void updateNotiWeight(int notiId, boolean isPositive) {

        Cursor cursor = db.rawQuery("select nlink from notification where id="+notiId, null);
        cursor.moveToFirst();

        String[] nLinks = cursor.getString(0).split(",");

        String operator = isPositive ? "+" : "-";

        for(int i=0; i<nLinks.length; i++)
            db.execSQL("update noun set weight = weight" + operator + "100 where id=" + nLinks[i]);
    }



    public void updateNounWeight(int nounId, boolean isPositive) {
        String operator = isPositive ? "+" : "-";
        db.execSQL("update noun set weight = weight" + operator + "100 where id=" + nounId);
    }



    public void updateNounWeight(String noun, boolean isPositive) {
        String operator = isPositive ? "+" : "-";
        db.execSQL("update noun set weight = weight" + operator + "100 where noun=\"" + noun + "\"");
    }



    public void updateNewNoti(String msg, String title) throws JSONException {

        Cursor cursor;
        int latestNotification = 0;
        ArrayList<String> similarSample = new ArrayList<>();


        // 받은 노티를 디비에 저장
        db.execSQL("insert into notification(msg, title) values (\""+ msg +"\",\"" + title + "\")");
        cursor = db.rawQuery("select id from notification", null);
        latestNotification = cursor.getCount();



        // msg를 JSON 타입으로 변환
        String tmpJson = "{\"sentence\" : \"" + msg + "\"}";

        // 백엔드 API에서 명사 정보를 받아옴
        tmpJson = new ApiCommUtil().requestJson("http://noti-drawer.run.goorm.io/api/analyze-sentence", tmpJson);

        JSONObject analyzeAPIResult = new JSONObject(tmpJson);      // 명사 분석 결과값 테스트셋 :: analyzeAPIResult = api.get("/api/analyze")
        JSONArray compare = analyzeAPIResult.getJSONArray("result");

        for(int i = 0; i < compare.length(); i++){
            // compare에 있는 명사가 데이터베이스에 있는지 확인
            cursor = db.rawQuery("select id from noun where noun = \"" + compare.getString(i) + "\"", null);
            cursor.moveToFirst();

            Log.e("===44===", ""+cursor.getCount()  );

            // 명사가 존재하는 경우
            if(cursor.getCount() != 0){
                Log.e("==Add nLink==", " ");

                int targetNounId = cursor.getInt(0);

                // 명사id 가져옴 -> 가장 최신으로 받은 노티 커서를 가져옴 -> nlink 값 가져오기 -> 명사의 id값 더해서 업데이트
                cursor = db.rawQuery("select nlink from notification where id="+latestNotification, null);
                cursor.moveToFirst();

                String appendNLink = cursor.getString(0);

                if(appendNLink == null)
                    appendNLink = targetNounId + "";
                else
                    appendNLink = appendNLink + "," + targetNounId;

                db.execSQL("update notification set nlink = \"" + appendNLink + "\" where id=" + latestNotification);

            }
            else { // 명사가 존재하지 않는 경우
                Log.e("==Add similarSample==",  " ");
                similarSample.add(compare.getString(i));

                // Noun DB에 추가하고 id값을 가져옴
                db.execSQL("insert into noun(noun) values (\""+ compare.getString(i) +"\")");
                cursor = db.rawQuery("select id from noun", null);

                int targetNounId = cursor.getCount();

                // 마지막으로 추가된 Notification DB의 데이터에 nLink 업데이트
                cursor = db.rawQuery("select nlink from notification where id="+latestNotification, null);
                cursor.moveToFirst();

                String appendNLink = cursor.getString(0);

                if(appendNLink == null)
                    appendNLink = targetNounId + "";
                else
                    appendNLink = appendNLink + "," + targetNounId;

                db.execSQL("update notification set nlink = \"" + appendNLink + "\" where id=" + latestNotification);
            }
        }

        // API에 보낼 JSON data 생성
        ArrayList<String> nounDBTotal = new ArrayList<>();
        ArrayList<Integer> nounDBTotalWeight = new ArrayList<>();

        String makeJSONTmp = "{\"request_noun\" : [";
        for(int i = 0; i < similarSample.size(); i++){
            makeJSONTmp += "\"" + similarSample.get(i) + "\",";
            if(i+1 == similarSample.size())
                makeJSONTmp = makeJSONTmp.substring(0,makeJSONTmp.length()-1);
        }
        makeJSONTmp += "],\"total_nouns\" : [";

        cursor = db.rawQuery("select noun, weight from noun", null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            nounDBTotal.add(cursor.getString(0));
            nounDBTotalWeight.add(cursor.getInt(1));

            makeJSONTmp += "\"" + cursor.getString(0) + "\",";
            if(i+1 == cursor.getCount())
                makeJSONTmp = makeJSONTmp.substring(0,makeJSONTmp.length()-1);
            cursor.moveToNext();
        }
        makeJSONTmp += "]}";

        Log.e("====22====", makeJSONTmp);


        // API로 유사도를 받아옴
        // apiSimilarSend를 보냄
        // similarityAPIResult를 받음
        // request_noun -> similarSample
        // total_nouns -> nounDBTotal, nounDBTotalWeight

        tmpJson = new ApiCommUtil().requestJson("http://noti-drawer.run.goorm.io/api/similarity-analysis", makeJSONTmp);
        Log.e("====22====", tmpJson);

        JSONObject similarityAPIResult = new JSONObject(tmpJson);   // 유사도 결과값  테스트셋 :: similarityAPIResult = api.get("/api/similarity")


        double totalWeight = 0.0;

        for(int i = 0; i < similarSample.size(); i++){
            JSONObject simJson = similarityAPIResult.getJSONObject(similarSample.get(i));
            for(int j=0; j < simJson.length(); j++){
                double value = simJson.getDouble(nounDBTotal.get(j));
                int weight = nounDBTotalWeight.get(i);

                totalWeight = totalWeight + (value*weight);
            }

            db.execSQL("update noun set weight = " + (int)totalWeight + " where noun=\"" + similarSample.get(i) + "\"");
            totalWeight = 0.0;
        }
        cursor.close();

        Log.e("=====", "Done to add");

    }

}



