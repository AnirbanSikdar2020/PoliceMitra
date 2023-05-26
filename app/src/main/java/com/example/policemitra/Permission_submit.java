package com.example.policemitra;

import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.annotation.SuppressLint;
        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.Calendar;
        import java.util.HashMap;

public class Permission_submit extends AppCompatActivity {

    ImageView back;
    TextView dob;
    EditText aadhar,name,location,details;
    int date, year, month, hour, minute;
    int d, y, mon, counter = 0;
    ImageButton setdate;
    Button submit;
    String sel_date,emailId;
    DBHelper DB;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    loader loader;
    TextInputLayout textInputLayoutLocation, textInputLayoutAadhar, textInputLayoutName, textInputLayoutDob;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_submit);
        textInputLayoutName = findViewById(R.id.textInputName);
        DB = new DBHelper(this);
        textInputLayoutAadhar = findViewById(R.id.textInputAadhar);
        textInputLayoutDob = findViewById(R.id.textInputDob);
        textInputLayoutLocation = findViewById(R.id.textInputLocation);
        loader = new loader(Permission_submit.this);
        aadhar = findViewById(R.id.aadhar);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        details = findViewById(R.id.details);
        dob = findViewById(R.id.dob);
        setdate = findViewById(R.id.calendar);
        submit= findViewById(R.id.submit);
        back = findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(Permission_submit.this, MainActivity.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        Cursor res = DB.getData();
        if(res.getCount()>0){
            while (res.moveToNext())
            {
                emailId = String.valueOf(res.getString(1));
//                emailId = emailId.replaceAll("[@.]*", "");
            }
        }

        dob.setOnClickListener(new View.OnClickListener() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // aadhar validation
                if (TextUtils.isEmpty(aadhar.getText().toString().trim()) || String.valueOf(aadhar.getText()).trim().length() != 12) {
                    showError(textInputLayoutAadhar, "Please enter correct aadhar number");
                    counter++;
                } else {
                    textInputLayoutAadhar.setError(null);
                    counter--;
                }
                // Name
                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    showError(textInputLayoutName, "Name cannot be blank");
                    counter++;
                } else {
                    textInputLayoutName.setError(null);
                    counter--;
                }
                // Location
                if (TextUtils.isEmpty(location.getText().toString().trim())) {
                    showError(textInputLayoutLocation, "Location cannot be blank");
                    counter++;
                } else {
                    textInputLayoutLocation.setError(null);
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

                if (counter <= 0 && String.valueOf(aadhar.getText()).trim().length() == 12) {
                    loader.loaderShow();
                    HashMap<String, Object> crimeDetails = new HashMap<>();
                    crimeDetails.put("Name", name.getText().toString());
                    crimeDetails.put("Details", details.getText().toString());
                    crimeDetails.put("Aadhar", aadhar.getText().toString());
                    crimeDetails.put("Location", location.getText().toString());
                    crimeDetails.put("User Email", emailId);
                    crimeDetails.put("Dob", dob.getText().toString());
                    crimeDetails.put("Police Station", "");
                    crimeDetails.put("Status", "Pending verification");

                    db.collection("permissions")
                            .document(aadhar.getText().toString().trim())
                            .set(crimeDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loader.loaderHide();
                                    Toast.makeText(Permission_submit.this, "Please check your mail for update",
                                            Toast.LENGTH_LONG).show();
                                    Toast.makeText(Permission_submit.this, "We will revert back within 48hours",
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Permission_submit.this, MainActivity.class);
                                    startActivity(intent);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loader.loaderHide();
                                    Toast.makeText(Permission_submit.this, "Registration failed, try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    loader.loaderHide();
                }
            }
        });

    }
    public void setDate(View view) {
        new DatePickerDialog(Permission_submit.this, (datePicker, year, month, date) -> {
            d = date;
            mon = month;
            y = year;
            sel_date = "" + date + "/" + (month + 1) + "/" + year;
            dob.setText(sel_date);
        }, year, month, date).show();
    }
    public void showError(TextInputLayout tag, String message) {
        tag.setError(message);
    }
}