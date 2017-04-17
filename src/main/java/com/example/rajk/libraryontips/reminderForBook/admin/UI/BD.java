package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.LibraryMap.MapL;
import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Map;

public class BD extends AppCompatActivity {

    private Book book;
    RatingBar totalRating, userRating, myRatingRB;
    TextView book_name, author, copies, sub, rating, myRatingTV, branch, shelf;
    String isbn, download_url, ShelfNo, RollNo;
    StorageReference image_storage;
    ImageView image;
    DatabaseReference db, db_StudentMarkedBookList, dbRating, dbRatingStudent;
    Button markBookButton, submitRating;
    ImageButton locateShelf;
    ChildEventListener db_StudentMarkedBookListChildEventListener;
    ValueEventListener dbEventListener;
    LinearLayout rateBookLayout, myRatingLayout;
    int h;
    int editMyRatingStatus=1;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Typeface one, two ,three, four;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        one = Typeface.createFromAsset(getAssets(),"kaushan.otf");
        two = Typeface.createFromAsset(getAssets(),"lato.ttf");
        three = Typeface.createFromAsset(getAssets(),"sofia.otf");
        four = Typeface.createFromAsset(getAssets(),"walterturncoat.ttf");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        collapsingToolbarLayout.setTitle("Book Details");
        collapsingToolbarLayout.setBackgroundColor(Color.BLACK);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", Context.MODE_PRIVATE);
        RollNo = sharedPreferences.getString("RollNo", "");
        isbn = getIntent().getStringExtra("ISBN_No");

        book_name = (TextView) findViewById(R.id.title);
        book_name.setTypeface(two);
        branch = (TextView) findViewById(R.id.branch);
        shelf = (TextView) findViewById(R.id.shelf);
        author = (TextView) findViewById(R.id.author);
        sub = (TextView) findViewById(R.id.subject);
        image = (ImageView) findViewById(R.id.detailimage);
        copies = (TextView) findViewById(R.id.copies_avail);
        rateBookLayout = (LinearLayout) findViewById(R.id.l3);
        totalRating = (RatingBar) findViewById(R.id.totalRating);
        userRating = (RatingBar) findViewById(R.id.newRating);
        db = FirebaseDatabase.getInstance().getReference().child("Book").child(isbn).getRef();
        dbRating = db.child("RatingPeople").getRef();
        //  rating = (TextView) findViewById(R.id.rating);
        dbRatingStudent = dbRating.child(RollNo).getRef();
        submitRating = (Button) findViewById(R.id.submit_rating);

        myRatingLayout = (LinearLayout) findViewById(R.id.l0);
        myRatingRB = (RatingBar) findViewById(R.id.myRating);

        myRatingLayout.setVisibility(View.GONE);

        locateShelf = (ImageButton) findViewById(R.id.detail_shelf);
        image_storage = FirebaseStorage.getInstance().getReference().child("image");

        markBookButton = (Button) findViewById(R.id.markBookButton);


        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float userRatingValue = userRating.getRating();
                dbRating.child(RollNo).setValue(userRatingValue + "");
                calculateRating(userRatingValue);
                myRatingLayout.setVisibility(View.VISIBLE);
                rateBookLayout.setVisibility(View.GONE);
                myRatingRB.setRating(userRatingValue);
            }
        });

        dbRatingStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myRatingLayout.setVisibility(View.VISIBLE);
                    rateBookLayout.setVisibility(View.GONE);
                    Float userRatingValue = Float.parseFloat(dataSnapshot.getValue(String.class));
                    //     myRatingTV.setText(userRatingValue + "");
                    myRatingRB.setRating(userRatingValue);
                } else {
                    userRating.setRating(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbEventListener = db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> mapBook = (Map<String, Object>) dataSnapshot.getValue();

                book = new Book();

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
                totalRating.setRating(Float.parseFloat(book.getRatings()));
                copies.setText(book.getAvailableCopies());
                //    rating.setText(book.getRatings().toString());
                book_name.setText(book.getTitle());
                author.setText(" -"+book.getAuthor());
                sub.setText(book.getSubject());
                ShelfNo = book.getShelfNo();
                branch.setText(book.getBranch());
                shelf.setText(book.getShelfNo());

                h = Integer.parseInt(book.getAvailableCopies());
                if (h > 0) {
                    markBookButton.setText("Available");
                    markBookButton.setEnabled(false);
                    markBookButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    db_StudentMarkedBookList = FirebaseDatabase.getInstance().getReference().child("Student").child(RollNo).child("MarkedBookISBN").getRef();
                    final View.OnClickListener markButton = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (markBookButton.getText().toString()) {
                                case "Mark Book":
                                    db_StudentMarkedBookList.child(isbn).setValue(isbn);
                                    markBookButton.setText("Unmark Book");
                                    markBookButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    break;
                                case "Unmark Book":
                                    db_StudentMarkedBookList.child(isbn).removeValue();
                                    markBookButton.setText("Mark Book");
                                    markBookButton.setBackgroundColor(getResources().getColor(R.color.red));
                                    break;
                            }
                        }
                    };
                    markBookButton.setEnabled(true);
                    markBookButton.setText("Mark Book");
                    markBookButton.setOnClickListener(markButton);
                    db_StudentMarkedBookListChildEventListener = db_StudentMarkedBookList.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().toString().equals(isbn)) {
                                markBookButton.setText("Unmark Book");
                                markBookButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {


                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        locateShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BD.this, MapL.class);
                intent.putExtra("ShelfNo", ShelfNo);
                startActivity(intent);
            }
        });

        image_storage.child(isbn).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                download_url = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Can not load image", Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.with(getBaseContext()).load(download_url).placeholder(R.drawable.placeholder).into(image);

    }

    private void calculateRating(Float userRating) {
        Float totalRatings = Float.parseFloat(book.getRatings());
        Integer ratingPeople = Integer.parseInt(book.getRatingPeopleNumber());
        Float newRating = (totalRatings * ratingPeople + userRating) / (ratingPeople + 1);
        Float newR = roundTwoDecimals(newRating);
        db.child("RatingPeopleNumber").setValue((ratingPeople + 1) + "");
        db.child("Ratings").setValue(newRating + "");
        totalRating.setRating(newR);
        //    rating.setText(newR + "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Class callerClass;
        if (db_StudentMarkedBookListChildEventListener != null)
            db_StudentMarkedBookList.removeEventListener(db_StudentMarkedBookListChildEventListener);
        if (dbEventListener != null)
            db.removeEventListener(dbEventListener);
        /* String caller = getIntent().getStringExtra("caller");
        try {
            callerClass = Class.forName(caller);
            startActivity(new Intent(this,callerClass));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

            Intent intent = new Intent(this,viewbook.class);
            startActivity(intent);
            finish();

    }

    Float roundTwoDecimals(Float d) {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Float.valueOf(twoDForm.format(d));
    }
}
