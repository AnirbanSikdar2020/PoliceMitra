package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Quick_Search_verify extends AppCompatActivity {

    ImageView back;
    ImageButton actionButton,aadharSearch;
    Bitmap bitmap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Query query;
    List<ViewCriminalModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapterCriminalView adapter;
    String fileNum;
    EditText aadhar;
    verify_dialog verify = new verify_dialog(Quick_Search_verify.this);
    loader loader = new loader(Quick_Search_verify.this);
    private static final int REQUEST_CAMERA_CODE = 100;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search_verify);
        mRecyclerView = findViewById(R.id.viewCriminal);
//        mRecyclerView.hasFixedSize(true);
        db = FirebaseFirestore.getInstance();
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        back = findViewById(R.id.back);
        aadharSearch = findViewById(R.id.aadharSearch);
        aadhar = findViewById(R.id.aadhar);
        actionButton = findViewById(R.id.fab);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(Quick_Search_verify.this, MainActivity.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                access();
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(Quick_Search_verify.this);
            }
        });
        aadharSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getAadhar = aadhar.getText().toString().trim();
                if(getAadhar.length()==12)
                {
                    search(getAadhar);
                }
                else
                {
                    Toast.makeText(Quick_Search_verify.this, "Please select Aadhar Number only", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getText(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void getText(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
//            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
//            textView_data.setText(stringBuilder.toString());
//            button_capture.setText("Retake");
//            button_copy.setVisibility(View.VISIBLE);
            if (stringBuilder.toString().trim().length() == 12) {
                search(stringBuilder.toString().trim());
            } else {
                Toast.makeText(this, "Please select Aadhar Number only", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void search(String aadhar) {
        loader.loaderShow();
        modelList.clear();
        query = db.collection("criminalRecords")
                .whereEqualTo("Aadhar", aadhar);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size()>0)
                        {
                            for (DocumentSnapshot doc : task.getResult()) {
                                fileNum = doc.getString("File Number");
                                ViewCriminalModel model = new ViewCriminalModel(doc.getString("File Number"),
                                        doc.getString("File Number"),
                                        doc.getString("Name"),
                                        doc.getString("Details"));
//
                                modelList.add(model);
                            }
                            adapter = new CustomAdapterCriminalView(Quick_Search_verify.this, modelList);
                            mRecyclerView.setAdapter(adapter);
                            loader.loaderHide();
                        }
                        else
                        {
                            loader.loaderHide();
                            Toast.makeText(Quick_Search_verify.this, "No details available please submit details for verification", Toast.LENGTH_SHORT).show();
                            verify.verifyDialogShow();

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });
    }

    private void copy(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied data", text);
        clipboardManager.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void access() {
        if (ContextCompat.checkSelfPermission(Quick_Search_verify.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Quick_Search_verify.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }
    }
}