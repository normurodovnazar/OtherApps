package com.normurodov_nazar.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ((TextView) findViewById(R.id.data)).setText(Keys.data.get(Keys.cPosition));
        ((TextView)findViewById(R.id.tiData)).setText(Keys.titles.get(Keys.cPosition));
    }
}