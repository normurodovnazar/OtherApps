package com.normurodov_nazar.dayschedule.activities;

import static com.normurodov_nazar.dayschedule.Public.Hey.print;
import static com.normurodov_nazar.dayschedule.Public.Hey.showTimePickerDialog;
import static com.normurodov_nazar.dayschedule.Public.Key.isAdded;
import static com.normurodov_nazar.dayschedule.Public.Key.lastWork;
import static com.normurodov_nazar.dayschedule.Public.Key.purpose;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.normurodov_nazar.dayschedule.Customs.Purpose;
import com.normurodov_nazar.dayschedule.Customs.Time;
import com.normurodov_nazar.dayschedule.Customs.Work;
import com.normurodov_nazar.dayschedule.Public.Hey;
import com.normurodov_nazar.dayschedule.R;
import com.normurodov_nazar.dayschedule.databinding.ActivityWorkBinding;

public class WorkActivity extends AppCompatActivity {
    int hour,minute=-1;
    ActivityWorkBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityWorkBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        if (purpose==Purpose.editWork){
            //TODO:Need editing stuff
            hour = lastWork.getTime().getHour();
            minute = lastWork.getTime().getMinute();
            setTimeText();
            b.titleWork.setText(lastWork.getTitle());
        }
        b.setTime.setOnClickListener(x->{
            showTimePickerDialog(this, (timePicker, i, i1) -> {
                print("data", String.valueOf(i)+i1);
                minute = i1;
                hour = i;
                setTimeText();
            });
        });
        b.done.setOnClickListener(c->{
            String text = b.titleWork.getText().toString();
            if (!text.replaceAll(" ","").isEmpty() && minute!=-1){
                lastWork = new Work(text,new Time(hour,minute),true);
                setResult(RESULT_OK);
                if (purpose==Purpose.newWork) {
                    isAdded = true;
                    Hey.addNewWork(this);
                } else {
                    isAdded = false;
                    Hey.editWork(this);
                }
                finish();
            }else Toast.makeText(this, R.string.setTimeAndTitle, Toast.LENGTH_SHORT).show();
        });
    }

    private void setTimeText() {
        b.timeWork.setText(getString(R.string.time, Hey.getStringTime(hour,minute)));
    }
}