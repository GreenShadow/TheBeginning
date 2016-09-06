package com.greenshadow.thebeginning.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.greenshadow.thebeginning.App;
import com.greenshadow.thebeginning.R;
import com.greenshadow.thebeginning.adapter.MusicListAdapter;
import com.greenshadow.thebeginning.manager.MusicListManager;
import com.greenshadow.thebeginning.ui.view.MusicListEmptyView;

/**
 * @author greenshadow
 */
public class MusicListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_LIST_NAME = "list_name";
    public static final String TYPE_MUSIC_ALL = "all";
    public static final String TYPE_MUSIC_STAR = "star";
    public static final String TYPE_MUSIC_PLAYLIST = "playlist";

    private static final String TAG = "MusicListActivity";

    private MusicListAdapter mAdapter;
    private ListView mList;
    private MusicListEmptyView emptyView;
    private String mActivityType;
    private String listName;
    private String mTitle;
    private int emptyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mActivityType = intent.getStringExtra(EXTRA_TYPE);
        if (TextUtils.isEmpty(mActivityType)) {
            Log.e(TAG, "finish, mActivityType is empty! ");
            finish();
            return;
        }
        if (!initAdapter(intent)) {
            finish();
            return;
        }
        setContentView(R.layout.activity_music_list);
        mList = (ListView) findViewById(R.id.list);
        mList.setAdapter(mAdapter);
        emptyView = (MusicListEmptyView) findViewById(R.id.empty);
        emptyView.setEmptyType(emptyType);
        if (emptyView.needScanMusic()) {
            emptyView.setRefreshClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(MusicListActivity.this, ScanMusicActivity.class), 0);
                }
            });
        }
        mList.setEmptyView(emptyView);
        mList.setOnItemClickListener(this);
        setUpActionBar();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    private boolean initAdapter(Intent intent) {
        Cursor cursor;
        switch (mActivityType) {
            case TYPE_MUSIC_ALL:
                cursor = App.getInstance().getMusicListManager().getAllMusic();
                if (cursor == null) {
                    Log.e(TAG, "finish, get all music error");
                    return false;
                }
                emptyType = MusicListEmptyView.TYPE_MUSIC_ALL;
                mTitle = getString(R.string.all_music);
                break;
            case TYPE_MUSIC_STAR:
                cursor = App.getInstance().getPlaylistManager().getStarList();
                if (cursor == null) {
                    Log.e(TAG, "finish, start list load error!");
                    return false;
                }
                emptyType = MusicListEmptyView.TYPE_STAR;
                mTitle = getString(R.string.my_collection);
                break;
            case TYPE_MUSIC_PLAYLIST:
                listName = intent.getStringExtra(EXTRA_LIST_NAME);
                if (TextUtils.isEmpty(listName)) {
                    Log.e(TAG, "finish, playlist name is empty!");
                    return false;
                }
                cursor = App.getInstance().getPlaylistManager().getPlaylist(listName);
                if (cursor == null) {
                    Log.e(TAG, "finish, playlist not exists!");
                    return false;
                }
                emptyType = MusicListEmptyView.TYPE_PLAYLIST;
                mTitle = getString(R.string.play_list);
                break;
            default:
                Log.e(TAG, "finish, not support this mActivityType: " + mActivityType);
                return false;
        }
        mAdapter = new MusicListAdapter(this, cursor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 9/3/16 play music 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            
        }
    }

    //    @Override
//    public void onFinish(Cursor cursor) {
//        if (cursor == null || cursor.isClosed() || cursor.getCount() == 0) {
//            Toast.makeText(this, "no musics found", Toast.LENGTH_LONG).show();
//            emptyView.setEmptyType(MusicListEmptyView.TYPE_LOCAL_NO_MUSIC);
//        } else {
//            Toast.makeText(this, "scan finish", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onPermissionDenied() {

//    }
}
