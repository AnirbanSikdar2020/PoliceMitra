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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class approve_verification_admin extends AppCompatActivity {

    Intent intent;
    String file;
    ImageView back;
    ImageButton setdate;
    EditText  name, details, aadhar, location, ps, dob, status;
    TextView fNumber;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button reg;
    FirebaseAuth mAuth;
    loader loader;
    TextInputLayout textInputLayoutDob, textInputLayoutStatus, textInputLayoutFileNumber, textInputLayoutDetails, textInputLayoutLocation, textInputLayoutPS, textInputLayoutAadhar, textInputLayoutName, textInputLayoutGender;
    int counter = 0;
    RadioGroup genderGroup;
    FirebaseStorage storage;
    FirebaseDatabase database;
    String sel_date, gender;
    Boolean Imageflag=false;
    int date, year, month, hour, minute;
    int d, y, mon, h, m;
    ImageView profile_img;
    Uri uri;
    private ActivityResultLauncher<String> imagePickerLauncher;
    DocumentReference docRef,updateData;
    String genderFirebase,email;
    RadioButton male, female, others;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_verification_admin);
        intent = getIntent();
        file = intent.getStringExtra("id");
//        Toast.makeText(this, file, Toast.LENGTH_SHORT).show();
        fNumber = findViewById(R.id.F_Number);
        name = findViewById(R.id.name);
        details = findViewById(R.id.details);
        aadhar = findViewById(R.id.aadhar);
        location = findViewById(R.id.location);
        status = findViewById(R.id.status);
        ps = findViewById(R.id.police_station);
        reg = findViewById(R.id.Registration);
        male = findViewById(R.id.male_radio_button);
        female = findViewById(R.id.female_radio_button);
        others = findViewById(R.id.others_radio_button);
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
        loader = new loader(approve_verification_admin.this);
        loader.loaderShow();
        back = findViewById(R.id.back);
        genderGroup = findViewById(R.id.gender_radio_group);
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        profile_img = findViewById(R.id.profile_imgs);

//        if(file!=null || file!="")
//        {
//            database.getReference().child(file).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String image = snapshot.getValue(String.class);
//                    Picasso.get().load(image).into(profile_img);
//                    loader.loaderHide();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        else {
//            profile_img.setImageResource(R.drawable.img);
//        }
//        loader.loaderShow();
        docRef = db.collection("verifications")
                .document(file);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("Name"));
                        fNumber.setText(document.getString("File Number"));
                        aadhar.setText(document.getString("Aadhar"));
                        status.setText(document.getString("Status"));
                        details.setText(document.getString("Details"));
                        ps.setText(document.getString("Police Station"));
                        location.setText(document.getString("Location"));
                        dob.setText(document.getString("Dob"));
                        email = document.getString("User Email");
                        String[] dates = document.getString("Dob").split("/");
                        date = Integer.valueOf(dates[0]);
                        month = Integer.valueOf(dates[1]) - 1;
                        year = Integer.valueOf(dates[2]);
                        genderFirebase = document.getString("Gender");
//                        if (document.getString("Gender").equals("Male")) {
//                            male.setChecked(true);
//                        } else if (document.getString("Gender").equals("Female")) {
//                            female.setChecked(true);
//                        } else {
//                            others.setChecked(true);
//                        }
                        loader.loaderHide();
                    } else {
                        Toast.makeText(approve_verification_admin.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                    loader.loaderHide();
                } else {
                    Toast.makeText(approve_verification_admin.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loader.loaderHide();
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
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
                                                    Toast.makeText(approve_verification_admin.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
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

                if (counter <= 0 && Imageflag==true && ps.getText().toString().trim()!=""
                        && status.getText().toString().trim()!="" && dob.getText().toString().trim()!=""
                        && location.getText().toString().trim()!="" && aadhar.getText().toString().trim()!=""
                        && details.getText().toString().trim()!="" && name.getText().toString().trim()!="") {
                    loader.loaderShow();
                    HashMap<String, Object> crimeDetails = new HashMap<>();
                    crimeDetails.put("File Number", fNumber.getText().toString());
                    crimeDetails.put("Name", name.getText().toString());
                    crimeDetails.put("Details", details.getText().toString());
                    crimeDetails.put("Aadhar", aadhar.getText().toString());
                    crimeDetails.put("Location", location.getText().toString());
                    crimeDetails.put("Status", status.getText().toString());
                    crimeDetails.put("Doc", dob.getText().toString());
                    crimeDetails.put("Gender", gender);
                    crimeDetails.put("Police Station", ps.getText().toString());

                    db.collection("criminalRecords")
                            .document(fNumber.getText().toString())
                            .set(crimeDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loader.loaderHide();

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("Aadhar", aadhar.getText().toString());
                                    updates.put("Details", details.getText().toString());
                                    updates.put("Dob", dob.getText().toString());
                                    updates.put("Location", location.getText().toString());
                                    updates.put("Name", name.getText().toString());
                                    updates.put("File Number", fNumber.getText().toString());
                                    updates.put("Status", status.getText().toString());
                                    updates.put("Police Station", ps.getText().toString());
                                    updateData = db.collection("verifications").document(email+"-"+aadhar.getText().toString());
                                    updateData.update(updates)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(approve_verification_admin.this, "Updated",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(approve_verification_admin.this, crime_registration.class);
                                                    startActivity(intent);
                                                    loader.loaderHide();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loader.loaderHide();
                                                    Toast.makeText(approve_verification_admin.this, "Data not updated", Toast.LENGTH_SHORT).show();
                                                }
                                            });
//                                    db.collection("verifications").document(file)
//                                            .delete()
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//
//                                                }
//                                            });
//

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loader.loaderHide();
                                    Toast.makeText(approve_verification_admin.this, "Update failed, try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    loader.loaderHide();
                }
             else if (Imageflag==false) {
                Toast.makeText(approve_verification_admin.this, "Select image to continue", Toast.LENGTH_SHORT).show();
            }
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(approve_verification_admin.this, admin_verification.class);
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
        new DatePickerDialog(approve_verification_admin.this, (datePicker, year, month, date) -> {
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
