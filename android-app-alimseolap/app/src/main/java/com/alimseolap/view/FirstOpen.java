package com.alimseolap.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alimseolap.R;
import com.alimseolap.presenter.ConfigHelper;
import com.alimseolap.presenter.DBManager;

public class FirstOpen extends AppCompatActivity {

    public String keyword_hate;
    public String keyword_like;

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_open);



        Button button = (Button) findViewById(R.id.FirstBtn); /*메인 액팁비티로 넘어가는 버튼*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText1 = (EditText) findViewById(R.id.editText_like);
                keyword_like = editText1.getText().toString();
                EditText editText2 = (EditText) findViewById(R.id.editText_hate);
                keyword_hate = editText2.getText().toString();

                if(keyword_hate.isEmpty() || keyword_like.isEmpty()){
                    Toast.makeText(getApplicationContext(), "텍스트를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, sub);//액티비티 띄우기
                    System.out.println(keyword_like);
                    //  db로 보낼 like, hate 키워드 스티링에 저장
                    ConfigHelper.setConfigValue(getBaseContext(), "register", "done");

                    // :TODO: ======================== NOUN 데이터베이스 업데이트 / keyword_like / keyword_hate
                    // 싱글톤으로 DBManager를 만들면 되지만...
                    DBManager database = new DBManager(openOrCreateDatabase("data.db", MODE_PRIVATE, null));
                    database.insertNoun(keyword_like);
                    database.updateNounWeight(keyword_like, true);
                    database.insertNoun(keyword_hate);
                    database.updateNounWeight(keyword_hate, false);
                }
            }
        });


    }


}
