package com.example.policemitra;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

public class crime_add extends AppCompatActivity {

    ImageView back;
    ImageButton setdate;
    EditText fNumber, name, details, aadhar, location, ps, dob, status;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button reg;
    Boolean Imageflag=false;
    FirebaseAuth mAuth;
    loader loader;
    TextInputLayout textInputLayoutDob, textInputLayoutStatus, textInputLayoutFileNumber, textInputLayoutDetails, textInputLayoutLocation, textInputLayoutPS, textInputLayoutAadhar, textInputLayoutName, textInputLayoutGender;
    int counter = 0;
    RadioGroup genderGroup;
    FirebaseStorage storage;
    FirebaseDatabase database;
    String sel_date, gender;
    int date, year, month, hour, minute;
    int d, y, mon, h, m;
    ImageView profile_img;
    Uri uri;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_add);
        fNumber = findViewById(R.id.F_Number);
        name = findViewById(R.id.name);
        details = findViewById(R.id.details);
        aadhar = findViewById(R.id.aadhar);
        location = findViewById(R.id.location);
        ps = findViewById(R.id.police_station);
        reg = findViewById(R.id.Registration);
        setdate = findViewById(R.id.calendar);
        dob = findViewById(R.id.dob);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        textInputLayoutFileNumber = findViewById(R.id.textInputFileNumber);
        textInputLayoutName = findViewById(R.id.textInputName);
        textInputLayoutDetails = findViewById(R.id.textInputDetails);
        textInputLayoutAadhar = findViewById(R.id.textInputAadhar);
        textInputLayoutGender = findViewById(R.id.textInputGender);
        textInputLayoutLocation = findViewById(R.id.textInputLocation);
        textInputLayoutPS = findViewById(R.id.textInputPoliceStation);
        textInputLayoutStatus = findViewById(R.id.textInputStatus);
        textInputLayoutDob = findViewById(R.id.textInputDob);
        loader = new loader(crime_add.this);
        back = findViewById(R.id.back);
        genderGroup = findViewById(R.id.gender_radio_group);
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        profile_img = findViewById(R.id.profile_img);
        status = findViewById(R.id.status);
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        //File Number
                        if (TextUtils.isEmpty(fNumber.getText().toString().trim())) {
                            showError(textInputLayoutFileNumber, "Enter file number first");
                        } else {
                            textInputLayoutFileNumber.setError(null);
                            loader.loaderShow();
                            Uri uri = result;
                            profile_img.setImageURI(uri);
                            final StorageReference reference = storage.getReference()
                                    .child(fNumber.getText().toString());
                            Imageflag=true;
                            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            database.getReference().child(fNumber.getText().toString()).setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    loader.loaderHide();
                                                    Toast.makeText(crime_add.this, "Image Added Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }

                });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set up the image picker launcher
                launchImagePicker();
            }
        });
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
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //File Number
                if (TextUtils.isEmpty(fNumber.getText().toString().trim())) {
                    showError(textInputLayoutFileNumber, "File number cannot be blank");
                    counter++;
                } else {
                    textInputLayoutFileNumber.setError(null);
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
                //Details validation
                if (TextUtils.isEmpty(details.getText().toString().trim())) {
                    showError(textInputLayoutDetails, "Details cannot be blank");
                    counter++;
                } else {
                    textInputLayoutDetails.setError(null);
                    counter--;
                }
                //aadhar validation
                if (TextUtils.isEmpty(aadhar.getText().toString().trim()) || String.valueOf(aadhar.getText()).trim().length() != 12) {
                    showError(textInputLayoutAadhar, "Aadhar number cannot be blank");
                    counter++;
                } else {
                    textInputLayoutAadhar.setError(null);
                    counter--;
                }
                //Location validation
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
                //Status
                if (TextUtils.isEmpty(status.getText().toString().trim())) {
                    showError(textInputLayoutStatus, "Status cannot be blank");
                    counter++;
                } else {
                    textInputLayoutStatus.setError(null);
                    counter--;
                }
                //PS validation
                if (TextUtils.isEmpty(ps.getText().toString().trim())) {
                    showError(textInputLayoutPS, "Police Station cannot be blank");
                    counter++;
                } else {
                    textInputLayoutPS.setError(null);
                    counter--;
                }
                //Gender
                if (gender == null) {
                    showError(textInputLayoutGender, "Gender cannot be blank");
                    counter++;
                } else {
                    textInputLayoutGender.setError(null);
                    counter--;
                }

                if (counter <= 0 && Imageflag==true) {
                    loader.loaderShow();
                    HashMap<String, Object> crimeDetails = new HashMap<>();
                    crimeDetails.put("File Number", fNumber.getText().toString());
                    crimeDetails.put("Name", name.getText().toString());
                    crimeDetails.put("Details", details.getText().toString());
                    crimeDetails.put("Aadhar", aadhar.getText().toString());
                    crimeDetails.put("Location", location.getText().toString());
                    crimeDetails.put("Status", status.getText().toString());
                    crimeDetails.put("Dob", dob.getText().toString());
                    crimeDetails.put("Gender", gender);
                    crimeDetails.put("Police Station", ps.getText().toString());

                    db.collection("criminalRecords")
                            .document(fNumber.getText().toString())
                            .set(crimeDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loader.loaderHide();
                                    Toast.makeText(crime_add.this, "Registration Successful",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(crime_add.this, crime_registration.class);
                                    startActivity(intent);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loader.loaderHide();
                                    Toast.makeText(crime_add.this, "Registration failed, try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    loader.loaderHide();
                } else if (Imageflag==false) {
                    Toast.makeText(crime_add.this, "Select image to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(crime_add.this, crime_registration.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
    }

    public void setDate(View view) {
        new DatePickerDialog(crime_add.this, (datePicker, year, month, date) -> {
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

    private void launchImagePicker() {
        imagePickerLauncher.launch("image/*");
    }

}