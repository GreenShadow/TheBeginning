package com.greenshadow.thebeginning.manager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.greenshadow.thebeginning.data.DBStruct;

/**
 * @author greenshadow
 */
public class PlaylistManager {
    private ContentResolver mContentResolver;

    public PlaylistManager(ContentResolver mContentResolver) {
        this.mContentResolver = mContentResolver;
    }

    public Cursor getStarList() {
        return mContentResolver.query(DBStruct.Playlist.STARS_URI,
                DBStruct.AllMusic.MUSIC_DISPLAY_LIST_PROJECTION, null, null, DBStruct.AllMusic._ID);
    }

    public Cursor getPlaylist(String playlistName) {
        return mContentResolver.query(Uri.withAppendedPath(DBStruct.AllMusic.CONTENT_URI, playlistName),
                DBStruct.AllMusic.MUSIC_DISPLAY_LIST_PROJECTION, null, null, DBStruct.AllMusic._ID);
    }

    public Cursor getAllPlaylist() {
        return mContentResolver.query(DBStruct.Playlist.CONTENT_URI, null, null, null, DBStruct.Playlist._ID);
    }
}
