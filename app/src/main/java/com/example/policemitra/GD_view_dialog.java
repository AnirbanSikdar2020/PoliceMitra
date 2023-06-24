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

        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;

        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.storage.FirebaseStorage;

public class GD_view_dialog {

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
    TextView name, email, status, subject, details, doc, location, ps;

    GD_view_dialog(Activity myactivity) {
        activity = myactivity;
    }

    void GDViewDialogShow(String fileSelector) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.admin_view_permission_details, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        loader = new loader(activity);

        name = dialog.findViewById(R.id.name);
        email = dialog.findViewById(R.id.email);
        status = dialog.findViewById(R.id.status);
        subject = dialog.findViewById(R.id.subject);
        details = dialog.findViewById(R.id.details);
        doc = dialog.findViewById(R.id.date);
        location = dialog.findViewById(R.id.location);
        ps = dialog.findViewById(R.id.ps);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        profile_img = dialog.findViewById(R.id.img);
        loader.loaderShow();

        docRef = db.collection("GD")
                .document(fileSelector);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        database.getReference().child(fileSelector).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                String image = snapshot.getValue(String.class);
//                                Picasso.get().load(image).into(profile_img);
//                                loader.loaderHide();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });

                        name.setText(document.getString("Name"));
                        email.setText(document.getString("User Email"));
                        status.setText(document.getString("Status"));
                        doc.setText(document.getString("Doe"));
                        subject.setText(document.getString("Subject"));
                        location.setText(document.getString("Location"));
                        details.setText(document.getString("Details"));
                        ps.setText(document.getString("Police Station"));

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


