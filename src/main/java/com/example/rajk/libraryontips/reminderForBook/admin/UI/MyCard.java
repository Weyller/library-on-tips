package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.rajk.libraryontips.R;

import org.w3c.dom.Text;

public class MyCard extends drawer1 {

    TextView cardroll,cardname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = (FrameLayout)findViewById(R.id.frame);
        getLayoutInflater().inflate(R.layout.activity_my_card, frame);

        cardroll = (TextView)findViewById(R.id.cardroll);
        cardname = (TextView) findViewById(R.id.cardname);

        SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", Context.MODE_PRIVATE);

        String RollNo = sharedPreferences.getString("RollNo", "");
        String Name = sharedPreferences.getString("Name", "");

        cardroll.setText(RollNo);
        cardname.setText(Name);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this,viewbook.class);
        startActivity(intent);
        finish();
    }
}
