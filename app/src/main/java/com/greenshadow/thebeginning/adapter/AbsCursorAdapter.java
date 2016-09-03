package com.greenshadow.thebeginning.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.greenshadow.thebeginning.R;

import java.util.HashSet;

/**
 * @author greenshadow
 */
public abstract class AbsCursorAdapter extends CursorAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    private HashSet<Integer> disableItem;

    protected TextView title;
    private ViewGroup contentPanel;

    public AbsCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
        disableItem = new HashSet<>();
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public final void bindView(View view, Context context, Cursor cursor) {
        initBasicViews(view);
        initViews(view);

        if (isTitle(cursor)) {
            title.setVisibility(View.VISIBLE);
            title.setText(cursor.getString(cursor.getColumnIndex("title")));
            contentPanel.setVisibility(View.GONE);
            disableItem.add(cursor.getPosition());
            onBindTitle(view, context, cursor);
        } else {
            title.setVisibility(View.GONE);
            contentPanel.setVisibility(View.VISIBLE);
            onBindContent(view, context, cursor);
        }
    }

    private boolean isTitle(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex("_id")) == -1;
    }

    private void initBasicViews(View rootView) {
        title = (TextView) rootView.findViewById(R.id.separator_title);
        contentPanel = (ViewGroup) rootView.findViewById(R.id.content);
    }

    @Override
    public final boolean isEnabled(int position) {
        if (disableItem.contains(position)) {
            return false;
        }
        return super.isEnabled(position);
    }

    protected abstract void onBindTitle(View rootView, Context context, Cursor cursor);

    protected abstract void onBindContent(View rootView, Context context, Cursor cursor);

    protected abstract void initViews(View rootView);
}
