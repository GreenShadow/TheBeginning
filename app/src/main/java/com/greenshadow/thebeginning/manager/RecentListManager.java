package com.greenshadow.thebeginning.manager;

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
        recentList = new LinkedList<>();
        updateMaxCount(maxCount);
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
        cv.put(DBStruct.AllMusic.IS_RECENT, 0);
        mContentResolver.update(DBStruct.AllMusic.CONTENT_URI, cv, null, null);
    }

    public Cursor getRecentCursor(String[] projection) {
        return mContentResolver.query(DBStruct.AllMusic.CONTENT_URI, projection, "is_recent = 1", null, DBStruct.AllMusic._ID);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void updateMaxCount(int maxCount) {
        if (maxCount < this.maxCount) {
            while (recentList.size() > maxCount) {
                removeLast();
            }
            updateRecentDatabase();
        }
        this.maxCount = maxCount;
        Cursor cursor = mContentResolver.query(DBStruct.RecentList.CONTENT_URI, new String[]{DBStruct.RecentList.COUNT},
                null, null, null);
        if (cursor != null && cursor.getCount() == 0) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > maxCount) {
                updateRecentDatabase();
            }
        }
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
        cv.put(DBStruct.AllMusic.IS_RECENT, isRecent ? 1 : 0);
        String selection = DBStruct.AllMusic._ID + " = ?";
        String[] selectionArgs = new String[]{id};
        mContentResolver.update(DBStruct.AllMusic.CONTENT_URI, cv, selection, selectionArgs);
    }

    public void parseRecentListFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return;
        }
        cursor.moveToFirst();
        String list = cursor.getString(0);
        String[] lists = list.trim().split(" ");
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
