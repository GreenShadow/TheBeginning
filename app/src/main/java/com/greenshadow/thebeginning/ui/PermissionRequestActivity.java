package com.greenshadow.thebeginning.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.greenshadow.thebeginning.R;

/**
 * @author greenshadow
 */

public class PermissionRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                finish();
                return;
            case R.id.next:
                requestPermission();
                break;
        }
    }

    private void requestPermission() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        ActivityCompat.requestPermissions(this, permissions, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i : grantResults) {
                if (i == -1) {
                    return;
                }
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
