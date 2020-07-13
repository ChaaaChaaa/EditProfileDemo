package com.practice.edituserprofiledemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    private Button button;
    private ProfileFragment profileFragment;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        //PackageManager.PERMISSION_GRANTED;


        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            profileFragment.onResult(requestCode, resultCode, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        button.setVisibility(View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();

        profileFragment = new ProfileFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, profileFragment)
                .commit();


//        Intent intent = new Intent(this,ProfileFragment.class);
//        startActivity(intent);
//        Bundle bundle = new Bundle();
//        profileFragment.getFragmentManager().beginTransaction()
//                .replace(R.id.main_content,profileFragment)
//                .commit();
//
//        FragmentManager fragmentManager = getCallingActivity()


    }
}
