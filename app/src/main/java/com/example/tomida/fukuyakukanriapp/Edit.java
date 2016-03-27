package com.example.tomida.fukuyakukanriapp;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Edit extends FragmentActivity implements View.OnClickListener,OnDateSetListener {
    TextView text1, text2,textView2,textView4,textView5,textView6;
    Button bt6, bt7, bt8,bt10;
    Intent it;
    static StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView2=(TextView)findViewById(R.id.textView2);
        textView4=(TextView)findViewById(R.id.textView4);
        textView6=(TextView)findViewById(R.id.textView6);

        bt6 = (Button) findViewById(R.id.bt6);//一回分の取り消しボタン
        bt8 = (Button) findViewById(R.id.bt8);//リセットボタン
        bt10=(Button)findViewById(R.id.bt10);
        bt6.setOnClickListener(this);
        bt8.setOnClickListener(this);
        bt10.setOnClickListener(this);

        Calendar cl = Calendar.getInstance();//日付取得
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN);//日本の日付にフォーマット
        String time = df.format(cl.getTime());//取得したデータをtimeのstring型に変換

        text1.setText(time);//日付をtext1に表示

        //○回服用しました
        text2.setText(MainActivity.count + "回服用しました");//メインのOKカウントアップ表示

        File path=new File("data/data/"+getPackageName()+"/files/date.txt");
        if(path.exists()){
            try{
                FileInputStream fis=openFileInput("date.txt");
                BufferedReader br=new BufferedReader(new InputStreamReader(fis));
                sb=new StringBuffer();
                String str;
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                textView5.setText(sb);
            }catch (Exception e){
                Toast.makeText(this,"ファイル読み込みできませんでした。",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == bt6) {
            if (MainActivity.count > 0) {
                MainActivity.count -= 1;//１回分取り消しカウント
                text2.setText(MainActivity.count + "回服用しました");
                MainActivity.bt1.setText("OK!");
            } else {
                text2.setText("マイナスです。");//取り消し０以下
            }
        }
        if (v == bt7) {
            //カレンダーの日付にマークかつアラームセット。Setting.javaの薬をもらった量の関連をつける。
        }
        if (v == bt8) {
            MainActivity.count = 0;//リセットボタンの処理
            text2.setText(MainActivity.count + "回服用しました");//リセットの処理を表示
            MainActivity.bt1.setText("OK!");
        }if(v==bt10){
            it=new Intent(this,CalendarView.class);
            startActivity(it);
        }
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {//DatePickerでダイアログによる日付取得
        String str=String.valueOf(year) + "年" + String.valueOf(monthOfYear + 1) + "月" + String.valueOf(dayOfMonth) + "日が通院日です";
        textView5.setText(str);
        try{
            FileOutputStream fos=openFileOutput("date.txt", Context.MODE_PRIVATE);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(str);
            bw.flush();
            fos.close();
        }catch (Exception e) {
            Toast.makeText(this, "ファイル保存できませんでした。", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDatePickerDialog(View v){//日付表示
    DialogFragment newFragment =new Calender();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}