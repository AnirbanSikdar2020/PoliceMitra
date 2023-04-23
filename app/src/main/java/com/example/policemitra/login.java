package com.example.policemitra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class login extends AppCompatActivity {

    TextView signup;
    EditText password;
    ImageButton eye;
    Boolean show_pwd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        signup = findViewById(R.id.signup);
        password = findViewById(R.id.pwd);
        eye = findViewById(R.id.pwdEye);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,registration.class);
                startActivity(intent);
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().isEmpty()){
                    password.setError("Please Enter Password");
                }
                else
                {
                    if(show_pwd==true){
                        password.setTransformationMethod(null);
                        show_pwd = false;
                    }
                    else
                    {
                        password.setTransformationMethod(new PasswordTransformationMethod());
                        show_pwd = true;
                    }
                }
            }
        });
    }
}