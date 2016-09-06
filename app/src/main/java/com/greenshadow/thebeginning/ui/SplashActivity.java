package com.greenshadow.thebeginning.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.greenshadow.thebeginning.R;
import com.greenshadow.thebeginning.util.PermissionCheckUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (PermissionCheckUtil.checkStoragePermission(SplashActivity.this)) {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    i = new Intent(SplashActivity.this, PermissionRequestActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(i);
                finish();
            }
        }, 500);
    }
}
