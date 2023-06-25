package com.example.policemitra;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.policemitra.SendMail.EMAIL;
        import static com.example.policemitra.SendMail.PASSWORD;

        import android.app.DownloadManager;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Environment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
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

public class CustomAdapterSOSView extends RecyclerView.Adapter<ViewSOSHolder> {
    SOS activityList;
    loader loader;
    List<ViewSOSModel> modelList;
    DeleteAlert deleteAlert;
    DocumentReference updateData,docRef;
    FirebaseStorage database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user_email,aadhar,name, fileName, fileExtn;


    public CustomAdapterSOSView(SOS activityList, List<ViewSOSModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewSOSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sos_module, parent, false);
        ViewSOSHolder viewHolder = new ViewSOSHolder(itemView);
        loader=new loader(activityList);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSOSHolder holder, int position) {
        database = FirebaseStorage.getInstance();
        holder.fileNo.setText(modelList.get(position).getFileNo());
        holder.rTitletv.setText(modelList.get(position).getId());

        addOpt(modelList.get(position).getFileNo(), holder, position);
    }

    private void addOpt(String fileNo, ViewSOSHolder holder, int position) {
        holder.download.setTag(modelList.get(position).getId());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = modelList.get(position).getId();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                activityList.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


