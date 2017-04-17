package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.StudentLogin.sessions;

public class Splash extends AppCompatActivity {

    sessions s;
    private static int SPLASH_TIME_OUT = 1000;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        icon = (ImageView)findViewById(R.id.icon);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        icon.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                s = new sessions(getApplicationContext());
                s.checklogin();
                finish();
            }
        }, SPLASH_TIME_OUT);



    }
}
