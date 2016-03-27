package com.example.tomida.fukuyakukanriapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ServiceActivity extends BroadcastReceiver {//時間が経つとこのクラスがレシーブされる
    public void onReceive(Context context,Intent intent){
        Toast.makeText(context,"時間がきました!",Toast.LENGTH_LONG).show();
        Log.d("AlarmReceiver","Alarm Received!"+intent.getIntExtra(Intent.EXTRA_ALARM_COUNT,0));
    }

}
