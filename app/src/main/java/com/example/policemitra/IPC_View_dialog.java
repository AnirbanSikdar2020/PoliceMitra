package com.example.policemitra;

import android.app.Activity;
        import android.view.LayoutInflater;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;

        import com.chaos.view.PinView;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.storage.FirebaseStorage;
        import com.squareup.picasso.Picasso;


public class IPC_View_dialog {

    FirebaseStorage storage;
    FirebaseDatabase database;
    loader loader;
    Activity activity;
    private AlertDialog dialog;
    DocumentReference docRef;
    String genderFirebase;
    FirebaseAuth mAuth;
    ImageView profile_img;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView section, details, title;

    IPC_View_dialog(Activity myactivity) {
        activity = myactivity;
    }

    void IPCViewDialogShow(String fileSelector) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.ipc_view_dialog, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        loader = new loader(activity);
        details = dialog.findViewById(R.id.details);
        section = dialog.findViewById(R.id.section);
        title = dialog.findViewById(R.id.title);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        profile_img = dialog.findViewById(R.id.img);
        loader.loaderShow();

        docRef = db.collection("IPC")
                .document(fileSelector);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        section.setText(document.getString("Sections"));
                        title.setText(document.getString("Title"));
                        details.setText(document.getString("Description"));
                        loader.loaderHide();
                    } else {
                        Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


