package com.greenshadow.thebeginning.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.greenshadow.thebeginning.App;
import com.greenshadow.thebeginning.manager.ActivityMgr;

/**
 * @author greenshadow
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMgr.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        ActivityMgr.getInstance().removeActivity(this);
        super.onDestroy();
    }

    protected final App getApp() {
        return (App) getApplication();
    }

    @Override
    @CallSuper
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
