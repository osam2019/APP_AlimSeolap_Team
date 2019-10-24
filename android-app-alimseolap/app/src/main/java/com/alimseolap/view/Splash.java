package com.alimseolap.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import com.alimseolap.R;
import com.alimseolap.presenter.ConfigHelper;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // API-Communication
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초

    }

    private class splashhandler implements Runnable{

        public void run(){
            if (ConfigHelper.getConfigValue(getBaseContext(), "register") == null) {
                startActivity(new Intent(getApplication(), FirstOpen.class)); //로딩이 끝난 후, ChoiceFunction 이동
                Splash.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
            else{
                startActivity(new Intent(getApplication(), MainActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                Splash.this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}


