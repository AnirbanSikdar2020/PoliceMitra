package com.example.policemitra;

import android.app.Activity;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

public class MyComplaintsModal {
    loader loader;
    Activity activity;
    private AlertDialog dialog;
    CardView comp,permissions,gd,verify;
//    Button fir;

    MyComplaintsModal(Activity myactivity) {
        activity = myactivity;
    }

    void MyComplaintsDialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.my_complaints_modal, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        loader = new loader(activity);
        comp = dialog.findViewById(R.id.complaints);
        permissions = dialog.findViewById(R.id.permissions);
        gd = dialog.findViewById(R.id.gd);
        verify = dialog.findViewById(R.id.verify);

        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                Intent intentLogin = new Intent(activity, MyComplaints.class);
                activity.startActivity(intentLogin);
            }
        });

        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                Intent intentLogin = new Intent(activity, MyPermission.class);
                activity.startActivity(intentLogin);
            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                Intent intentLogin = new Intent(activity, MyVerifications.class);
                activity.startActivity(intentLogin);
            }
        });
    }
}



