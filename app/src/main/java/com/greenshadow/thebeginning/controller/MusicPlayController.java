package com.greenshadow.thebeginning.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.greenshadow.thebeginning.services.MusicPlayService;

/**
 * @author greenshadow
 */

public class MusicPlayController {
    private Context mContext;
    private Uri currentUri;

    public MusicPlayController(Context context) {
        this.mContext = context;
    }

    public synchronized void play(@NonNull Uri uri) {
        if (currentUri.equals(uri)) {
            return;
        }
        currentUri = uri;
        Intent i = new Intent(mContext, MusicPlayService.class);
        i.setData(uri);
        mContext.startService(i);
    }

    public void pause() {
    }
}
