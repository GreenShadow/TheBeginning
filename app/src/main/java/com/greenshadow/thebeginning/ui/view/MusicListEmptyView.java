package com.greenshadow.thebeginning.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenshadow.thebeginning.R;

/**
 * @author greenshadow
 */

public class MusicListEmptyView extends LinearLayout {
    public static final int TYPE_MUSIC_ALL = 0;
    public static final int TYPE_STAR = 1;
    public static final int TYPE_PLAYLIST = 2;
    public static final int TYPE_MUSIC_PLAYLIST = 3;
    public static final int TYPE_LOCAL_NO_MUSIC = 4;

    private TextView emptyText;
    private Button refreshButton;
    private int mType = -1;

    public MusicListEmptyView(Context context) {
        this(context, null);
    }

    public MusicListEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicListEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MusicListEmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        emptyText = (TextView) findViewById(R.id.empty_text);
        refreshButton = (Button) findViewById(R.id.refresh_button);
    }

    public void setRefreshClickListener(OnClickListener l) {
        if (refreshButton != null) {
            refreshButton.setOnClickListener(l);
        }
    }

    public void hideRefreshButton() {
        if (refreshButton != null) {
            refreshButton.setVisibility(GONE);
        }
    }

    public boolean needScanMusic() {
        return mType == TYPE_MUSIC_ALL;
    }

    public void setEmptyType(int type) {
        switch (type) {
            case TYPE_MUSIC_ALL:
                setEmptyMessage(getContext().getString(R.string.no_music_to_scan));
                break;
            case TYPE_MUSIC_PLAYLIST:
                setEmptyMessage(getContext().getString(R.string.no_music_in_playlyst));
                hideRefreshButton();
                break;
            case TYPE_PLAYLIST:
                setEmptyMessage(getContext().getString(R.string.no_play_list));
                hideRefreshButton();
                break;
            case TYPE_STAR:
                setEmptyMessage(getContext().getString(R.string.no_star));
                hideRefreshButton();
                break;
            case TYPE_LOCAL_NO_MUSIC:
                setEmptyMessage(getContext().getString(R.string.no_music_local));
                hideRefreshButton();
                break;
            default:
                return;
        }
        mType = type;
    }

    public void setEmptyMessage(String message) {
        if (emptyText != null) {
            emptyText.setText(message);
        }
    }
}
