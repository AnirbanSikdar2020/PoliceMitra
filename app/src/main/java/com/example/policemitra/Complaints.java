package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Complaints extends AppCompatActivity {
    ImageButton mic;
    ImageView back;
    TextView dob,fileName;
    EditText aadhar, name, location, details, ps;
    int date, year, month, hour, minute;
    int d, y, mon, counter = 0;
    ImageButton setdate;
    Button submit, choose_file;
    String sel_date, emailId;
    DBHelper DB;
    loader loader;
    Uri fileUri;
    FirebaseStorage storage;
    String upldFileName;
    Intent intent;
    FirebaseDatabase database;
    DocumentReference updateData;
    TextInputLayout textInputLayoutLocation, textInputLayoutPS, textInputLayoutDetails, textInputLayoutAadhar, textInputLayoutName, textInputLayoutDob;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int RECOGNIZER_RESULT = 1;
    private static final int FILE_SELECT_REQUEST_CODE = 2;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        details = findViewById(R.id.details);
        mic = findViewById(R.id.mic);
        setdate = findViewById(R.id.calendar);
        choose_file = findViewById(R.id.uploadButton);
        textInputLayoutName = findViewById(R.id.textInputName);
        textInputLayoutPS = findViewById(R.id.textInputNearestPS);
        textInputLayoutDetails = findViewById(R.id.textInputDetails);
        DB = new DBHelper(this);
        textInputLayoutAadhar = findViewById(R.id.textInputAadhar);
        textInputLayoutDob = findViewById(R.id.textInputDoc);
        textInputLayoutLocation = findViewById(R.id.textInputLocation);
        loader = new loader(Complaints.this);
        aadhar = findViewById(R.id.aadhar);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        details = findViewById(R.id.details);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        dob = findViewById(R.id.doc);
        ps = findViewById(R.id.nearestPS);
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        submit = findViewById(R.id.submit);
        fileName=findViewById(R.id.fileName);
        back = findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(Complaints.this, MainActivity.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        Cursor res = DB.getData();
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
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
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text");
                startActivityForResult(intent, RECOGNIZER_RESULT);
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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // aadhar validation
                if (TextUtils.isEmpty(aadhar.getText().toString().trim()) || String.valueOf(aadhar.getText()).trim().length() != 12) {
                    showError(textInputLayoutAadhar, "Incorrect aadhar number");
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
                // Details
                if (TextUtils.isEmpty(details.getText().toString().trim())) {
                    showError(textInputLayoutDetails, "Details cannot be blank");
                    counter++;
                } else {
                    textInputLayoutDetails.setError(null);
                    counter--;
                }
                // Nearest PS
                if (TextUtils.isEmpty(ps.getText().toString().trim())) {
                    showError(textInputLayoutPS, "PS cannot be blank");
                    counter++;
                } else {
                    textInputLayoutPS.setError(null);
                    counter--;
                }
                //DOB
                if (TextUtils.isEmpty(dob.getText().toString().trim())) {
                    showError(textInputLayoutDob, "DOC cannot be blank");
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
                    crimeDetails.put("Doc", dob.getText().toString());
                    crimeDetails.put("Police Station", "");
                    crimeDetails.put("Status", "Pending verification");
                    crimeDetails.put("File Url", "");

                    //upload data
                    db.collection("complaints")
                            .document(aadhar.getText().toString().trim())
                            .set(crimeDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //upload file
                                    if(fileUri!=null)
                                    {
                                        upldFileName = fileName.getText().toString();
                                        upldFileName = upldFileName.replaceAll("[@.]*", "");
                                        String names = aadhar.getText().toString()+"-"+name.getText().toString()+"-"+upldFileName+"-complaints";
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
                                                                updateData = db.collection("complaints").document(String.valueOf(aadhar.getText()).trim());
                                                                updateData.update(updates)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(Complaints.this, "Please check your mail for update",
                                                                                        Toast.LENGTH_LONG).show();
                                                                                Toast.makeText(Complaints.this, "We will revert back within 48hours",
                                                                                        Toast.LENGTH_LONG).show();
                                                                                Intent intent = new Intent(Complaints.this, MainActivity.class);
                                                                                startActivity(intent);
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(Complaints.this, "Try again", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Complaints.this, "Please check your mail for update",
                                                Toast.LENGTH_LONG).show();
                                        Toast.makeText(Complaints.this, "We will revert back within 48hours",
                                                Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Complaints.this, MainActivity.class);
                                        startActivity(intent);
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loader.loaderHide();
                                    Toast.makeText(Complaints.this, "Registration failed, try again.",
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
            if (requestCode == RECOGNIZER_RESULT) {
                ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                details.setText(matches.get(0).toString());

            }
            else if(requestCode == FILE_SELECT_REQUEST_CODE){
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
        new DatePickerDialog(Complaints.this, (datePicker, year, month, date) -> {
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
        if (ContextCompat.checkSelfPermission(Complaints.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Complaints.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Complaints.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
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