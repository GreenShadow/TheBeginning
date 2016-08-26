package com.greenshadow.thebeginning.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import com.greenshadow.thebeginning.App;
import com.greenshadow.thebeginning.data.DBStruct;
import com.greenshadow.thebeginning.managers.RecentListManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author greenshadow
 */
public class MusicListProvider extends ContentProvider {
    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DBStruct.DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DBStruct.AllMusic.TABLE_NAME +
                    "(" +
                    DBStruct.AllMusic._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBStruct.AllMusic.PLAYLIST_ID + " TEXT, " +
                    DBStruct.AllMusic.IS_RECENT + " INTEGER, " +
                    DBStruct.AllMusic.DISPLAY_NAME + " TEXT, " +
                    DBStruct.AllMusic.ARTIST + " TEXT, " +
                    DBStruct.AllMusic.ARTIST + " TEXT, " +
                    DBStruct.AllMusic.ALBUM + " TEXT, " +
                    DBStruct.AllMusic.RAW_URI + " TEXT, " +
                    DBStruct.AllMusic.FILE_PATH + " TEXT, " +
                    DBStruct.AllMusic.LYRIC + " TEXT" +
                    ");");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + DBStruct.Playlist.TABLE_NAME +
                    "(" +
                    DBStruct.Playlist._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBStruct.Playlist.NAME + " TEXT, " +
                    DBStruct.Playlist.DESCRIPTION + " TEXT, " +
                    DBStruct.Playlist.COVER + " TEXT" +
                    ");");

            ContentValues cv = new ContentValues();
            cv.put(DBStruct.Playlist.NAME, DBStruct.Playlist.STAR);
            cv.put(DBStruct.Playlist.DESCRIPTION, "null");
            cv.put(DBStruct.Playlist.COVER, "null");
            db.insert(DBStruct.Playlist.TABLE_NAME, null, cv);

            db.execSQL("CREATE TABLE IF NOT EXISTS " + DBStruct.RecentList.TABLE_NAME +
                    "(" +
                    DBStruct.RecentList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBStruct.RecentList.DATA + " TEXT, " +
                    DBStruct.RecentList.COUNT + " TEXT" +
                    ");");

            cv.clear();
            cv.put(DBStruct.RecentList.DATA, "");
            cv.put(DBStruct.RecentList.COUNT, 0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + DBStruct.AllMusic.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + DBStruct.Playlist.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + DBStruct.RecentList.TABLE_NAME + ";");
            onCreate(db);
        }
    }

    private DBHelper dbHelper;
    private static final String TAG = "MusicListProvider";

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return dbHelper != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int match = uriMatcher.match(uri);
        switch (match) {
            case MUSIC_ALL:
                qb.setTables(DBStruct.AllMusic.TABLE_NAME);
                break;
            case MUSIC_RECENT:
                qb.setTables(DBStruct.AllMusic.TABLE_NAME);
                qb.appendWhere(DBStruct.AllMusic.IS_RECENT + " = 1");
                break;
            case MUSIC_STAR:
                qb.setTables(DBStruct.AllMusic.TABLE_NAME);
                qb.appendWhere(DBStruct.AllMusic.PLAYLIST_ID + " = " + DBStruct.Playlist.STAR);
                break;
            case MUSIC_CURRENT_LIST:
                qb.setTables(DBStruct.AllMusic.TABLE_NAME);
                qb.appendWhere(DBStruct.AllMusic.PLAYLIST_ID + " like " + uri.getLastPathSegment());
                break;
            case LIST_ALL:
                qb.setTables(DBStruct.Playlist.TABLE_NAME);
                break;
            default:
                Log.e(TAG, "not support uri " + uri.toString());
                return null;
        }

        if (projection != null && projection.length == 1 && projection[0].equals(BaseColumns._COUNT)) {
            qb.setProjectionMap(countProjectionMap);
        }

        Cursor result;
        try {
            result = qb.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        } catch (SQLException e) {
            Log.e(TAG, "returning NULL cursor, query: " + uri, e);
            return null;
        }
        return result;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case MUSIC_ALL:
                result = DBStruct.AllMusic.CONTENT_URI;
                break;
            case LIST_ALL:
                result = DBStruct.Playlist.CONTENT_URI;
                break;
            default:
                Log.e(TAG, "not support uri " + uri.toString());
                return null;
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MUSIC_ALL:
                break;
            case MUSIC_RECENT:
                break;
            case MUSIC_STAR:
                break;
            case MUSIC_CURRENT_LIST:
                break;
            case LIST_ALL:
                break;
            default:
                Log.e(TAG, "not support uri " + uri.toString());
                return -1;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = 0;
        String table;
        int match = uriMatcher.match(uri);
        switch (match) {
            case MUSIC_CURRENT_ID:
                table = DBStruct.AllMusic.TABLE_NAME;
                result = db.update(table, values, selection, selectionArgs);
                break;
            case MUSIC_RECENT:
                String id = (String) values.get("new_id");
                RecentListManager recentListManager = App.getInstance().getRecentListManager();
                recentListManager.addRecent(id);
                table = DBStruct.RecentList.TABLE_NAME;
                values.clear();
                values.put(DBStruct.RecentList.DATA, recentListManager.toString());
                result = db.update(table, values, null, null);
                break;
            case MUSIC_STAR:
                table = DBStruct.AllMusic.TABLE_NAME;
                ContentValues cv = new ContentValues();
                break;
            case MUSIC_CURRENT_LIST:
                table = DBStruct.AllMusic.TABLE_NAME;
                break;
            case LIST_ALL:
                break;
            default:
                Log.e(TAG, "not support uri " + uri.toString());
                return 0;
        }

        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    private static final String AUTHORITY = DBStruct.AUTHORITY;

    private static final int MUSIC_ALL = 0;
    private static final int MUSIC_CURRENT_ID = 1;
    private static final int MUSIC_RECENT = 2;
    private static final int MUSIC_STAR = 3;
    private static final int MUSIC_CURRENT_LIST = 4;
    private static final int LIST_ALL = 5;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, DBStruct.AllMusic.TABLE_NAME, MUSIC_ALL);
        uriMatcher.addURI(AUTHORITY, DBStruct.AllMusic.TABLE_NAME + "/#", MUSIC_CURRENT_ID);
        uriMatcher.addURI(AUTHORITY, DBStruct.RecentList.TABLE_NAME, MUSIC_RECENT);
        uriMatcher.addURI(AUTHORITY, DBStruct.Playlist.TABLE_NAME + "/star", MUSIC_STAR);
        uriMatcher.addURI(AUTHORITY, DBStruct.Playlist.TABLE_NAME + "/*", MUSIC_CURRENT_LIST);
        uriMatcher.addURI(AUTHORITY, DBStruct.Playlist.TABLE_NAME, LIST_ALL);
    }

    private static final Map<String, String> countProjectionMap = new HashMap<>();

    static {
        countProjectionMap.put(BaseColumns._COUNT, "COUNT(*)");
    }
}
