package com.example.rajk.libraryontips.reminderForBook.admin.ReturnBookReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.HistoryBook;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SetReminderAlarmService extends Service {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    ArrayList<HistoryBook> currentBookList = new ArrayList<>();
    AlarmManager mgrAlarm ;
    HashMap<String, PendingIntent> pendingIntentMap = new HashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentBookList.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", Context.MODE_PRIVATE);
        String RollNo = sharedPreferences.getString("RollNo", "");
        mgrAlarm=(AlarmManager) getSystemService(ALARM_SERVICE);
        if (!TextUtils.isEmpty(RollNo)) {
            DatabaseReference db_setReminders = mDatabase.getReference().child("Student").child(RollNo).child("CurrentBookList").getRef();
                db_setReminders.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HistoryBook currentBook = dataSnapshot.getValue(HistoryBook.class);
                        if(!currentBookList.contains(currentBook)) {
                            currentBookList.add(currentBook);
                            try {
                                setAlarmNotification(currentBook.getReturnDate(), currentBook.getISBN_No(), currentBook.getTitle());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        HistoryBook currentBook = dataSnapshot.getValue(HistoryBook.class);
                        currentBookList.remove(currentBook);
                        cancelAlarms(currentBook.getISBN_No());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setAlarmNotification(String returnDate,String ISBN_No,String bookName) throws ParseException {

        Intent intent = new Intent(this, ReminderForReturnBook.class);
        intent.putExtra("DueDate",returnDate);
        intent.putExtra("BookName",bookName);
        intent.putExtra("ISBN_No",ISBN_No);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, Integer.parseInt(ISBN_No), intent, 0);
        pendingIntentMap.put(ISBN_No,pendingIntent);

        Calendar calendar =Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = format.parse(returnDate);
        calendar.setTime(date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = df.format(c.getTime());
        Date today = df.parse(todayDate);
        long diff = date.getTime() - today.getTime();
        mgrAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,diff,AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void cancelAlarms(String ISBN_No) {
        PendingIntent pendingIntent= pendingIntentMap.get(ISBN_No);
        mgrAlarm.cancel(pendingIntent);
        pendingIntentMap.remove(ISBN_No);
    }
}
