package com.example.policemitra;

import static com.example.policemitra.SendMail.EMAIL;
        import static com.example.policemitra.SendMail.PASSWORD;
        import android.Manifest;
        import android.app.DownloadManager;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
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
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.storage.FirebaseStorage;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.Properties;
        import javax.mail.Authenticator;
        import javax.mail.Message;
        import javax.mail.MessagingException;
        import javax.mail.PasswordAuthentication;
        import javax.mail.Session;
        import javax.mail.Transport;
        import javax.mail.internet.AddressException;
        import javax.mail.internet.InternetAddress;
        import javax.mail.internet.MimeMessage;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CustomAdapterMyComplaintsView extends RecyclerView.Adapter<ViewMyComplaintsHolder> {
    MyComplaints activityList;
    loader loader;
    List<ViewMyComplaintsModel> modelList;
    DocumentReference updateData, docRef;
    MyComplaints_view_dialog myComplaints_view_dialog;
    FirebaseStorage database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String aadhar, name, subject, fileName, fileExtn, user_email;

    public CustomAdapterMyComplaintsView(MyComplaints activityList, List<ViewMyComplaintsModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewMyComplaintsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_complaints_view_model, parent, false);
        ViewMyComplaintsHolder viewHolder = new ViewMyComplaintsHolder(itemView);
        loader=new loader(activityList);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMyComplaintsHolder holder, int position) {
        database = FirebaseStorage.getInstance();
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDetails());
        holder.status.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getId(), holder, position);
    }

    private void addOpt(String fileNo, ViewMyComplaintsHolder holder, int position) {
        holder.layoutDiv.setTag(modelList.get(position).getId());
        holder.layoutDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myComplaints_view_dialog = new MyComplaints_view_dialog(activityList);
                myComplaints_view_dialog.MyComplaintsViewDialogShow(fileNo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}

