package com.greenshadow.thebeginning.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author greenshadow
 */
public final class DBStruct {
    public static final String AUTHORITY = "com.greenshadow.thebeginning.MusicProvider";

    private static final Uri BASE_URI = new Uri.Builder()
            .scheme("content")
            .encodedAuthority(AUTHORITY)
            .build();

    public static final String DB_NAME = "beginning_music";

    public static final class AllMusic implements BaseColumns {
        public static final String TABLE_NAME = "all_music";
        public static final Uri CONTENT_URI = appendUri(TABLE_NAME);
        public static final Uri RELOAD_URI = Uri.withAppendedPath(CONTENT_URI, "drop");

        public static final String RAW_MUSIC_ID = "raw_music_id";
        public static final String PLAYLIST_ID = "playlist_id";
        public static final String IS_RECENT = "is_recent";
        public static final String DISPLAY_NAME = "display_name";
        public static final String ARTIST = "artist";
        public static final String ALBUM = "album";
        public static final String FILE_PATH = "file_path";
        public static final String LYRIC = "lyric";

        public static final String[] MUSIC_DISPLAY_LIST_PROJECTION = new String[]{
                _ID,
                PLAYLIST_ID,
                DISPLAY_NAME,
                ARTIST,
                ALBUM,
        };
    }

    public static final class Playlist implements BaseColumns {
        public static final String TABLE_NAME = "playlist";
        public static final Uri CONTENT_URI = appendUri(TABLE_NAME);

        public static final Uri STARS_URI = Uri.withAppendedPath(CONTENT_URI, "star");
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String COVER = "cover";

        public static final String STAR = "_star_";
    }

    public static final class RecentList implements BaseColumns {
        public static final String TABLE_NAME = "recent";
        public static final Uri CONTENT_URI = appendUri(TABLE_NAME);

        public static final String DATA = "data";
        public static final String COUNT = "count";
    }

    private static Uri appendUri(String path) {
        return Uri.withAppendedPath(BASE_URI, path);
    }
}
