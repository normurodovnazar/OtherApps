package com.normurodov_nazar.dayschedule.Public;

import static com.normurodov_nazar.dayschedule.Public.Key.active;
import static com.normurodov_nazar.dayschedule.Public.Key.hour;
import static com.normurodov_nazar.dayschedule.Public.Key.keys;
import static com.normurodov_nazar.dayschedule.Public.Key.lastWork;
import static com.normurodov_nazar.dayschedule.Public.Key.minute;
import static com.normurodov_nazar.dayschedule.Public.Key.title;
import static com.normurodov_nazar.dayschedule.Public.Key.time;
import static com.normurodov_nazar.dayschedule.Public.Key.works;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TimePicker;

import com.normurodov_nazar.dayschedule.Customs.Time;
import com.normurodov_nazar.dayschedule.Customs.Work;
import com.normurodov_nazar.dayschedule.activities.WorkActivity;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

public class Hey {

    public static ArrayList<Work> getAllWorks(Context context){
        ArrayList<Work> workList= new ArrayList<>();
        SharedPreferences keys = context.getSharedPreferences(Key.keys,Context.MODE_PRIVATE),works = context.getSharedPreferences(Key.works, Context.MODE_PRIVATE);
        Map<String,?> data = keys.getAll();
        for (Map.Entry<String,?> entry : data.entrySet()){
            workList.add(getWork(entry.getKey(),works));
        }
        return workList;
    }

    public static String getStringTime(int hour, int minute) {
        return (hour<10 ? "0"+hour : hour)+":"+(minute<10 ? "0"+minute : minute);
    }
    public static String getStringTime(Work work) {
        return getStringTime(work.getTime().getHour(),work.getTime().getMinute());
    }

    public static void addNewWork(WorkActivity workActivity) {
        String id = lastWork.getTime().toString();
        SharedPreferences keys = workActivity.getSharedPreferences(Key.keys,Context.MODE_PRIVATE),works = workActivity.getSharedPreferences(Key.works,Context.MODE_PRIVATE);
        keys.edit().putBoolean(id,false).apply();
        works.edit()
                .putString(id+title,lastWork.getTitle())
                .putInt(id+hour,lastWork.getTime().getHour())
                .putInt(id+minute,lastWork.getTime().getMinute()).putBoolean(id+active,true).apply();
    }

    public static Work getWork(String key,SharedPreferences preferences){
        return new Work(preferences.getString(key+title,""),new Time(preferences.getInt(key+hour,0),preferences.getInt(key+minute,0)),preferences.getBoolean(key+active,false));
    }

    public static void changeStatus(Context context, String key, boolean value){
        SharedPreferences preferences = context.getSharedPreferences(works,Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key+active,value).apply();
    }

    public static void showTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener onTimeSetListener){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener::onTimeSet, getHour(), getMinute(), true);
        timePickerDialog.show();
    }

    public static int getHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(){
        int i = 0;
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static void print(String a,String b){
        Log.e(a,b);
    }

    public static void editWork(WorkActivity workActivity) {
        String id = lastWork.getTime().toString();
        SharedPreferences works = workActivity.getSharedPreferences(Key.works,Context.MODE_PRIVATE);
        works.edit()
                .putString(id+title,lastWork.getTitle())
                .putInt(id+hour,lastWork.getTime().getHour())
                .putInt(id+minute,lastWork.getTime().getMinute()).putBoolean(id+active,true).apply();
    }
}
