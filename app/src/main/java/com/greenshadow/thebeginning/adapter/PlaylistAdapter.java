package com.greenshadow.thebeginning.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greenshadow.thebeginning.R;
import com.greenshadow.thebeginning.data.DBStruct;

/**
 * @author greenshadow
 */
public class PlaylistAdapter extends AbsCursorAdapter {
    private TextView playlistTitle;
    private TextView playlistDescription;
    private ImageButton more;

    public PlaylistAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_playlist, null);
    }

    @Override
    protected void onBindTitle(View rootView, Context context, Cursor cursor) {
    }

    @Override
    protected void onBindContent(View rootView, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DBStruct.Playlist.NAME));
        if (name.equals(DBStruct.Playlist.STAR)) {
            name = mContext.getString(R.string.my_star_music);
        }
        playlistTitle.setText(name);
        String description = cursor.getString(cursor.getColumnIndex(DBStruct.Playlist.DESCRIPTION));
        if (TextUtils.isEmpty(description)) {
            playlistDescription.setVisibility(View.GONE);
        } else {
            playlistDescription.setVisibility(View.VISIBLE);
            playlistDescription.setText(description);
        }
    }

    @Override
    protected void initViews(View rootView) {
        playlistTitle = (TextView) rootView.findViewById(R.id.playlist_title);
        playlistDescription = (TextView) rootView.findViewById(R.id.playlist_description);
        more = (ImageButton) rootView.findViewById(R.id.more);
    }
}
