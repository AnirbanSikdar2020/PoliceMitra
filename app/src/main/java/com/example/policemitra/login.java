package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
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

public class login extends AppCompatActivity {

    loader loader = new loader(login.this);
    forgot_password forgot = new forgot_password(login.this);

    TextView signup,forgotPwd;
    int counter = 0;
    EditText password, email;
    ImageButton eye;
    Boolean show_pwd = false;
    Button signin;
    private FirebaseAuth mAuth;

    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;


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
        forgotPwd = findViewById(R.id.fpwd);
        password.setTransformationMethod(new PasswordTransformationMethod());
        mAuth = FirebaseAuth.getInstance();
        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot.forgotPasswordShow();
            }
        });

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
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(login.this, "Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(login.this, MainActivity.class);
                                            startActivity(intent);
                                            loader.loaderHide();
                                        } else {
                                            Toast.makeText(login.this, "Please check your email or password", Toast.LENGTH_SHORT).show();
                                            loader.loaderHide();
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

//                if (password.getText().toString().isEmpty()) {
//                    password.setError("Please Enter Password");
//                } else {
                    if (show_pwd == true) {
                        eye.setImageResource(R.drawable.hidden);
                        password.setTransformationMethod(new PasswordTransformationMethod());
                        show_pwd = false;
                    } else {
                        eye.setImageResource(R.drawable.password_eye);
                        password.setTransformationMethod(null);
                        show_pwd = true;
                    }
//                }
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