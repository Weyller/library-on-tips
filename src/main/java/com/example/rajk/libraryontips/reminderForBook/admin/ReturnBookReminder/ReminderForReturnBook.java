package com.example.rajk.libraryontips.reminderForBook.admin.ReturnBookReminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.UI.CurrentBookActivity;

import java.util.Calendar;

public class ReminderForReturnBook extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("BookName")!=null&&intent.getStringExtra("DueDate")!=null)
        createNotification(context,"Return "+intent.getStringExtra("BookName"),"Due Date:"+intent.getStringExtra("DueDate") ,"Alert");
    }
    public void createNotification(Context context,String msg,String msgText,String msgAlert)
    {
        PendingIntent notificIntent=PendingIntent.getActivity(context,0,new Intent(context,CurrentBookActivity.class),0);
        NotificationCompat.Builder mBuilder= (NotificationCompat.Builder) new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_notific_icon)
                .setContentTitle(msg).setTicker(msgAlert).setContentText(msgText);
        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        Calendar calendar = Calendar.getInstance();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(calendar.get(Calendar.MILLISECOND),mBuilder.build());
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

    }

}
