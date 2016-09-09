package com.greenshadow.thebeginning.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.greenshadow.thebeginning.R;
import com.greenshadow.thebeginning.data.DBStruct;
import com.greenshadow.thebeginning.util.PermissionCheckUtil;

/**
 * @author greenshadow
 */
public class ScanMusicActivity extends BaseActivity implements View.OnClickListener {
    private boolean isScanning;
    private Button btnScanMusic;
    private ProgressBar progressBar;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isRunning(false);
            setResult(RESULT_OK);
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacn_music);
        btnScanMusic = (Button) findViewById(R.id.scan_music);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        btnScanMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_music:
                if (!PermissionCheckUtil.checkStoragePermission(this)) {
                    Intent i = new Intent(this, PermissionRequestActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    return;
                }
                scanMusic();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isScanning) {
            Toast.makeText(this, R.string.scanning, Toast.LENGTH_LONG).show();
            return;
        }
        super.onBackPressed();
    }

    public void scanMusic() {
        isRunning(true);
        handler.post(new Runnable() {
            @Override
            public void run() {
                /*drop music table first*/
                getContentResolver().delete(DBStruct.AllMusic.RELOAD_URI, null, null);

                Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] projection = new String[]{
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                };
                String where = MediaStore.Audio.Media.MIME_TYPE + " IN ('audio/mpeg', 'audio/x-ms-wma') AND "
                        + MediaStore.Audio.AudioColumns.IS_MUSIC + " > 0";
                String sortOrder = MediaStore.Audio.Media.DATA;

                Cursor cursor = getContentResolver().query(contentUri, projection, where, null, sortOrder);
                if (cursor == null || cursor.getCount() == 0) {
                    Message.obtain(handler, 0).sendToTarget();
                    return;
                }

                int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int displayNameCol = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int albumCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int artistCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int filePathCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(idCol);
                    String name = cursor.getString(displayNameCol);
                    String artist = cursor.getString(artistCol);
                    String album = cursor.getString(albumCol);
                    String path = cursor.getString(filePathCol);

                    ContentValues cv = new ContentValues();
                    cv.put(DBStruct.AllMusic.RAW_MUSIC_ID, id);
                    cv.put(DBStruct.AllMusic.DISPLAY_NAME, name);
                    cv.put(DBStruct.AllMusic.ARTIST, artist);
                    cv.put(DBStruct.AllMusic.ALBUM, album);
                    cv.put(DBStruct.AllMusic.FILE_PATH, path);

                    getContentResolver().insert(DBStruct.AllMusic.CONTENT_URI, cv);
                }
                cursor.close();
                Message.obtain(handler, 0).sendToTarget();
            }
        });
    }

    private void isRunning(boolean isRunning) {
        this.isScanning = isRunning;
        btnScanMusic.setEnabled(!isRunning);
        progressBar.setVisibility(isRunning ? View.VISIBLE : View.GONE);
    }
}
