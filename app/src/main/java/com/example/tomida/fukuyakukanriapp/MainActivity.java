package com.example.tomida.fukuyakukanriapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//カレンダーアクティビティを作成し『カレンダー』ボタンでカレンダーに飛ぶ。カレンダーに服用上限を超えるとその日付にマーキングする。
//staticフィールド変数でクラス間での変数共有が便利。
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt2,bt10,bt11,bt12;static Button bt1;//ボタンのテキスト変更のためstatic変数に変更
    Intent it;
    static int count=0;//OKを押した回数の入れ物
    TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt10 = (Button) findViewById(R.id.bt10);
        bt11 = (Button) findViewById(R.id.bt11);
        bt12 = (Button) findViewById(R.id.bt12);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2=(TextView)findViewById(R.id.textView7);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt10.setOnClickListener(this);
        bt11.setOnClickListener(this);
        bt12.setOnClickListener(this);

        tv1.setText(Edit.sb);
        tv2.setText(count+"/"+Setting.max_result+"回服用しました");

    }

    @Override
    public void onClick(View v) {
        if(v==bt1){
            it=new Intent(this,Edit.class);//カウントし編集へ飛ぶ
       if(count<Setting.max_result){//セットした上限までカウントアップ
           count+=1;//1日の服用回数カウントアップ
           if(count>Setting.max_result-1) bt1.setText("登録！");
       }else if(count>=Setting.max_result){
           Toast.makeText(getBaseContext(),"今日は全て飲みました。\n登録します。",Toast.LENGTH_SHORT).show();
           it=new Intent(this,DB.class);
           //上限を超えるとDBに登録
       }else if(count>Setting.max_result){
           Toast.makeText(getBaseContext(),"飲み過ぎです！気をつけてください！",Toast.LENGTH_SHORT).show();
           //カウントが超えたら警告トースト表示
       }startActivity(it);
        }else if(v==bt2){
            it=new Intent(this,Setting.class);//設定へ飛ぶ
            startActivity(it);
        }else if(v==bt10){
            it=new Intent(this,DB.class);//DBリスト表示
            startActivity(it);
        }else if(v==bt11){
            it=new Intent(this,Edit.class);//編集へ
            startActivity(it);
        }else if(v==bt12){
            count=0;
            Toast.makeText(this,"服用回数をリセットしました。",Toast.LENGTH_SHORT).show();
            bt1.setText("OK!");
        }
    }
}