package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Verification_submit extends AppCompatActivity {

    ImageView back;
    TextView dob,fileName;
    EditText aadhar,name,location,details;
    int date, year, month, hour, minute;
    int d, y, mon, counter = 0;
    ImageButton setdate;
    Button submit,choose_file;
    String sel_date,emailId;
    DBHelper DB;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    loader loader;
    Uri fileUri;
    Intent intent;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DocumentReference updateData;
    String upldFileName,upldFileExtn;
    TextInputLayout textInputLayoutLocation, textInputLayoutAadhar, textInputLayoutName, textInputLayoutDob;
    private static final int FILE_SELECT_REQUEST_CODE = 2;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_submit);
        textInputLayoutName = findViewById(R.id.textInputName);
        DB = new DBHelper(this);
        choose_file = findViewById(R.id.uploadButton);
        textInputLayoutAadhar = findViewById(R.id.textInputAadhar);
        textInputLayoutDob = findViewById(R.id.textInputDob);
        textInputLayoutLocation = findViewById(R.id.textInputLocation);
        loader = new loader(Verification_submit.this);
        aadhar = findViewById(R.id.aadhar);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        details = findViewById(R.id.details);
        dob = findViewById(R.id.dob);
        setdate = findViewById(R.id.calendar);
        submit= findViewById(R.id.submit);
        back = findViewById(R.id.back);
        fileName=findViewById(R.id.fileName);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(Verification_submit.this, MainActivity.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                access();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
                    intent.setType("*/*");
                    startActivityForResult(intent, FILE_SELECT_REQUEST_CODE);

                }
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
                    crimeDetails.put("File Url", "");

                    db.collection("verifications")
                            .document(emailId + "-" + aadhar.getText().toString().trim())
                            .set(crimeDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if(fileUri!=null)
                                    {
                                        upldFileName = fileName.getText().toString();
                                        String[] fileExt = upldFileName.split("\\.");
                                        upldFileName = fileExt[0];
                                        upldFileExtn = fileExt[1];
//                                        upldFileName = fileName.getText().toString();
//                                        upldFileName = upldFileName.replaceAll("[@.]*", "");
                                        String names = aadhar.getText().toString()+"-"+name.getText().toString()+"-"+upldFileName+"-verify";
                                        final StorageReference reference = storage.getReference()
                                                .child(names);
                                        reference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        database.getReference().child(names).setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Map<String, Object> updates = new HashMap<>();
                                                                updates.put("File Url", upldFileName);
                                                                updates.put("File Ext", upldFileExtn);
                                                                updateData = db.collection("verifications").document(String.valueOf(emailId.trim()+"-"+aadhar.getText()).trim());
                                                                updateData.update(updates)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(Verification_submit.this, "Please check your mail for update",
                                                                                        Toast.LENGTH_LONG).show();
                                                                                Toast.makeText(Verification_submit.this, "We will revert back within 48hours",
                                                                                        Toast.LENGTH_LONG).show();
                                                                                Intent intent = new Intent(Verification_submit.this, MainActivity.class);
                                                                                startActivity(intent);
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(Verification_submit.this, "Try again", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                loader.loaderHide();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                        loader.loaderHide();
                                    }
                                    else
                                    {
                                        Toast.makeText(Verification_submit.this, "Please check your mail for update",
                                                Toast.LENGTH_LONG).show();
                                        Toast.makeText(Verification_submit.this, "We will revert back within 48hours",
                                                Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Verification_submit.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loader.loaderHide();
                                    Toast.makeText(Verification_submit.this, "Registration failed, try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    loader.loaderHide();
                }
            }
        });

    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
           if(requestCode == FILE_SELECT_REQUEST_CODE){
                if (data != null) {
                    fileUri = data.getData();
                    String file_name = getFileNameFromUri(fileUri);
                    if(file_name!=null){
                        fileName.setText(file_name);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setDate(View view) {
        new DatePickerDialog(Verification_submit.this, (datePicker, year, month, date) -> {
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

    public void access() {
        if (ContextCompat.checkSelfPermission(Verification_submit.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Verification_submit.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Verification_submit.this, new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, PackageManager.PERMISSION_GRANTED);
        }
    }

    public String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                fileName = cursor.getString(displayNameIndex);
                cursor.close();
            }
        }

        return fileName;
    }
}