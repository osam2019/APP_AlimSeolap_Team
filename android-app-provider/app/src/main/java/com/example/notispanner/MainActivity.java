package com.example.notispanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;



public class MainActivity extends AppCompatActivity {

    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private Button create;
    private Button remove;
    // id를 바꾸어서 알림 생성 버튼 클릭 시 계속해서 알림이 생성됨.
    int idManager = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = findViewById(R.id.create);
        remove = findViewById(R.id.remove);

        create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view){
                createNotification();
            }
        });
//        // 알림 삭제 기능
//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeNotification();
//            }
//        });
    }

    private void createNotification() {

        // Notification 빌더 생성 & 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        // 이미지 로드 시 에러 발생

        String notiMsg = getJsonArrayDataRandomly(getJsonString());

//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));   // Bitmap 이미지를 넣음

//        // 참고 템플릿
//        builder.setContentTitle("NotiSpanner");
//        builder.setSubText("테스트 알림");     // 부제목
//        builder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("알림이 생성됨").bigText("스포너에서 알림을 생성했습니다. 확인하시려면 여기를 누르세요."));
//        builder.setContentText("스포너에서 알림을 생성했습니다."); // 작은 메시지

        builder.setContentTitle("");  // 주석 처리하면 접고펼수 있는 알림이 됨
        builder.setSubText("정보");     // 부제목
        builder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("알림이 생성됨").bigText(notiMsg).setSummaryText(""));
        builder.setContentText( notiMsg.length() > 20 ? notiMsg.substring(0,20)+".." : notiMsg ); // 작은 메시지

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT); // 우선순위 지정
        builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class),  PendingIntent.FLAG_UPDATE_CURRENT));  // 알림 클릭 시, 해당 액티비티를 실행
        builder.setAutoCancel(true);     // 사용자가 탭을 클릭하면 자동 제거
        builder.setColor(Color.GREEN);   // 알림 아이콘의 배경색을 지정

        // 알림 생성
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // Oreo == 26
            // 오레오 이상 버전일 때
            builder.setSmallIcon(R.mipmap.ic_launcher);

            CharSequence channelName  = "테스트 알림";
            String description = "테스트 알림 서비스입니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH;  // 중요도를 높여 상단 알림창이 뜨도록 함.

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 노티매니저에 생성
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        } else {
            // 오레오 미만 버전일 때
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        }
        assert notificationManager != null;
        notificationManager.notify(idManager++, builder.build());  // 노티 발생!
    }

//    private void removeNotification(){
//        NotificationManagerCompat.from(this).cancel(1234); // 알림 임의 삭제 시, 생성한 아이디로 cancel() 메소드를 사용
//    }


    private String getJsonString(){
        String json = "";

        try{
            InputStream is = getAssets().open("samples.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            return json;

        }catch(IOException e){
            e.printStackTrace();
            return "";
        }
    }


    private String getJsonArrayDataRandomly(String jsonData){

        try {
            JSONArray jArray = new JSONObject(jsonData).getJSONArray("data");
            return jArray.getString(new Random().nextInt(jArray.length()-2)-1);

        } catch(JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

}
