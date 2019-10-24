package com.alimseolap.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alimseolap.R;
import com.alimseolap.model.NounDTO;
import com.alimseolap.presenter.DBManager;
import com.alimseolap.service.NotificationCrawlerService;
import com.alimseolap.utils.ApiCommUtil;
import com.alimseolap.view.Dialog.MyDialogFragment_All;
import com.alimseolap.view.Dialog.MyDialogFragment_Negative;
import com.alimseolap.view.Dialog.MyDialogFragment_Positive;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private DBManager database;

    // UI
    MyDialogFragment_All all = new MyDialogFragment_All(database);
    MyDialogFragment_Positive positive = new MyDialogFragment_Positive(database);
    MyDialogFragment_Negative negative = new MyDialogFragment_Negative(database);

    ImageView WordCloudImg;
    ProgressBar progressBar;
    String TAG = "오류";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // API-Communication
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//
        // Notification Service

        // <특별한 접근 -> 알림 접근 혀용> 확인

        if (!isPermissionGranted()) {
            // 접근 혀용이 되어있지 않다면 1. 메시지 발생 / 2, 설정으로 이동시킴
            Toast.makeText(getApplicationContext(), getString(R.string.app_name) + " 앱의 알림 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        // 서비스 시작
        // NotificationListenerService의 경우, Rebind() 메소드 실행 전까지 서비스가 종료되지 않는 것으로 확인됨.
        Intent service = new Intent(getApplicationContext(), NotificationCrawlerService.class);
        startService(service);
        //___________________________________________//

        // DB Connect
        database = new DBManager(openOrCreateDatabase("data.db", MODE_PRIVATE, null));  // 데이터베이스 생성, 열기


        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////


        Log.d(TAG, "2");
        WordCloudImg = (ImageView) findViewById(R.id.WordCloudImg);
        progressBar = (ProgressBar) findViewById(R.id.loading_wordcloudimg);


        Log.d(TAG, "3");
        String url = "http://noti-drawer.run.goorm.io/api/get-wordcloud";
        String jsonData = "{";

        ArrayList<NounDTO> list = database.getNounList();
        for (int i = 0; i < list.size(); i++) {
            jsonData += "\""+list.get(i).noun +"\" : "+(list.get(i).weight == 0 ? 1 : list.get(i).weight)+",";
        }
        jsonData = jsonData.substring(0,jsonData.length()-1) + "}";
        Log.e("++++", jsonData);

        Log.d(TAG, "4");

//        이미지 로드
        ApiCommAsync getImg = new ApiCommAsync(url, jsonData);
        Log.d(TAG, "5");
        getImg.execute();

    }


    // 모든 알림 보여주는 버튼
    public void btn1Method(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.container, all);
        MyDialogFragment_All myDialogFragmentAll = new MyDialogFragment_All(database);
        myDialogFragmentAll.show(manager, "test");
    }

    // 선호 알림 보여주는 버튼
    public void btn2Method(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.container, positive);
        MyDialogFragment_Positive myDialogFragment_positive = new MyDialogFragment_Positive(database);
        myDialogFragment_positive.show(manager, "test");
    }

    // 비선호 알림 보여주는 버튼
    public void btn3Method(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.replace(R.id.container, negative);
        MyDialogFragment_Negative myDialogFragment_negative = new MyDialogFragment_Negative(database);
        myDialogFragment_negative.show(manager, "test");
    }


    public class ApiCommAsync extends AsyncTask<Void, Void, Bitmap> {

        String url;
        String jsonData;

        // Constructor
        public ApiCommAsync(String url, String jsonData) {
            this.jsonData = jsonData;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // 비동기 처리 후 결과값을 리턴
            // 이 메소드가 끝난 후에 onPostExecute()가 실행됨
            return new ApiCommUtil().requestImg(url, jsonData);
            // :TODO:=============================== 이미지 가져오는 부분
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            // 서버 통신 후 처리 완료
            // 받아온 값은 result에 저장되며 JSON 타입으로 저장됨
            // 올바르지 못한 값을 받아왔을 때, 빈 String 값이 나옴

            if (result == null) {
            } else {
                WordCloudImg.setImageBitmap(result);
                progressBar.setVisibility(View.GONE);
            }

        }
    }


    // Notification Service
    private boolean isPermissionGranted() {
        // 노티수신을 확인하는 권한을 가진 앱 모든 리스트
        Set<String> sets = NotificationManagerCompat.getEnabledListenerPackages(this);
        // 이 앱의 알림 접근 허용이 되어있는가?
        return sets != null && sets.contains(getPackageName());
    }
}
