package com.example.policemitra;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class registration extends AppCompatActivity {

    TextView signin,signup;
    ImageButton setdate;
    String sel_date;
    EditText name,phone,email,aadhar,dob,password,confPassword;
    RadioGroup genderGroup;
    RadioButton gender;
    FirebaseAuth mAuth;
    int date, year, month, hour, minute;
    int d, y, mon, h, m, counter=0;

    TextInputLayout textInputLayoutPhone,textInputLayoutEmail,textInputLayoutAadhar,textInputLayoutName,textInputLayoutDob,textInputLayoutGender,textInputLayoutCpassword,textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textInputLayoutName = findViewById(R.id.textInputName);
        textInputLayoutPhone = findViewById(R.id.textInputPhone);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutGender = findViewById(R.id.textInputGender);
        textInputLayoutAadhar = findViewById(R.id.textInputAadhar);
        textInputLayoutDob = findViewById(R.id.textInputDob);
        textInputLayoutPassword = findViewById(R.id.textInputPwd);
        textInputLayoutCpassword = findViewById(R.id.textInputCpwd);
        mAuth = FirebaseAuth.getInstance();
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        setdate = findViewById(R.id.calendar);
        dob = findViewById(R.id.dob);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        genderGroup = findViewById(R.id.genderGroup);
        int selectedId = genderGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        gender = findViewById(selectedId);
        aadhar = findViewById(R.id.aadhar);
        password = findViewById(R.id.pwd);
        confPassword = findViewById(R.id.cpwd);

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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //phone validation
                if(TextUtils.isEmpty(phone.getText()) || String.valueOf(phone.getText()).length()!=10)
                {
                    showError(textInputLayoutPhone,"Please enter correct phone number");
                    counter++;
                }
                else
                {
                    textInputLayoutPhone.setError(null);
                    counter--;
                }

                //aadhar validation
                if(TextUtils.isEmpty(aadhar.getText()) || String.valueOf(aadhar.getText()).length()!=12)
                {
                    showError(textInputLayoutAadhar,"Please enter correct aadhar number");
                    counter++;
                }
                else
                {
                    textInputLayoutAadhar.setError(null);
                    counter--;
                }

                if(TextUtils.isEmpty(name.getText()))
                {
                    showError(textInputLayoutName,"Name cannot be blank");
                    counter++;
                }
                else
                {
                    textInputLayoutName.setError(null);
                    counter--;
                }

                if(TextUtils.isEmpty(email.getText()))
                {
                    showError(textInputLayoutEmail,"Email cannot be blank");
                    counter++;
                }
                else
                {
                    textInputLayoutEmail.setError(null);
                    counter--;
                }

//                if(TextUtils.isEmpty(gender.getText()))
//                {
//                    showError(textInputLayoutGender,"Gender cannot be blank");
//                    counter++;
//                }
//                else
//                {
//                    textInputLayoutGender.setError(null);
//                    counter--;
//                }

                if(TextUtils.isEmpty(dob.getText()))
                {
                    showError(textInputLayoutDob,"DOB cannot be blank");
                    counter++;
                }
                else
                {
                    textInputLayoutDob.setError(null);
                    counter--;
                }

                if(String.valueOf(password.getText()).equals(String.valueOf(confPassword.getText())))
                {
                    if(String.valueOf(password.getText()).length()>=6) {
                        textInputLayoutPassword.setError(null);
                        textInputLayoutCpassword.setError(null);
                        if(counter<=0) {
                            String Uname, Uphone, Uemail, Ugender, Uaadhar, Udob, Upassword;
                            Uname = String.valueOf(name.getText());
                            Uphone = String.valueOf(phone.getText());
                            Uemail = String.valueOf(email.getText());
//                            Ugender = String.valueOf(gender.getText());
                            Uaadhar = String.valueOf(aadhar.getText());
                            Udob = String.valueOf(dob.getText());
                            Upassword = String.valueOf(password.getText());

                            Toast.makeText(registration.this, "Success", Toast.LENGTH_SHORT).show();
                                                mAuth.createUserWithEmailAndPassword(Uemail, Upassword)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Sign in success, update UI with the signed-in user's information
                            //                                        Log.d(TAG, "createUserWithEmail:success");
                                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                                    Toast.makeText(registration.this, "Authentication success.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    Intent intentLogin = new Intent(registration.this,MainActivity.class);
                                                                    startActivity(intentLogin);

                                                                } else {
                                                                    // If sign in fails, display a message to the user.
                                                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                                    Toast.makeText(registration.this, "Authentication failed.",
                                                                            Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });
                        }
                    }
                    else
                    {
                        showError(textInputLayoutPassword,"Password must be at least 6 characters");
                    }
                }
                else
                {
                    showError(textInputLayoutPassword,"Password and Confirm Password are not same");
                    showError(textInputLayoutCpassword,"Password and Confirm Password are not same");
                }
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

    public void showError(TextInputLayout tag ,String message){
        tag.setError(message);
    }
}