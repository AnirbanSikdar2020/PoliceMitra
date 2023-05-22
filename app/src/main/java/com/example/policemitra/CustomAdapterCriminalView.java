
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

public class CustomAdapterCriminalView extends RecyclerView.Adapter<ViewCriminalHolder> {
    Quick_Search_verify activityList;
    List<ViewCriminalModel> modelList;

    Criminal_View_dialog criminal_view_dialog;


    public CustomAdapterCriminalView(Quick_Search_verify activityList, List<ViewCriminalModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewCriminalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_criminal_module, parent, false);
        ViewCriminalHolder viewHolder = new ViewCriminalHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCriminalHolder holder, int position) {
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getFileNo(), holder, position);
    }

    private void addOpt(String fileNo, ViewCriminalHolder holder, int position) {

        holder.details.setTag(modelList.get(position).getFileNo());
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criminal_view_dialog = new Criminal_View_dialog(activityList);
//                Toast.makeText(activityList, "test"+fileNo, Toast.LENGTH_SHORT).show();
                criminal_view_dialog.CriminalViewDialogShow(fileNo);
//                Intent intent = new Intent(activityList, crime_edit_admin.class);
//                intent.putExtra("fileNo", (modelList.get(position).getFileNo().toString()));
//                activityList.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


