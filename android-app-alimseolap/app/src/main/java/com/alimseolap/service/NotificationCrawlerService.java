package com.alimseolap.service;

import android.app.Notification;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.alimseolap.presenter.DBManager;
import org.json.JSONException;



public class NotificationCrawlerService extends android.service.notification.NotificationListenerService {
    public final static String TAG = "=====";
    DBManager database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new DBManager(openOrCreateDatabase("data.db", MODE_PRIVATE, null));  // 데이터베이스 생성, 열기
        Log.e(TAG, "onCreate():: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy():: ");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.e(TAG, "onListenerConnected():: ");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        // 새로운 알림이 생성되었을 때
        super.onNotificationPosted(sbn);
        Log.e(TAG, "onNotificationPosted()");

        Notification notification = sbn.getNotification();
        Bundle extras = sbn.getNotification().extras;

        String contentTitle = extras.getString(Notification.EXTRA_TITLE);
        CharSequence contentText = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        CharSequence bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        CharSequence bigContentTitle = extras.getCharSequence(Notification.EXTRA_TITLE_BIG);

        Icon smallIcon = notification.getSmallIcon();
//        Icon largeIcon = notification.getLargeIcon();

        Log.e(TAG, "NotiPosted::  " +
                " / id : " + sbn.getId() +
                " / bigContentTitle : " + bigContentTitle +
                " / bigText : " + bigText +
                " / contentText : " + contentText +
                " / subText : " + subText +
                " / contentTitle : " + contentTitle +
//                " / icon : " + smallIcon.getResId() +
                " / packageName : " + sbn.getPackageName()
        );

        AsyncManager dbAsyncTask = new AsyncManager(bigText.toString(), sbn.getPackageName());
        dbAsyncTask.execute();

    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

        // 알림이 지워졌을 때
        super.onNotificationRemoved(sbn);
        Log.e(TAG, "onNotificationRemoved():: ");

        Log.e(TAG, "NotiIRemoved:: " +
                " packageName: " + sbn.getPackageName() +
                " id: " + sbn.getId());

    }



    // 비동기 처리
    public class AsyncManager extends AsyncTask<Void, Void, Void > {

        String notiMsg;
        String notiTitle;
        String taskName;

        // Constructor
        public AsyncManager(String notiMsg, String notiTitle){
            this.notiMsg = notiMsg;
            this.notiTitle = notiTitle;
            taskName = "DB Async Task";
    }

        @Override
        protected Void doInBackground(Void... voids) {
            // 비동기 처리 후 결과값을 리턴
            // 이 메소드가 끝난 후에 onPostExecute()가 실행됨

            // 여기를 함수형으로 짜면 좋을텐데.. 쩝..
            Log.e("==="+taskName+"===", "Start");
            try {
                database.updateNewNoti(notiMsg, notiTitle);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            // 서버 통신 후 처리 완료
            // 받아온 값은 result에 저장되며 JSON 타입으로 저장됨
            // 올바르지 못한 값을 받아왔을 때, 빈 String 값이 나옴

            Log.e("==="+taskName+"===", "Done");

        }
    }
}