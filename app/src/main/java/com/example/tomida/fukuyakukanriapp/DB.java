package com.example.tomida.fukuyakukanriapp;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DB extends AppCompatActivity implements AdapterView.OnItemLongClickListener{
    ListView lv;
    SQLiteDatabase db;
    ArrayList<DB> arrayList;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        Calendar cl = Calendar.getInstance();//日付取得
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN);//日本の日付にフォーマット
        String time = df.format(cl.getTime());//取得したデータをtimeのstring型に変換
        //SQL文作成
        String qry3="SELECT*FROM log";


        String str="data/data/"+getPackageName()+"/files/log.db";//データベースを作成
        db = SQLiteDatabase.openOrCreateDatabase(str, null);//データベースをひらく
        String qry1="CREATE TABLE IF NOT EXISTS log"+"(id INTEGER PRIMARY KEY,time STRING unique,ok STRING)";//テーブル作成
        String[]qry2={"INSERT INTO log(time,ok) VALUES('"+time+"','OK')"};//1日一つだけテーブルに追加作成
        db.execSQL(qry1);
        Cursor cr = db.rawQuery(qry3, null);

        for (int i = 0; i < qry2.length; i++) {
            db.execSQL(qry2[i]);//日付が変更したときに１つのみ記録できる。
            }
        {
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            arrayList = new ArrayList<DB>();
            while (cr.moveToNext()) {
                int i=cr.getColumnIndex("id");
                int d=cr.getColumnIndex("time");
                int b=cr.getColumnIndex("ok");
                int id=cr.getInt(i);
                time=cr.getString(d);
                String ok=cr.getString(b);
                String row=time+" "+ok;
                ad.add(row);

                DB dbdata = new DB();
                dbdata.id = id;
                arrayList.add(dbdata);
                Log.d("tag", " id = " + id);
            }
            lv = (ListView) findViewById(R.id.listView);//最新の行しか表示されない（バグ）
            lv.setOnItemLongClickListener(this);
            lv.setAdapter(ad);
        }

    }
    public boolean onItemLongClick(final AdapterView<?>parent, final View view,int position,final long id){
        final DB dbdata = arrayList.get(position);
        Builder builder=new Builder(DB.this);
        builder.setTitle("削除");
        builder.setMessage("削除してもいいですか？");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete("log", " id = " + dbdata.id, null);
                Log.d("tag", " idd= " + dbdata.id);
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        return false;
   }
    public void onDestroy(){
        super.onDestroy();
        if(db!=null)db.close();
    }
}