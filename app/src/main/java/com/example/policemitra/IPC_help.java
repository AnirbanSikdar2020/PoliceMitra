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

public class IPC_help extends AppCompatActivity {

    ImageView back;
    ImageButton IPCSearch;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Query query;
    List<ViewIPCModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapterIPCView adapter;
    String fileNum;
    EditText IPC;
    verify_dialog verify = new verify_dialog(IPC_help.this);
    loader loader = new loader(IPC_help.this);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc_help);
        mRecyclerView = findViewById(R.id.viewCriminal);
        db = FirebaseFirestore.getInstance();
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        back = findViewById(R.id.back);
        IPCSearch = findViewById(R.id.IPCSearch);
        IPC = findViewById(R.id.IPC);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(IPC_help.this, MainActivity.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

        IPCSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IP = IPC.getText().toString().trim();
                if(IPC.getText().toString().length()>0)
                {
                    searching(IP);
                }
                else
                {
                    Toast.makeText(IPC_help.this, "Enter text to search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searching(String IPC) {
        loader.loaderShow();
        modelList.clear();
        query = db.collection("IPC")
                .whereEqualTo("Keyword", IPC);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size()>0)
                        {
                            for (DocumentSnapshot doc : task.getResult()) {
                                fileNum = doc.getString("File Number");
                                ViewIPCModel model = new ViewIPCModel(doc.getString("Sections"),
                                        doc.getString("Title"),
                                        doc.getString("Description"));
//
                                modelList.add(model);
                            }
                            adapter = new CustomAdapterIPCView(IPC_help.this, modelList);
                            mRecyclerView.setAdapter(adapter);
                            loader.loaderHide();
                        }
                        else
                        {
                            loader.loaderHide();
                            Toast.makeText(IPC_help.this, "No details available please submit details for verification", Toast.LENGTH_SHORT).show();
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
}