package com.greenshadow.thebeginning.managers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.greenshadow.thebeginning.data.DBStruct;

import java.util.LinkedList;

/**
 * @author greenshadow
 */
public class RecentListManager {
    private ContentResolver mContentResolver;
    private int maxCount;
    private LinkedList<String> recentList;

    public RecentListManager(int maxCount, ContentResolver contentResolver) {
        mContentResolver = contentResolver;
        this.maxCount = maxCount;
        recentList = new LinkedList<>();
    }

    public void addRecent(String id) {
        if (recentList.size() == maxCount) {
            removeLast();
        }
        addFirst(id);
        updateRecentDatabase();
    }

    public void clearRecent() {
        recentList.clear();
        ContentValues cv = new ContentValues();
        cv.put(DBStruct.AllMusic.IS_RECENT, false);
        mContentResolver.update(DBStruct.AllMusic.CONTENT_URI, cv, null, null);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        if (maxCount < this.maxCount) {
            while (recentList.size() > maxCount) {
                removeLast();
            }
            updateRecentDatabase();
        }
        this.maxCount = maxCount;
    }

    private void addFirst(String id) {
        recentList.addFirst(id);
        updateMusicDatabase(id, true);
    }

    private void removeLast() {
        updateMusicDatabase(recentList.removeLast(), false);
    }

    private void updateRecentDatabase() {
        ContentValues cv = new ContentValues();
        cv.put(DBStruct.RecentList.DATA, toString());
        mContentResolver.update(DBStruct.RecentList.CONTENT_URI, cv, null, null);
    }

    private void updateMusicDatabase(String id, boolean isRecent) {
        ContentValues cv = new ContentValues();
        cv.put(DBStruct.AllMusic.IS_RECENT, isRecent);
        String selection = DBStruct.AllMusic._ID + " = ?";
        String[] selectionArgs = new String[]{id};
        mContentResolver.update(DBStruct.AllMusic.CONTENT_URI, cv, selection, selectionArgs);
    }

    public void parseRecentListFromCursor(Cursor cursor) {
        String list = cursor.getString(0);
        String[] lists = list.split(" ");
        for (String item : lists) {
            if (!TextUtils.isEmpty(item)) {
                recentList.addLast(item);
            }
            if (recentList.size() == maxCount) {
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String item : recentList) {
            builder.append(item);
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}
