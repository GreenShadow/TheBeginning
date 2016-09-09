package com.greenshadow.thebeginning.manager;

import android.app.Activity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author greenshadow
 */
public class ActivityMgr {
    private static ActivityMgr mInstance;

    private Set<Activity> activities;

    private ActivityMgr() {
        activities = new HashSet<>();
    }

    public static ActivityMgr getInstance() {
        if (mInstance == null) {
            synchronized (ActivityMgr.class) {
                if (mInstance == null) {
                    mInstance = new ActivityMgr();
                }
            }
        }
        return mInstance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
        System.exit(0);
    }
}
