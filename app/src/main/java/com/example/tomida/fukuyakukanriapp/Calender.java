package com.example.tomida.fukuyakukanriapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import java.util.Calendar;

public class Calender extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    //ダイアログメソッドの定義
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar cl=Calendar.getInstance();
        int year=cl.get(Calendar.YEAR);
        int month=cl.get(Calendar.MONTH);
        int day=cl.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (Edit) getActivity(),year,month,day);

    }
    public void onDateSet(android.widget.DatePicker view,int year,int monthOfYear,int dayOfMonth){

    }
}