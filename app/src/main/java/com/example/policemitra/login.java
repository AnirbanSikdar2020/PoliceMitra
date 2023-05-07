package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class login extends AppCompatActivity {

    loader loader = new loader(login.this);
    TextView signup;
    int counter = 0;
    EditText password, email;
    ImageButton eye;
    Boolean show_pwd = false;
    Button signin;
    private FirebaseAuth mAuth;

    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    @Override
    public void onStart() {
        super.onStart();
        loader.loaderShow();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            loader.loaderHide();
        }
        else
        {
            loader.loaderHide();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loader = new loader(login.this);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        password = findViewById(R.id.pwd);
        email = findViewById(R.id.email);
        eye = findViewById(R.id.pwdEye);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutPassword = findViewById(R.id.textInputPwd);
        password.setTransformationMethod(new PasswordTransformationMethod());
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText())) {
                    showError(textInputLayoutEmail, "Email cannot be blank");
                    counter++;
                } else {
                    textInputLayoutEmail.setError(null);
                    counter--;
                }

                if (String.valueOf(password.getText()).length() >= 6) {
                    textInputLayoutPassword.setError(null);
                    if (counter <= 0) {
                        loader.loaderShow();
                        String Uemail, Upassword;
                        Uemail = String.valueOf(email.getText());
                        Upassword = String.valueOf(password.getText());

                        mAuth.signInWithEmailAndPassword(Uemail, Upassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(login.this, "Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(login.this, MainActivity.class);
                                            startActivity(intent);
                                            loader.loaderHide();
//                                    updateUI(user);
                                        } else {
                                            Toast.makeText(login.this, "Please check your email or password", Toast.LENGTH_SHORT).show();
                                            loader.loaderHide();
                                            // If sign in fails, display a message to the user.
//
//                                    updateUI(null);
                                        }
                                    }
                                });

                    }
                } else {
                    showError(textInputLayoutPassword, "Password must be at least 6 characters");
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.getText().toString().isEmpty()) {
                    password.setError("Please Enter Password");
                } else {
                    if (show_pwd == true) {
                        password.setTransformationMethod(new PasswordTransformationMethod());
                        show_pwd = false;
                    } else {
                        password.setTransformationMethod(null);
                        show_pwd = true;
                    }
                }
            }
        });
    }

    public void showToast(String val) {
        Toast.makeText(login.this, val, Toast.LENGTH_SHORT).show();
    }

    public void showError(TextInputLayout tag, String message) {
        tag.setError(message);
    }
}