package com.example.policemitra;

import static com.example.policemitra.SendMail.USER_EMAIL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    General_dialog_modal general = new General_dialog_modal(MainActivity.this);
    loader loader = new loader(MainActivity.this);
    verify_dialog verify = new verify_dialog(MainActivity.this);
    Toolbar toolbar;
//    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView profile_picture;
    TextView textView_data,Uemail,Uname;
    DBHelper DB;
    FirebaseDatabase database;
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    DocumentReference docRef;

    String emailId;

    private String currentPage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_item_one) {

            Log.i("MENU_DRAWER_TAG", "Sos");
//            Intent intent = new Intent(MainActivity.this,profile.class);
//            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader.loaderShow();
        loader.loaderHide();
        DB = new DBHelper(this);
        database = FirebaseDatabase.getInstance();
        loader.loaderShow();
        //profile picture
        NavigationView navigationView = (NavigationView) findViewById(R.id.navidationView);
        View headerView = navigationView.getHeaderView(0);
        Uemail= headerView.findViewById(R.id.emailId);
        Uname = headerView.findViewById(R.id.Uname);
        Cursor res = DB.getData();
        if(res.getCount()>0){
            while (res.moveToNext())
            {
                Uname.setText(String.valueOf(res.getString(0)));
                Uemail.setText(String.valueOf(res.getString(1)));
                emailId = String.valueOf(res.getString(1));
                emailId = emailId.replaceAll("[@.]*", "");
            }
        }

        profile_picture = (ImageView) headerView.findViewById(R.id.profile_pics);

        if(emailId!="" || emailId!=null)
        {
            database.getReference().child(emailId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String image = snapshot.getValue(String.class);
                    Picasso.get().load(image).into(profile_picture);
                    loader.loaderHide();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            profile_picture.setImageResource(R.drawable.img);
        }

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                currentPage = "profile";
                loadFragment(new Profile());
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navidationView);
        toolbar = findViewById(R.id.toolbar);
        textView_data = findViewById(R.id.test);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loadFragment(new home());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        loader.loaderShow();
                        currentPage="";
                        loadFragment(new home());
//                        Log.i("MENU_DRAWER_TAG", "HOME ");
                        loader.loaderHide();
                        break;
//
                    case R.id.verify:
                        loader.loaderShow();
                        currentPage="verify";
                        verify.verifyDialogShow();
//                        loadFragment(new Verification());
//                        Log.i();
                        loader.loaderHide();
                        break;
                    case R.id.logout:
                        loader.loaderShow();
                        FirebaseAuth.getInstance().signOut();
                        if(emailId!=null || emailId !=""){
                            Boolean checkDeleteData = DB.deleteUserData(Uemail.getText().toString());
                            if (checkDeleteData == true)
                            {
                                Intent intentLogin = new Intent(MainActivity.this,login.class);
                                startActivity(intentLogin);
                            }
                        }
                        loader.loaderHide();
                        break;

                    case R.id.complaints:
                        loader.loaderShow();
                        currentPage="complaints";
//                        loadFragment(new complaint());
                        Intent intent = new Intent(MainActivity.this,Complaints.class);
                        startActivity(intent);
                        loader.loaderHide();
                        break;
                    case R.id.gen:
                        loader.loaderShow();
                        currentPage="general";
                        general.generalDialogShow();
//                        loadFragment(new Verification());
//                        Log.i();
                        loader.loaderHide();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (currentPage!="")
        {
            loadFragment(new home());
            currentPage="";
        }
        else
        {
            super.onBackPressed();
        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container,fragment);
        ft.commit();

    }

}