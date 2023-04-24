package com.example.policemitra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class login extends AppCompatActivity {

    TextView signup;
    EditText password;
    ImageButton eye;
    Boolean show_pwd = false;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        password = findViewById(R.id.pwd);
        eye = findViewById(R.id.pwdEye);
        password.setTransformationMethod(new PasswordTransformationMethod());

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,home.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,registration.class);
                startActivity(intent);
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(password.getText().toString().isEmpty()){
                    password.setError("Please Enter Password");
                }
                else
                {
                    if(show_pwd==true){
                        password.setTransformationMethod(new PasswordTransformationMethod());
                        show_pwd = false;
                    }
                    else
                    {
                        password.setTransformationMethod(null);
                        show_pwd = true;
                    }
                }
            }
        });
    }
    public void showToast(String val){
        Toast.makeText(login.this,val, Toast.LENGTH_SHORT).show();
    }
}