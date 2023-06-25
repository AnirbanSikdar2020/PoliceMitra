package com.example.policemitra;

import androidx.appcompat.app.AlertDialog;
        import android.app.Activity;
        import android.text.TextUtils;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.storage.FirebaseStorage;
        import java.util.HashMap;

public class IPC_Register {

    Activity activity;
    private AlertDialog dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    FirebaseDatabase database;
    EditText email;
    Button confirm;
    FirebaseAuth auth;
    EditText section,title,Descriptions,Keyword;
    TextInputLayout textInputLayoutSection,textInputLayoutTitle,textInputLayoutKeyword,textInputLayoutDescriptions;
    int counter = 0;
    DocumentReference updateData, docRef;
    String user_email,subject,aadhar,name;

    IPC_Register(Activity myactivity) {
        activity = myactivity;
    }

    void IPC_RegisterShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.ipc_register, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        section = dialog.findViewById(R.id.section);
        title = dialog.findViewById(R.id.title);
        Descriptions = dialog.findViewById(R.id.Descriptions);
        Keyword = dialog.findViewById(R.id.Keyword);
        confirm = dialog.findViewById(R.id.confirm);
        textInputLayoutSection = dialog.findViewById(R.id.textInputSection);
        textInputLayoutTitle = dialog.findViewById(R.id.textInputTitle);
        textInputLayoutKeyword = dialog.findViewById(R.id.textInputKeyword);
        textInputLayoutDescriptions = dialog.findViewById(R.id.textInputDescriptions);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //section
                if (TextUtils.isEmpty(section.getText().toString().trim())) {
                    showError(textInputLayoutSection, "Please enter Section");
                    counter = 1;
                } else {
                    textInputLayoutSection.setError(null);
                    counter = 0;
                }
                //title
                if (TextUtils.isEmpty(title.getText().toString().trim())) {
                    showError(textInputLayoutTitle, "Please enter Title");
                    counter = 1;
                } else {
                    textInputLayoutTitle.setError(null);
                    counter = 0;
                }
                //Descriptions
                if (TextUtils.isEmpty(Descriptions.getText().toString().trim())) {
                    showError(textInputLayoutDescriptions, "Please enter Descriptions");
                    counter = 1;
                } else {
                    textInputLayoutDescriptions.setError(null);
                    counter = 0;
                }
                //Keyword
                if (TextUtils.isEmpty(Keyword.getText().toString().trim())) {
                    showError(textInputLayoutKeyword, "Please enter Keyword");
                    counter = 1;
                } else {
                    textInputLayoutKeyword.setError(null);
                    counter = 0;
                }
                if (counter == 0) {
                    HashMap<String, Object> sosDetails = new HashMap<>();
                    sosDetails.put("Sections", section.getText().toString());
                    sosDetails.put("Title", title.getText().toString());
                    sosDetails.put("Description", Descriptions.getText().toString());
                    sosDetails.put("Keyword", Keyword.getText().toString());

                    //upload data
                    db.collection("IPC")
                            .document(section.getText().toString())
                            .set(sosDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show();
                                    dialog.hide();
                                }
                            });
                }
            }
        });
    }
    public void showError(TextInputLayout tag, String message) {
        tag.setError(message);
    }
}
