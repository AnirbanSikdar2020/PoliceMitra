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
public class CustomAdapterMyVerificationsView extends RecyclerView.Adapter<ViewMyVerificationsHolder> {
    MyVerifications activityList;
    loader loader;
    List<ViewMyVerificationsModel> modelList;
    DocumentReference updateData, docRef;
    MyVerifications_view_dialog MyVerificationss_view_dialog;
    FirebaseStorage database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String aadhar, name, subject, fileName, fileExtn, user_email;

    public CustomAdapterMyVerificationsView(MyVerifications activityList, List<ViewMyVerificationsModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewMyVerificationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_complaints_view_model, parent, false);
        ViewMyVerificationsHolder viewHolder = new ViewMyVerificationsHolder(itemView);
        loader=new loader(activityList);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMyVerificationsHolder holder, int position) {
        database = FirebaseStorage.getInstance();
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDetails());
        holder.status.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getId(), holder, position);
    }

    private void addOpt(String fileNo, ViewMyVerificationsHolder holder, int position) {
        holder.layoutDiv.setTag(modelList.get(position).getId());
        holder.layoutDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aadhar=modelList.get(position).getEmail()+"-"+modelList.get(position).getId();
                MyVerificationss_view_dialog = new MyVerifications_view_dialog(activityList);
                MyVerificationss_view_dialog.MyVerificationsViewDialogShow(aadhar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


