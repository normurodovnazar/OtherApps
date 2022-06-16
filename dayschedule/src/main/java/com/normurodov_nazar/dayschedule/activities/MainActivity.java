package com.normurodov_nazar.dayschedule.activities;

import static com.normurodov_nazar.dayschedule.Public.Hey.getAllWorks;
import static com.normurodov_nazar.dayschedule.Public.Hey.print;
import static com.normurodov_nazar.dayschedule.Public.Key.isAdded;
import static com.normurodov_nazar.dayschedule.Public.Key.lastWork;
import static com.normurodov_nazar.dayschedule.Public.Key.purpose;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.normurodov_nazar.dayschedule.ClickListener;
import com.normurodov_nazar.dayschedule.Customs.Purpose;
import com.normurodov_nazar.dayschedule.Customs.Work;
import com.normurodov_nazar.dayschedule.Customs.WorksAdapter;
import com.normurodov_nazar.dayschedule.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    WorksAdapter adapter;
    ActivityResultLauncher<Intent> launcher;
    ActivityMainBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result!=null){
                if (result.getResultCode()==RESULT_OK){
                    print("w",lastWork.toString());
                    if (isAdded) adapter.addItem();
                }
            }
        });
        b.add.setOnClickListener(c->{
            purpose = Purpose.newWork;
            launch();
        });
        showWorks();
    }

    private void showWorks() {
        ArrayList<Work> works = getAllWorks(this);
        adapter = new WorksAdapter(this, works, (position, work) -> {
            purpose = Purpose.editWork;
            lastWork = work;
            launch();
        });
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);
    }

    private void launch(){
        launcher.launch(new Intent(MainActivity.this,WorkActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            for (Work work : getAllWorks(this)){
                print("w",work.toString());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}