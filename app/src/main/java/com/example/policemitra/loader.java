package com.example.policemitra;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class loader {

    Activity activity;
    private AlertDialog dialog;

    loader(Activity myactivity)
    {
        activity = myactivity;
    }

    void loaderShow()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loader, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void loaderHide()
    {
        dialog.dismiss();
    }
}
