package com.example.policemitra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class registration extends AppCompatActivity {

    TextView signin;
    ImageButton setdate;
    String sel_date;
    EditText dob;
    int date, year, month, hour, minute;
    int d, y, mon, h, m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        signin = findViewById(R.id.signin);
        setdate = findViewById(R.id.calendar);
        dob = findViewById(R.id.dob);
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this,login.class);
                startActivity(intent);
            }
        });

        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
    }

    public void setDate(View view) {
        new DatePickerDialog(registration.this, (datePicker, year, month, date) -> {

            d=date;
            mon=month;
            y=year;
            sel_date = "" + date + "/" + (month + 1) + "/" + year;
            dob.setText(sel_date);
        }, year, month, date).show();
    }
    public void showToast(String val){
        Toast.makeText(registration.this,val, Toast.LENGTH_SHORT).show();
    }
}