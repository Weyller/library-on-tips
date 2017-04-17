package com.example.rajk.libraryontips.reminderForBook.admin.MarkedBookReminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.Book;
import com.example.rajk.libraryontips.reminderForBook.admin.UI.BD;
import com.example.rajk.libraryontips.reminderForBook.admin.UI.CurrentBookActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MarkedBookReminderService extends Service {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    HashMap<String, DatabaseReference> hashMap = new HashMap<>();
    HashMap<String, ValueEventListener> hashMapListeners = new HashMap<>();
    Context context = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", Context.MODE_PRIVATE);
        String RollNo = sharedPreferences.getString("RollNo", "");

        if (!TextUtils.isEmpty(RollNo)) {
            final DatabaseReference db_setReminders = mDatabase.getReference().child("Student").child(RollNo).child("MarkedBookISBN").getRef();

            db_setReminders.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final String MarkedBookISBN = dataSnapshot.getValue(String.class);

                    Log.e(TAG, MarkedBookISBN);

                    DatabaseReference db = mDatabase.getReference().child("Book").child(MarkedBookISBN).getRef();
                    ValueEventListener valueEventListener = db.addValueEventListener(new ValueEventListener()

                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String, Object> mapBook = (Map<String, Object>) dataSnapshot.getValue();

                            Book book = new Book();

                            book.setAuthor((String) mapBook.get("Author"));
                            book.setBranch((String) mapBook.get("Branch"));
                            book.setCopiesNo((String) mapBook.get("CopiesNo"));
                            book.setISBN_No((String) mapBook.get("ISBN_No"));
                            book.setNoOfPages((String) mapBook.get("NoOfPages"));
                            book.setRatings((String) mapBook.get("Ratings"));
                            book.setRatingPeopleNumber((String) mapBook.get("RatingPeopleNumber"));
                            book.setTitle((String) mapBook.get("Title"));
                            book.setShelfNo((String) mapBook.get("ShelfNo"));
                            book.setSubject((String) mapBook.get("Subject"));
                            book.setAvailableCopies((String) mapBook.get("AvailableCopies"));

                            if (Integer.parseInt(book.getAvailableCopies()) > 0) {
                                Intent intent1 = new Intent(context, BD.class);
                                intent1.putExtra("ISBN_No", book.getISBN_No());
                                PendingIntent notificIntent = PendingIntent.getActivity(context, 0, intent1, 0);

                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_notific_icon)
                                        .setContentTitle("Book Available").setTicker("Alert").setContentText(book.getTitle() + " Now Available");
                                mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                                mBuilder.setAutoCancel(true);
                                mBuilder.setContentIntent(notificIntent);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1, mBuilder.build());
                                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(500);
                                if (hashMapListeners.get(dataSnapshot.getKey()) != null) {
                                    removeChildEventListener(MarkedBookISBN, this, db_setReminders);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    hashMapListeners.put(MarkedBookISBN, valueEventListener);
                    hashMap.put(MarkedBookISBN, db);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    if (hashMapListeners.get(dataSnapshot.getKey()) != null) {
                        removeChildEventListener(dataSnapshot.getKey(), hashMapListeners.get(dataSnapshot.getKey()), db_setReminders);
                    }
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

    private void removeChildEventListener(String markedBookISBN, ValueEventListener cel, DatabaseReference databaseMain) {
        DatabaseReference db = hashMap.get(markedBookISBN);
        db.removeEventListener(cel);
        hashMap.remove(markedBookISBN);
        hashMapListeners.remove(markedBookISBN);
        databaseMain.child(markedBookISBN).removeValue();
    }
}
