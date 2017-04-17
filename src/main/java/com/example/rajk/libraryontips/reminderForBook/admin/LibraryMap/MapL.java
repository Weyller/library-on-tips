package com.example.rajk.libraryontips.reminderForBook.admin.LibraryMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.rajk.libraryontips.R;

public class MapL extends AppCompatActivity {

    TextView one,two,three,four,five,six,seven,eight,nine,ten;
    Character a;
    TextView shelf;
    String shelfno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        shelfno = getIntent().getExtras().getString("ShelfNo");

        one = (TextView) findViewById(R.id.textView1);
        two = (TextView) findViewById(R.id.textView2);
        three = (TextView) findViewById(R.id.textView3);
        four = (TextView) findViewById(R.id.textView4);
        five = (TextView) findViewById(R.id.textView5);
        six = (TextView) findViewById(R.id.textView6);
        seven = (TextView) findViewById(R.id.textView7);
        eight = (TextView) findViewById(R.id.textView8);
        nine = (TextView) findViewById(R.id.textView9);
        ten = (TextView) findViewById(R.id.textView10);
        shelf = (TextView)findViewById(R.id.shelfLocate);
        shelf.setText(shelfno);


                one.setBackgroundColor(getResources().getColor(R.color.light));
                two.setBackgroundColor(getResources().getColor(R.color.light));
                three.setBackgroundColor(getResources().getColor(R.color.light));
                four.setBackgroundColor(getResources().getColor(R.color.light));
                five.setBackgroundColor(getResources().getColor(R.color.light));
                six.setBackgroundColor(getResources().getColor(R.color.light));
                seven.setBackgroundColor(getResources().getColor(R.color.light));
                eight.setBackgroundColor(getResources().getColor(R.color.light));
                nine.setBackgroundColor(getResources().getColor(R.color.light));
                ten.setBackgroundColor(getResources().getColor(R.color.light));

                a = shelfno.charAt(0);

                    switch(a)
                    {
                        case 'A': one.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'B': two.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'C': three.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'D': four.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'E': five.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'F': six.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'G': seven.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'H': eight.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'I': nine.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        case 'J': ten.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                    }
            }
 }