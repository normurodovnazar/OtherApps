package com.normurodov_nazar.sample;

import static android.view.View.GONE;

import static com.normurodov_nazar.sample.Keys.cPosition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.normurodov_nazar.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    ActivityMainBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        preferences = getPreferences(MODE_PRIVATE);
        CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                b.splash.setVisibility(GONE);
                if (!preferences.getBoolean("a",false)){
                    b.helloPage.setVisibility(View.VISIBLE);
                    b.nextButton.setOnClickListener(view -> {
                        b.helloPage.setVisibility(GONE);
                        showData();
                        preferences.edit().putBoolean("a",true).apply();
                    });
                }else showData();
            }
        };
        countDownTimer.start();
    }

    private void showData() {
        b.helloPage.setVisibility(GONE);
        b.recycler.setVisibility(View.VISIBLE);
        Adapter adapter = new Adapter(this, position -> {
            cPosition = position;
            startActivity(new Intent(this,DataActivity.class));
        });
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);
    }
}