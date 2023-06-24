package com.example.policemitra;

import android.app.Activity;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import androidx.appcompat.app.AlertDialog;

public class MyComplaintsModal {
    loader loader;
    Activity activity;
    private AlertDialog dialog;
//    Button permissions,gd,fir;

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
//        permissions = dialog.findViewById(R.id.permissions);
//        gd = dialog.findViewById(R.id.gd);

//        permissions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loader.loaderShow();
//                Intent intentLogin = new Intent(activity, Permission_submit.class);
//                activity.startActivity(intentLogin);
//            }
//        });

//        gd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loader.loaderShow();
//                Intent intentLogin = new Intent(activity, GD_submit.class);
//                activity.startActivity(intentLogin);
//            }
//        });
    }
}



