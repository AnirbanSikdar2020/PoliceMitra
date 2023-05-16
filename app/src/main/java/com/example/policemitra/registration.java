package com.example.policemitra;

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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class registration extends AppCompatActivity {
    otp_page otp = new otp_page(registration.this);
    TextView signin, signup;
    ImageButton setdate;
    String sel_date, gender;
    EditText name, phone, email, aadhar, dob, password, confPassword;
    RadioGroup genderGroup;
    FirebaseAuth mAuth;
    int date, year, month, hour, minute;
    int d, y, mon, h, m, counter = 0;

    TextInputLayout textInputLayoutPhone, textInputLayoutEmail, textInputLayoutAadhar, textInputLayoutName, textInputLayoutDob, textInputLayoutGender, textInputLayoutCpassword, textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textInputLayoutName = findViewById(R.id.textInputName);
        textInputLayoutPhone = findViewById(R.id.textInputPhone);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutAadhar = findViewById(R.id.textInputAadhar);
        textInputLayoutGender = findViewById(R.id.textInputGender);
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
        genderGroup = findViewById(R.id.gender_radio_group);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    gender = checkedRadioButton.getText().toString();
                }
            }
        });

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
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //phone validation
                if (TextUtils.isEmpty(phone.getText().toString().trim()) || String.valueOf(phone.getText()).trim().length() != 10) {
                    showError(textInputLayoutPhone, "Please enter correct phone number");
                    counter++;
                } else {
                    textInputLayoutPhone.setError(null);
                    counter--;
                }

//                aadhar validation
                if (TextUtils.isEmpty(aadhar.getText().toString().trim()) || String.valueOf(aadhar.getText()).trim().length() != 12) {
                    showError(textInputLayoutAadhar, "Please enter correct aadhar number");
                    counter++;
                } else {
                    textInputLayoutAadhar.setError(null);
                    counter--;
                }
//              Name
                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    showError(textInputLayoutName, "Name cannot be blank");
                    counter++;
                } else {
                    textInputLayoutName.setError(null);
                    counter--;
                }
//              Email
                if (TextUtils.isEmpty(email.getText().toString().trim())) {
                    showError(textInputLayoutEmail, "Email cannot be blank");
                    counter++;
                } else {
                    textInputLayoutEmail.setError(null);
                    counter--;
                }
//              Gender
                if (gender == null) {
                    showError(textInputLayoutGender, "Gender cannot be blank");
                    counter++;
                } else {
                    textInputLayoutGender.setError(null);
                    counter--;
                }
                //DOB
                if (TextUtils.isEmpty(dob.getText().toString().trim())) {
                    showError(textInputLayoutDob, "DOB cannot be blank");
                    counter++;
                } else {
                    textInputLayoutDob.setError(null);
                    counter--;
                }

                if (String.valueOf(password.getText()).equals(String.valueOf(confPassword.getText()))) {
                    if (String.valueOf(password.getText()).length() >= 6) {
                        textInputLayoutPassword.setError(null);
                        textInputLayoutCpassword.setError(null);
                        if (counter <= 0) {
                            ArrayList<String> u_details = new ArrayList<>();
                            u_details.add(String.valueOf(name.getText()).trim()); //0
                            u_details.add(String.valueOf(phone.getText()).trim());//1
                            u_details.add(String.valueOf(email.getText()).trim());//2
                            u_details.add(String.valueOf(gender));//3
                            u_details.add(String.valueOf(aadhar.getText()).trim());//4
                            u_details.add(String.valueOf(dob.getText()).trim());//5
                            u_details.add(String.valueOf(password.getText()).trim());//6
                            otp.otpDialogShow(u_details, "reg");
//                            Log.i("Data",String.valueOf(u_details));
                        }

                    } else {
                        showError(textInputLayoutPassword, "Password must be at least 6 characters");
                    }
                } else {
                    showError(textInputLayoutPassword, "Password and Confirm Password are not same");
                    showError(textInputLayoutCpassword, "Password and Confirm Password are not same");
                }
            }
        });

        textInputLayoutDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
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
            d = date;
            mon = month;
            y = year;
            sel_date = "" + date + "/" + (month + 1) + "/" + year;
            dob.setText(sel_date);
        }, year, month, date).show();
    }

    public void showToast(String val) {
        Toast.makeText(registration.this, val, Toast.LENGTH_SHORT).show();
    }

    public void showError(TextInputLayout tag, String message) {
        tag.setError(message);
    }
}