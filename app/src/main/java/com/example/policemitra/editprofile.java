package com.example.policemitra;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editprofile extends Fragment {

    EditText name, phone, aadhar, dob;
    loader loader;
    String gender, SQLemail;
    RadioGroup genderGroup;
    TextInputLayout textInputLayoutPhone, textInputLayoutAadhar, textInputLayoutName, textInputLayoutDob, textInputLayoutGender;
    TextView email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    DBHelper DB;
    RadioButton male, female, others;
    Button update;
    int counter;
    String genderFirebase, sel_date;
    DocumentReference updateData;
    int date, year, month;
    int d, y, mon;
    ImageButton setdate;

    public editprofile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        loader = new loader(this.getActivity());
        loader.loaderShow();
        DB = new DBHelper(this.getActivity());
        Cursor res = DB.getData();

        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        aadhar = view.findViewById(R.id.aadhar);
        dob = view.findViewById(R.id.dob);
        male = view.findViewById(R.id.male_radio_button);
        female = view.findViewById(R.id.female_radio_button);
        others = view.findViewById(R.id.others_radio_button);
        textInputLayoutName = view.findViewById(R.id.textInputName);
        textInputLayoutPhone = view.findViewById(R.id.textInputPhone);
        textInputLayoutAadhar = view.findViewById(R.id.textInputAadhar);
        textInputLayoutGender = view.findViewById(R.id.textInputGender);
        textInputLayoutDob = view.findViewById(R.id.textInputDob);
        genderGroup = view.findViewById(R.id.gender_radio_group);
        update = view.findViewById(R.id.updateBtn);
        setdate = view.findViewById(R.id.calendar);

        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                name.setText(String.valueOf(res.getString(0)));
                email.setText(String.valueOf(res.getString(1)));
                SQLemail = String.valueOf(res.getString(1));
            }
        }

        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
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
        docRef = db.collection("users")
                .document(SQLemail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("Name"));
                        phone.setText(document.getString("Phone"));
                        aadhar.setText(document.getString("Aadhar"));
                        dob.setText(document.getString("Dob"));
                        String[] dates = document.getString("Dob").split("/");
                        date = Integer.valueOf(dates[0]);
                        month = Integer.valueOf(dates[1]) - 1;
                        year = Integer.valueOf(dates[2]);
                        genderFirebase = document.getString("Gender");
                        if (document.getString("Gender").equals("Male")) {
                            male.setChecked(true);
                        } else if (document.getString("Gender").equals("Female")) {
                            female.setChecked(true);
                        } else {
                            others.setChecked(true);
                        }
                        loader.loaderHide();
                    } else {
                        Toast.makeText(getActivity(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText().toString().trim()) || String.valueOf(phone.getText()).trim().length() != 10) {
                    showError(textInputLayoutPhone, "Please enter correct phone number");
                    counter++;
                } else {
                    textInputLayoutPhone.setError(null);
                    counter--;
                }
//                aadhar validation
                if (TextUtils.isEmpty(aadhar.getText().toString().trim()) || String.valueOf(aadhar.getText()).trim().length() != 12) {
                    showError(textInputLayoutAadhar, "Please enter correct aadhar number");
                    counter++;
                } else {
                    textInputLayoutAadhar.setError(null);
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
                //Gender
                if (gender == null) {
                    gender = genderFirebase;
                }

                //DOB
                if (TextUtils.isEmpty(dob.getText().toString().trim())) {
                    showError(textInputLayoutDob, "DOB cannot be blank");
                    counter++;
                } else {
                    textInputLayoutDob.setError(null);
                    counter--;
                }

                if (counter <= 0) {

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("Name", String.valueOf(name.getText()).trim());
                    updates.put("Phone", String.valueOf(phone.getText()).trim());
                    updates.put("Gender", String.valueOf(gender));
                    updates.put("Aadhar", String.valueOf(aadhar.getText()).trim());
                    updates.put("Dob", String.valueOf(dob.getText()).trim());
                    updateData = db.collection("users").document(String.valueOf(email.getText()).trim());
                    updateData.update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Data not updated", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        return view;
    }

    public void setDate(View view) {
        new DatePickerDialog(this.getActivity(), (datePicker, year, month, date) -> {
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