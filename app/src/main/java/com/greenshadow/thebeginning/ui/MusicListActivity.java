package com.greenshadow.thebeginning.ui;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.greenshadow.thebeginning.App;
import com.greenshadow.thebeginning.adapter.MusicListAdapter;
import com.greenshadow.thebeginning.manager.MusicListManager;

/**
 * @author greenshadow
 */
public class MusicListActivity extends ListActivity {
    private static final String TAG = "MusicListActivity";

    private MusicListAdapter mAdapter;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(type)) {
            Log.e(TAG, "finish, type is empty! ");
            finish();
        }

        if (type.equals("all")) {
//            Cursor cursor = MusicListManager.getInstance(getContentResolver())
//            mAdapter = new MusicListAdapter()
        }
    }
}
