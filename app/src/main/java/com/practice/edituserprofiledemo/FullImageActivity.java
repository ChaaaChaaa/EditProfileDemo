package com.practice.edituserprofiledemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;



import com.bumptech.glide.Glide;

public class FullImageActivity extends Activity {
    private ImageView fullImageView;
    private String IMG_BASE_URL = "http://www.ppizil.kro.kr/review/file/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        init();
    }

    private void init(){
        fullImageView = findViewById(R.id.full_image_view);
        setFullImageView();
    }

    public void setFullImageView(){
        // 공용되게 shared에서 가져오는것 어디서
        // Intent
       Intent getIntent = getIntent();
        String path = getIntent.getStringExtra("originalImage");
        if(path != null){
            Glide.with(this)
                    .load(IMG_BASE_URL+path)
                    .into(fullImageView);
        }
    }
}
