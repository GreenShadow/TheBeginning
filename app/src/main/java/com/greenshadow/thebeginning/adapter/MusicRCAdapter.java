package com.greenshadow.thebeginning.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author greenshadow
 */
public class MusicRCAdapter extends RecyclerViewCursorAdapter {

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public MusicRCAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
    }

    @Override
    protected void onContentChanged() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
