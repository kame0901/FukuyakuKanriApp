package com.example.tomida.fukuyakukanriapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class Setting extends AppCompatActivity implements View.OnClickListener{
    Button bt4,bt9;
    static int max_result=0;
    CheckBox cb1,cb2,cb3,cb4;
    int setTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bt4=(Button)findViewById(R.id.bt4);//1日分の量を設定するボタン
        bt9=(Button)findViewById(R.id.bt9);//オールリセットボタン
        cb1=(CheckBox)findViewById(R.id.checkBox);//朝
        cb2=(CheckBox)findViewById(R.id.checkBox2);//昼
        cb3=(CheckBox)findViewById(R.id.checkBox3);//夜
        cb4=(CheckBox)findViewById(R.id.checkBox4);//寝る前

        bt4.setOnClickListener(this);
        bt9.setOnClickListener(this);
        cb1.setOnCheckedChangeListener(new SampleCheckedChangeListener());
        cb2.setOnCheckedChangeListener(new SampleCheckedChangeListener());
        cb3.setOnCheckedChangeListener(new SampleCheckedChangeListener());
        cb4.setOnCheckedChangeListener(new SampleCheckedChangeListener());
        cb1.setOnClickListener(this);
        cb2.setOnClickListener(this);
        cb3.setOnClickListener(this);
        cb4.setOnClickListener(this);

        File path=new File("data/data/"+getPackageName()+"/files/count.txt");//ファイルパス指定
        if(path.exists()){
            try{
                FileInputStream fis=openFileInput("count.txt");//ファイル読み込み
                BufferedReader br=new BufferedReader(new InputStreamReader(fis));
                StringBuffer sb=new StringBuffer();
                String str;
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
            }catch (Exception e){
                Toast.makeText(this,"ファイル読み込みできませんでした。",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v==bt9){//リセットボタン
            try{
                FileOutputStream fos=openFileOutput("count.txt", Context.MODE_PRIVATE);//ファイルに書き込み
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos));
                bw.write(max_result);//リセットした回数をファイルにセット
                bw.flush();
                fos.close();
            }catch (Exception e){
                Toast.makeText(this,"ファイル保存できませんでした。",Toast.LENGTH_SHORT).show();
            }
            max_result = 0;
            Toast.makeText(getBaseContext(), "リセットしました。", Toast.LENGTH_SHORT).show();
        }
        //チェックボックスに変更
        if (v == bt4) {
                Toast.makeText(getBaseContext(), "1日の上限をセットしました。", Toast.LENGTH_SHORT).show();
                try{
                    FileOutputStream fos=openFileOutput("count.txt", Context.MODE_PRIVATE);
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos));
                    bw.write(max_result);//上限回数をファイルにセット
                    bw.flush();
                    fos.close();
                }catch (Exception e){
                    Toast.makeText(this,"ファイル保存できませんでした。",Toast.LENGTH_SHORT).show();
                }//ファイルに読み込んだ数値をリセットするボタン追加
            }
        //アラームセットメソッド
        //アラームのメソッドインスタンス
        Intent intent=new Intent(Setting.this,ServiceActivity.class);
        PendingIntent sender=PendingIntent.getBroadcast(Setting.this, 0, intent, 0);
        Calendar calendar=Calendar.getInstance();//カレンダーインスタンス
        calendar.setTimeInMillis(System.currentTimeMillis());//毎秒カウント
        //基準時間を23時に設定したい。
        //calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 0, 0);
        //calendar.add(Calendar.SECOND, (int) (setTime - calendar.getTimeInMillis()));//0時からの経過時間をSetting.classで指定する。
        calendar.add(Calendar.SECOND,setTime);
        //0=setTime-現時刻==>0時からのsetTime後の時間
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
        Toast.makeText(Setting.this,"Start Alarm!",Toast.LENGTH_LONG).show();
        //ここまでアラーム （0時時点にアラームセットする想定）
        if(v==cb1){
            if(MainActivity.count>0)setTime=0;
            if(MainActivity.count==0)setTime=24*60*60/4;//６時間後にアラーム//MainActivityのOK!ボタンで解除
        }
        if(v==cb2){
            if(MainActivity.count>0)setTime=0;
            if(MainActivity.count==0)setTime=24*60*60*2/4;//１２時間後にアラームセット
        }
        if(v==cb3){
            if(MainActivity.count>0)setTime=0;
            if(MainActivity.count==0)setTime=24*60*60*3/4;//１８時間後ににアラームセット
        }
        if(v==cb4){
            if(MainActivity.count>0)setTime=0;
            if(MainActivity.count==0)setTime=24*60*60;//２４時間後にアラームセット
            }
        }

    public class SampleCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton cb,boolean isChecked) {
            if (isChecked == true) {
                //戻るボタンで戻った後セットするとプラスされてしまう（バグ）
                //1日４回までセット可能
                if(max_result<4) max_result+=1;
            } else if (isChecked == false) {
                max_result-=1;
            }
        }
    }
}