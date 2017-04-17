package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.LibraryMap.MapL;

public class viewbook extends drawer1 {

    private TabLayout tab;
    private ViewPager vpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frame = (FrameLayout)findViewById(R.id.frame);
        getLayoutInflater().inflate(R.layout.activity_viewbook, frame);

        tab = (TabLayout) findViewById(R.id.tabLayout);

        vpager = (ViewPager) findViewById(R.id.pager);

        pager adapter = new pager(getSupportFragmentManager());
        adapter.addFragment(new tab1("Information Technology"), "Information Technology");
        adapter.addFragment(new tab1("Computer Science"), "Computer Science");
        adapter.addFragment(new tab1("Mechanical Engineering"), "Mechanical Engineering");
        adapter.addFragment(new tab1("Electrical Engineering"), "Electrical Engineering");

        vpager.setAdapter(adapter);

        tab.setupWithViewPager(vpager);

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpager.setOffscreenPageLimit(3);
    }
}