package com.greenshadow.thebeginning;

import android.app.Application;
import android.content.SharedPreferences;

import com.greenshadow.thebeginning.data.DBStruct;
import com.greenshadow.thebeginning.manager.MusicListManager;
import com.greenshadow.thebeginning.manager.PlaylistManager;
import com.greenshadow.thebeginning.manager.RecentListManager;

/**
 * @author greenshadow
 */
public class App extends Application {
    private MusicListManager musicListManager;
    private RecentListManager recentListManager;
    private PlaylistManager playlistManager;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("music_settings", MODE_PRIVATE);
        musicListManager = new MusicListManager(getContentResolver());

        recentListManager = new RecentListManager(sp.getInt("recent_max_count", 10), getContentResolver());
        recentListManager.parseRecentListFromCursor(getContentResolver()
                .query(DBStruct.RecentList.CONTENT_URI, new String[]{DBStruct.RecentList.DATA},
                        null, null, null));
        playlistManager = new PlaylistManager(getContentResolver());
    }

    public MusicListManager getMusicListManager() {
        return musicListManager;
    }

    public RecentListManager getRecentListManager() {
        return recentListManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sp;
    }

    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }
}
