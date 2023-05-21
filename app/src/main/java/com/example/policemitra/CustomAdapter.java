package com.example.policemitra;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    crime_registration activityList;
    List<Model> modelList;
    DeleteAlert deleteAlert;


    public CustomAdapter(crime_registration activityList, List<Model> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fileNo.setText(modelList.get(position).getFileNo());
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getFileNo(), holder, position);
    }

    private void addOpt(String fileNo, ViewHolder holder, int position) {
        holder.edit.setTag(modelList.get(position).getFileNo());
        holder.delete.setTag(modelList.get(position).getFileNo());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityList, crime_edit_admin.class);
                intent.putExtra("fileNo", (modelList.get(position).getFileNo().toString()));
                activityList.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlert = new DeleteAlert(activityList);
                deleteAlert.deleteAlertShow((modelList.get(position).getFileNo().toString()),crime_registration.class);

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


