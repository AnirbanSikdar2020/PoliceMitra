package com.example.policemitra;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageButton actionButton;
    TextView textView_data;
    Bitmap bitmap;
    private static final int REQUEST_CAMERA_CODE=100;
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

        //profile picture
        NavigationView navigationView = (NavigationView) findViewById(R.id.navidationView);
        View headerView = navigationView.getHeaderView(0);
        ImageButton profile_picture = (ImageButton) headerView.findViewById(R.id.profile_pics);
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                loadFragment(new Profile());
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navidationView);
        toolbar = findViewById(R.id.toolbar);
        actionButton = findViewById(R.id.fab);
        textView_data = findViewById(R.id.test);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loadFragment(new home());

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                access();
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        loadFragment(new home());
                        Log.i("MENU_DRAWER_TAG", "HOME ");
                        break;
//
//                    case R.id.map:
//                        loadFragment(new home());
//                        Log.i();
//                        break;

                    case R.id.complaints:
                        Intent intent = new Intent(MainActivity.this,Complaints.class);
                        startActivity(intent);
                        break;
                    case R.id.gen:
                        loadFragment(new editprofile());
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                Uri resultUri = result.getUri();
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    getText(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private void getText(Bitmap bitmap)
    {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational())
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0;i<textBlockSparseArray.size();i++)
            {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
//            textView_data.setText(stringBuilder.toString());
//            button_capture.setText("Retake");
//            button_copy.setVisibility(View.VISIBLE);
        }
    }
    private void copy(String text)
    {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied data",text);
        clipboardManager.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void access()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },REQUEST_CAMERA_CODE);
        }
    }
}