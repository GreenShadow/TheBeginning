package com.greenshadow.thebeginning.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greenshadow.thebeginning.R;
import com.greenshadow.thebeginning.data.DBStruct;

import java.util.HashSet;

/**
 * @author greenshadow
 */
public class MusicListAdapter extends AbsCursorAdapter {

    private TextView musicTitle, musicArtist, musicAlbum;
    private ImageButton more;

    public MusicListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_music_list, null);
    }

    @Override
    protected void onBindTitle(View rootView, Context context, Cursor cursor) {
    }

    @Override
    protected void onBindContent(View rootView, Context context, Cursor cursor) {
        musicTitle.setText(cursor.getString(cursor.getColumnIndex(DBStruct.AllMusic.DISPLAY_NAME)));
        musicArtist.setText(cursor.getString(cursor.getColumnIndex(DBStruct.AllMusic.ARTIST)));
        musicAlbum.setText(cursor.getString(cursor.getColumnIndex(DBStruct.AllMusic.ALBUM)));
    }

    @Override
    protected void initViews(View rootView) {
        musicTitle = (TextView) rootView.findViewById(R.id.music_title);
        musicArtist = (TextView) rootView.findViewById(R.id.music_artist);
        musicAlbum = (TextView) rootView.findViewById(R.id.music_album);
        more = (ImageButton) rootView.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "asdasd", Toast.LENGTH_LONG).show();
            }
        });
    }
}
