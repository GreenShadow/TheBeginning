package com.greenshadow.thebeginning.manager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.greenshadow.thebeginning.data.DBStruct;

/**
 * @author greenshadow
 */
public class MusicListManager {

    private ContentResolver mContentResolver;

    public MusicListManager(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    public Cursor getAllMusic() {
        String[] projection = new String[]{
                DBStruct.AllMusic._ID,
                DBStruct.AllMusic.RAW_MUSIC_ID,
                DBStruct.AllMusic.DISPLAY_NAME,
                DBStruct.AllMusic.ARTIST,
                DBStruct.AllMusic.ALBUM,
                DBStruct.AllMusic.FILE_PATH,
        };
        String sortOrder = DBStruct.AllMusic._ID;
        return mContentResolver.query(DBStruct.AllMusic.CONTENT_URI, projection, null, null, sortOrder);
    }
}
