package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.CheckInternetConnectivity.NetWatcher;
import com.example.rajk.libraryontips.reminderForBook.admin.LibraryMap.MapL;
import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.Student;
import com.example.rajk.libraryontips.reminderForBook.admin.StudentLogin.sessions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class drawer1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,NetWatcher.ConnectivityReceiverListener {

        sessions s;
        TextView name,roll;
        NavigationView navigationView;
        DatabaseReference mDatabase,db;
        Student student;
        static  boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Student");

        boolean isConnectedprev = isConnected;
        isConnected = NetWatcher.isConnected(getApplicationContext());
        if(isConnected!=isConnectedprev||isConnected==false)
            showSnack(isConnected);


        SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", Context.MODE_PRIVATE);
        String RollNo = sharedPreferences.getString("RollNo", "");
        String Name = sharedPreferences.getString("Name","");

        db = mDatabase.child(RollNo).getRef();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String,Object> mapBook= (Map<String,Object>)  dataSnapshot.getValue();

                    student = new Student();
                    student.setName((String) mapBook.get("Name"));
                    student.setPassword((String) mapBook.get("Password"));
                    student.setRollNo((String) mapBook.get("RollNo"));
                    student.setTotalFine((String) mapBook.get("TotalFine"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        s = new sessions(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(fab.INVISIBLE);

        View headerView = navigationView.getHeaderView(0);
        name = (TextView)headerView.findViewById(R.id.name);
        roll = (TextView)headerView.findViewById(R.id.roll);
        name.setText(Name);
        roll.setText(RollNo);
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawers();
            } else {
                super.onBackPressed();
            }
        }
    }


//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.first) {
            Intent intent = new Intent(this,viewbook.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.second) {
            Intent intent = new Intent(this,CurrentBookActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.third) {
            Intent intent = new Intent(this,HistoryBookActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.five) {
            Intent intent = new Intent(this,MyCard.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.six) {
            navigationView.getMenu().findItem(R.id.six).setTitle("Fine : " + student.getTotalFine());
            Toast.makeText(drawer1.this, "Fine : " + student.getTotalFine(), Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.fourth) {
            s.clearloginsession();
            finish();
            //12114072
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        if (isConnected == false) {
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

        }
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LibraryOnTips.getInstance().setConnectivityListener(this);
}

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }
}

