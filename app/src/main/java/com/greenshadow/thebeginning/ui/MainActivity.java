package com.greenshadow.thebeginning.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.greenshadow.thebeginning.R;
import com.greenshadow.thebeginning.adapter.PlaylistAdapter;

/**
 * @author greenshadow
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Cursor mCursor;
    private PlaylistAdapter mAdapter;
    private MatrixCursor recentTitle, starTitle;

    private ImageButton musicStar, musicList, musicAll;
    private ListView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();
        initDatas();
    }

    private void initViews() {
        musicStar = (ImageButton) findViewById(R.id.music_star);
        musicList = (ImageButton) findViewById(R.id.music_list);
        musicAll = (ImageButton) findViewById(R.id.music_all);
        list = (ListView) findViewById(R.id.list);
    }

    private void initEvents() {
        musicStar.setOnClickListener(this);
        musicList.setOnClickListener(this);
        musicAll.setOnClickListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initDatas() {
//        if (recentTitle == null) {
//            recentTitle = new MatrixCursor(new String[]{"_id", "title"});
//            recentTitle.addRow(new String[]{"-1", "recent"});
//        }
//        Cursor recent = App.getInstance().getRecentListManager()
//                .getRecentCursor(DBStruct.AllMusic.MUSIC_DISPLAY_LIST_PROJECTION);

        if (starTitle == null) {
            starTitle = new MatrixCursor(new String[]{"_id", "title"});
            starTitle.addRow(new String[]{"-1", "playlist"});
        }
        Cursor playlist = getApp().getPlaylistManager().getAllPlaylist();

        mCursor = new MergeCursor(new Cursor[]{/*recentTitle, recent, */starTitle, playlist});
        mAdapter = new PlaylistAdapter(this, mCursor);
        list.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.music_star:
                i = new Intent(this, MusicListActivity.class);
                i.putExtra(MusicListActivity.EXTRA_TYPE, MusicListActivity.TYPE_MUSIC_STAR);
                startActivity(i);
                break;
            case R.id.music_list:
                break;
            case R.id.music_all:
                i = new Intent(this, MusicListActivity.class);
                i.putExtra(MusicListActivity.EXTRA_TYPE, MusicListActivity.TYPE_MUSIC_ALL);
                startActivity(i);
                break;
        }
    }
}
