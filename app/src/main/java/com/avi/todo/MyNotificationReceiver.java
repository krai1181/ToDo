package com.avi.todo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.avi.todo.activities.AddTaskActivity;

import static android.support.constraint.Constraints.TAG;


public class MyNotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 3;

    @Override
    public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: Succsess" );

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("My Assignment");
        builder.setContentText("This is the Body");
        builder.setSmallIcon(R.drawable.ic_list_image_background);

        //create intent
        Intent notifyIntent = new Intent(context, AddTaskActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);


    }


}
