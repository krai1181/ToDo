package com.avi.todo;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.avi.todo.activities.MainActivity;


public class MyNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       /* String data  = intent.getStringExtra();
        if(data!=null)
        createNotification(context,data);*/

    }

    private void createNotification(Context context, String data) {

    }
}
