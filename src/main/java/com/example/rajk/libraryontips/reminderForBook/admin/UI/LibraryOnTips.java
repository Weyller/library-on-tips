package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.example.rajk.libraryontips.reminderForBook.admin.CheckInternetConnectivity.NetWatcher;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class LibraryOnTips extends Application {
    private static LibraryOnTips mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }
    public static synchronized LibraryOnTips getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetWatcher.ConnectivityReceiverListener listener) {
        NetWatcher.connectivityReceiverListener = listener;
    }
}


