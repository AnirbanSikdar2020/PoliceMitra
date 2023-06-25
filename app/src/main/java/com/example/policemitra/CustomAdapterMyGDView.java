package com.example.policemitra;

import android.os.Build;
        import android.os.Environment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.loader.content.Loader;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.storage.FirebaseStorage;
        import java.util.HashMap;
        import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CustomAdapterMyGDView extends RecyclerView.Adapter<ViewMyGDHolder> {
    MyGD activityList;
    loader loader;
    List<ViewMyGDModel> modelList;
    DocumentReference updateData, docRef;
    MyGD_view_dialog myGD_view_dialog;
    FirebaseStorage database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String aadhar, name, subject, fileName, fileExtn, user_email;

    public CustomAdapterMyGDView(MyGD activityList, List<ViewMyGDModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewMyGDHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_complaints_view_model, parent, false);
        ViewMyGDHolder viewHolder = new ViewMyGDHolder(itemView);
        loader=new loader(activityList);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMyGDHolder holder, int position) {
        database = FirebaseStorage.getInstance();
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDetails());
        holder.status.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getId(), holder, position);
    }

    private void addOpt(String fileNo, ViewMyGDHolder holder, int position) {
        holder.layoutDiv.setTag(modelList.get(position).getId());
        holder.layoutDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGD_view_dialog = new MyGD_view_dialog(activityList);
                myGD_view_dialog.MyGDViewDialogShow(fileNo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


