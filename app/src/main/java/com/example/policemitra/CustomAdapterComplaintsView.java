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
public class CustomAdapterComplaintsView extends RecyclerView.Adapter<ViewComplaintsHolder> {
    admin_complaints activityList;
    loader loader;
    List<ViewComplaintsModel> modelList;
    DocumentReference updateData, docRef;
    Complaints_view_dialog complaints_view_dialog;
    FirebaseStorage database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String aadhar, name, subject, fileName, fileExtn, user_email;

    public CustomAdapterComplaintsView(admin_complaints activityList, List<ViewComplaintsModel> modelList) {
        this.activityList = activityList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewComplaintsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaints_model_view, parent, false);
        ViewComplaintsHolder viewHolder = new ViewComplaintsHolder(itemView);
        loader=new loader(activityList);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewComplaintsHolder holder, int position) {
        database = FirebaseStorage.getInstance();
        holder.rTitletv.setText(modelList.get(position).getTile());
        holder.rDesctv.setText(modelList.get(position).getDetails());
        holder.status.setText(modelList.get(position).getDescription());
        addOpt(modelList.get(position).getId(), holder, position);
    }

    private void addOpt(String fileNo, ViewComplaintsHolder holder, int position) {
        holder.decline.setTag(modelList.get(position).getId());
        holder.download.setTag(modelList.get(position).getId());
        holder.approve.setTag(modelList.get(position).getId());
        holder.layoutDiv.setTag(modelList.get(position).getId());
        holder.layoutDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaints_view_dialog = new Complaints_view_dialog(activityList);
//                Toast.makeText(activityList, "test"+fileNo, Toast.LENGTH_SHORT).show();
                complaints_view_dialog.ComplaintsViewDialogShow(fileNo);
//                Toast.makeText(activityList, "Test", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(activityList, approve_verification_admin.class);
//                intent.putExtra("id", (modelList.get(position).getId().toString()));
//                activityList.startActivity(intent);
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                docRef = db.collection("complaints")
                        .document(modelList.get(position).getId());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user_email = document.getString("User Email");
                                subject = document.getString("Subject");
                                aadhar = document.getString("Aadhar");
                                name = document.getString("Name");
                            }
                        }
                    }
                });
                Map<String, Object> updates = new HashMap<>();
                updates.put("Status", "Declined");
                updateData = db.collection("complaints").document(modelList.get(position).getId().toString());
                updateData.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(activityList, "Declined", Toast.LENGTH_SHORT).show();
                                Toast.makeText(activityList, "Mail sent to user", Toast.LENGTH_SHORT).show();
                                buttonSendEmail("declined", user_email, name, aadhar, subject);
                                Intent intentLogin = new Intent(activityList, admin_complaints.class);
                                activityList.startActivity(intentLogin);
                                loader.loaderHide();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loader.loaderHide();
                                Toast.makeText(activityList, "Data not updated", Toast.LENGTH_SHORT).show();
                            }
                        });
//                deleteAlert = new DeleteAlert(activityList);
//                deleteAlert.deleteAlertShow((modelList.get(position).getFileNo().toString()),crime_registration.class);

            }
        });
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                docRef = db.collection("complaints")
                        .document(modelList.get(position).getId());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user_email = document.getString("User Email");
                                subject = document.getString("Subject");
                                aadhar = document.getString("Aadhar");
                                name = document.getString("Name");
                            }
                        }
                    }
                });
                Map<String, Object> updates = new HashMap<>();
                updates.put("Status", "Accepted");
                updateData = db.collection("complaints").document(modelList.get(position).getId().toString());
                updateData.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(activityList, "Approved", Toast.LENGTH_SHORT).show();
                                Toast.makeText(activityList, "Mail sent to user", Toast.LENGTH_SHORT).show();
                                buttonSendEmail("accept", user_email, name, aadhar, subject);
                                Intent intentLogin = new Intent(activityList, admin_complaints.class);
                                activityList.startActivity(intentLogin);
                                loader.loaderHide();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loader.loaderHide();
                                Toast.makeText(activityList, "Data not updated", Toast.LENGTH_SHORT).show();
                            }
                        });
//                deleteAlert = new DeleteAlert(activityList);
//                deleteAlert.deleteAlertShow((modelList.get(position).getFileNo().toString()),crime_registration.class);

            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                    docRef = db.collection("complaints")
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
    }

    public void buttonSendEmail(String flag, String email, String name, String aadhar, String subject) {
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
            if (flag == "accept") {
                mimeMessage.setSubject("Complaint Status Update for Aadhar No." + aadhar);
                mimeMessage.setText("Hello " + name + ", \n\nAs per you have registered a complaint of \nSubject : " + subject + "\nWe have accepted your complaint and rapid action would be taken for the same. For more details we will call you within 48hours on your registered mobile number. \n\nRegards,\nPoliceMitra Team");
            } else {
                mimeMessage.setSubject("Complaint Status Update for Aadhar No." + aadhar);
                mimeMessage.setText("Hello " + name + ", \n\nAs per you have registered a complaint of \nSubject : " + subject + "\nWe are sorry to let you know that we would not be able to take your complaint online. We would request you to kindly visit your nearest police station to raise the complaint.\n\nRegards,\nPoliceMitra Team");
            }
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

    private void downloadfile(String aadhar, String name, String fileName) {
        String url;
        if (aadhar != null && aadhar != "" && name != null && name != "" && fileName != null && fileName != "") {
            url = aadhar + "-" + name + "-" + fileName + "-complaints";
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

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
