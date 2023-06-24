package com.example.policemitra;

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

public class CustomAdapterVerificationsView extends RecyclerView.Adapter<ViewVerificationHolder> {
    admin_verification activityList;
    loader loader;
    List<ViewVerificationModel> modelList;
    DeleteAlert deleteAlert;
    DocumentReference updateData,docRef;
    FirebaseStorage database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user_email,aadhar,name, fileName, fileExtn;


    public CustomAdapterVerificationsView(admin_verification activityList, List<ViewVerificationModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewVerificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.verification_module, parent, false);
        ViewVerificationHolder viewHolder = new ViewVerificationHolder(itemView);
        loader=new loader(activityList);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewVerificationHolder holder, int position) {
        database = FirebaseStorage.getInstance();
        holder.fileNo.setText(modelList.get(position).getFileNo());
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getFileNo(), holder, position);
    }

    private void addOpt(String fileNo, ViewVerificationHolder holder, int position) {
        holder.edit.setTag(modelList.get(position).getFileNo());
        holder.approve.setTag(modelList.get(position).getFileNo());
        holder.download.setTag(modelList.get(position).getId());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                docRef = db.collection("verifications")
                        .document(modelList.get(position).getId().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                fileName = document.getString("File Url");
                                fileExtn = document.getString("File Ext");
//                                user_email = document.getString("User Email");
                                aadhar = document.getString("Aadhar");
                                name = document.getString("Name");
                            }
                        }
                    }
                });
                downloadfile(aadhar, name, fileName);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityList, approve_verification_admin.class);
                intent.putExtra("id", (modelList.get(position).getId().toString()));
                activityList.startActivity(intent);
            }
        });
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activityList, "tata", Toast.LENGTH_SHORT).show();
                docRef = db.collection("verifications")
                        .document(modelList.get(position).getId().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user_email = document.getString("User Email");
                                aadhar = document.getString("Aadhar");
                                name = document.getString("Name");
                            }
                        }
                    }
                });
                Map<String, Object> updates = new HashMap<>();
                updates.put("Status", "Approved");
                updateData = db.collection("verifications").document(modelList.get(position).getId().toString());
                updateData.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(activityList, "Approved", Toast.LENGTH_SHORT).show();
                                Toast.makeText(activityList, "Mail sent to user", Toast.LENGTH_SHORT).show();
                                buttonSendEmail(user_email,name,aadhar);
                                Intent intentLogin = new Intent(activityList, admin_verification.class);
                                activityList.startActivity(intentLogin);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activityList, "Data not updated", Toast.LENGTH_SHORT).show();
                            }
                        });
//                deleteAlert = new DeleteAlert(activityList);
//                deleteAlert.deleteAlertShow((modelList.get(position).getFileNo().toString()),crime_registration.class);

            }
        });
    }

    private void downloadfile(String aadhar, String name, String fileName) {
        String url;
        if (aadhar != null && aadhar != "" && name != null && name != "" && fileName != null && fileName != "") {
            url = aadhar + "-" + name + "-" + fileName + "-verify";
            Toast.makeText(activityList, url, Toast.LENGTH_SHORT).show();
            database.getReference().child(url).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri fileUri) {
                            String destinationDirectory = Environment.DIRECTORY_DOWNLOADS;
                            DownloadManager downloadManager = (DownloadManager) activityList.getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(fileUri);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(destinationDirectory, fileName+"."+fileExtn);
                            downloadManager.enqueue(request);
                            loader.loaderHide();
                        }
                    });
        } else {
            loader.loaderHide();
            Toast.makeText(activityList, "No file available", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonSendEmail(String email,String name,String aadhar) {
        try {

            String stringSenderEmail = EMAIL;
            String stringReceiverEmail = email;
            String stringPasswordSenderEmail = PASSWORD;

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Verification Status Update for Aadhar No."+aadhar);
            mimeMessage.setText("Hello Sir/Madam, \n\nAs per you have requested for verification of \nUser Name : "+name+"\nAadhar Number : "+aadhar+"\nAs per current updated records with us the user is not involved in any criminal activities in past.\n\nRegards,\nPoliceMitra Team");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}


