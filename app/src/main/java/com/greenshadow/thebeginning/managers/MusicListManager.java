package com.greenshadow.thebeginning.managers;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore.Audio;

/**
 * @author greenshadow
 */
public class MusicListManager {
    private static MusicListManager mInstance;

    private ContentResolver contentResolver;
    private Uri contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
    private String[] projection = new String[]{
            Audio.Media._ID,
            Audio.Media.DISPLAY_NAME,
            Audio.Media.DATA,
            Audio.Media.ALBUM,
            Audio.Media.ARTIST,
            Audio.Media.DURATION,
            Audio.Media.SIZE,
    };
    private String where = Audio.Media.MIME_TYPE + " IN ('audio/mpeg', 'audio/x-ms-wma') AND "
            + Audio.AudioColumns.IS_MUSIC + " > 0";
    private String sortOrder = Audio.Media.DATA;

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
    
    public void load(){
        
    }
}
