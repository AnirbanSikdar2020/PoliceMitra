package com.example.policemitra;

import android.os.Build;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.storage.FirebaseStorage;
        import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CustomAdapterMyPermissionView extends RecyclerView.Adapter<ViewMyPermissionHolder> {
    MyPermission activityList;
    loader loader;
    List<ViewMyPermissionModel> modelList;
    DocumentReference updateData, docRef;
    MyPermission_view_dialog myPermissions_view_dialog;
    FirebaseStorage database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String aadhar, name, subject, fileName, fileExtn, user_email;

    public CustomAdapterMyPermissionView(MyPermission activityList, List<ViewMyPermissionModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewMyPermissionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_complaints_view_model, parent, false);
        ViewMyPermissionHolder viewHolder = new ViewMyPermissionHolder(itemView);
        loader=new loader(activityList);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMyPermissionHolder holder, int position) {
        database = FirebaseStorage.getInstance();
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDetails());
        holder.status.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getId(), holder, position);
    }

    private void addOpt(String fileNo, ViewMyPermissionHolder holder, int position) {
        holder.layoutDiv.setTag(modelList.get(position).getId());
        holder.layoutDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPermissions_view_dialog = new MyPermission_view_dialog(activityList);
                myPermissions_view_dialog.MyPermissionsViewDialogShow(fileNo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


