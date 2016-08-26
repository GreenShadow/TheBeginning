package com.greenshadow.thebeginning.manager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.view.KeyEvent;

import com.greenshadow.thebeginning.data.DBStruct;
import com.greenshadow.thebeginning.data.MusicBean;

import java.util.ArrayList;

/**
 * @author greenshadow
 */
public class MusicListManager {
    private static MusicListManager mInstance;

    private ContentResolver contentResolver;
    private ArrayList<MusicBean> musicList;

    private MusicListManager(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static MusicListManager getInstance(ContentResolver contentResolver) {
        if (mInstance == null) {
            synchronized (MusicListManager.class) {
                if (mInstance == null) {
                    mInstance = new MusicListManager(contentResolver);
                }
            }
        }
        return mInstance;
    }

    String[] projection = new String[]{
            DBStruct.AllMusic.RAW_MUSIC_ID,
            DBStruct.AllMusic.PLAYLIST_ID,
            DBStruct.AllMusic.IS_RECENT,
            DBStruct.AllMusic.DISPLAY_NAME,
            DBStruct.AllMusic.ARTIST,
            DBStruct.AllMusic.ALBUM,
            DBStruct.AllMusic.FILE_PATH,
            DBStruct.AllMusic.LYRIC,
    };


    public void load() {
        String[] projection = new String[]{
                DBStruct.AllMusic.RAW_MUSIC_ID,
                DBStruct.AllMusic.DISPLAY_NAME,
                DBStruct.AllMusic.ARTIST,
                DBStruct.AllMusic.ALBUM,
                DBStruct.AllMusic.FILE_PATH,
        };
        String sortOrder = DBStruct.AllMusic._ID;
        Cursor cursor = contentResolver.query(DBStruct.AllMusic.CONTENT_URI, projection, null, null, sortOrder);
        if (cursor == null) return;

        initMusicList();
        cursor.moveToFirst();
        int idCol = cursor.getColumnIndex(DBStruct.AllMusic.RAW_MUSIC_ID);
        int displayNameCol = cursor.getColumnIndex(DBStruct.AllMusic.DISPLAY_NAME);
        int albumCol = cursor.getColumnIndex(DBStruct.AllMusic.ALBUM);
        int artistCol = cursor.getColumnIndex(DBStruct.AllMusic.ARTIST);
        int filePathCol = cursor.getColumnIndex(DBStruct.AllMusic.FILE_PATH);
        do {
            int id = cursor.getInt(idCol);
            String name = cursor.getString(displayNameCol);
            String artist = cursor.getString(artistCol);
            String album = cursor.getString(albumCol);
            String path = cursor.getString(filePathCol);

            MusicBean item = new MusicBean();
            item.setId(id);
            item.setName(name);
            item.setArtist(artist);
            item.setAlbum(album);
            item.setFilePath(path);
            musicList.add(item);
        } while (cursor.moveToNext());
        cursor.close();
    }

    private void initMusicList() {
        if (musicList == null) {
            musicList = new ArrayList<>();
        } else {
            musicList.clear();
        }
    }

    public void reload(Context context) {
        final AlertDialog dialog = new ProgressDialog.Builder(context)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_BACK;
                    }
                })
                .setMessage("Loading...")
                .create();
        dialog.show();
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (msg.obj instanceof AlertDialog) {
                            ((AlertDialog) msg.obj).dismiss();
                        }
                }
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
                Uri contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                String[] projection = new String[]{
                        Audio.Media._ID,
                        Audio.Media.DISPLAY_NAME,
                        Audio.Media.DATA,
                        Audio.Media.ALBUM,
                        Audio.Media.ARTIST,
                };
                String where = Audio.Media.MIME_TYPE + " IN ('audio/mpeg', 'audio/x-ms-wma') AND "
                        + Audio.AudioColumns.IS_MUSIC + " > 0";
                String sortOrder = Audio.Media.DATA;

                Cursor cursor = contentResolver.query(contentUri, projection, where, null, sortOrder);
                if (cursor == null) return;

                initMusicList();
                cursor.moveToFirst();
                int idCol = cursor.getColumnIndex(Audio.Media._ID);
                int displayNameCol = cursor.getColumnIndex(Audio.Media.DISPLAY_NAME);
                int albumCol = cursor.getColumnIndex(Audio.Media.ALBUM);
                int artistCol = cursor.getColumnIndex(Audio.Media.ARTIST);
                int filePathCol = cursor.getColumnIndex(Audio.Media.DATA);
                do {
                    int id = cursor.getInt(idCol);
                    String name = cursor.getString(displayNameCol);
                    String artist = cursor.getString(artistCol);
                    String album = cursor.getString(albumCol);
                    String path = cursor.getString(filePathCol);

                    MusicBean item = new MusicBean();
                    item.setId(id);
                    item.setName(name);
                    item.setArtist(artist);
                    item.setAlbum(album);
                    item.setFilePath(path);
                    musicList.add(item);

                    ContentValues cv = new ContentValues();
                    cv.put(DBStruct.AllMusic.RAW_MUSIC_ID, id);
                    cv.put(DBStruct.AllMusic.DISPLAY_NAME, name);
                    cv.put(DBStruct.AllMusic.ARTIST, artist);
                    cv.put(DBStruct.AllMusic.ALBUM, album);
                    cv.put(DBStruct.AllMusic.FILE_PATH, path);
                    contentResolver.insert(DBStruct.AllMusic.CONTENT_URI, cv);
                } while (cursor.moveToNext());
                cursor.close();
                Message.obtain(handler, 0, dialog);
            }
        });
    }
}
