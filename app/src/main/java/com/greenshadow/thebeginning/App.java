package com.greenshadow.thebeginning;

import android.app.Application;
import android.content.SharedPreferences;

import com.greenshadow.thebeginning.data.DBStruct;
import com.greenshadow.thebeginning.managers.RecentListManager;

/**
 * @author greenshadow
 */
public class App extends Application {
    private static App mInstance;

    private RecentListManager recentListManager;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        sp = getSharedPreferences("music_settings", MODE_PRIVATE);
        recentListManager = new RecentListManager(sp.getInt("recent_max_count", 10),
                getApplicationContext().getContentResolver());
        recentListManager.parseRecentListFromCursor(getContentResolver()
                .query(DBStruct.RecentList.CONTENT_URI, new String[]{DBStruct.RecentList.DATA},
                        null, null, null));
    }

    public static App getInstance() {
        return mInstance;
    }

    public RecentListManager getRecentListManager() {
        return recentListManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sp;
    }
}
