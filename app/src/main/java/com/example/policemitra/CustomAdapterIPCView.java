package com.example.policemitra;

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

public class CustomAdapterIPCView extends RecyclerView.Adapter<ViewIPCHolder> {
    IPC_help activityList;
    List<ViewIPCModel> modelList;

    IPC_View_dialog ipc_view_dialog;


    public CustomAdapterIPCView(IPC_help activityList, List<ViewIPCModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewIPCHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ipc_module, parent, false);
        ViewIPCHolder viewHolder = new ViewIPCHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewIPCHolder holder, int position) {
        holder.sectionTV.setText(modelList.get(position).getId());
        holder.titleTV.setText(modelList.get(position).getTile());
        addOpt(modelList.get(position).getId(), holder, position);
    }

    private void addOpt(String fileNo, ViewIPCHolder holder, int position) {

        holder.details.setTag(modelList.get(position).getId());
        holder.details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ipc_view_dialog = new IPC_View_dialog(activityList);
                ipc_view_dialog.IPCViewDialogShow(fileNo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


