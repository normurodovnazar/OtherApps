package com.normurodov_nazar.contactsapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class NewContactDialog extends Dialog {
    final Context context;
    final ClickListener clickListener;
    protected NewContactDialog(Context context, ClickListener clickListener) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;
    }
    EditText name,number;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact_layout);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        add = findViewById(R.id.add);
        add.setOnClickListener(o->{
            String sName = name.getText().toString(),sNumber = number.getText().toString();
            if (!sName.isEmpty() && !sNumber.isEmpty()){
                Calendar calendar =Calendar.getInstance();
                clickListener.onClick(new Contact((int) calendar.getTimeInMillis(),sName,sNumber));
                dismiss();
            }else Toast.makeText(context, "Name or Number must not be empty", Toast.LENGTH_SHORT).show();
        });
    }
}
