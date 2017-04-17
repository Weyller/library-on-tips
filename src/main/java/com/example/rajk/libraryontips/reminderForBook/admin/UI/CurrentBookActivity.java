package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.HistoryBook;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentBookActivity extends drawer1 {
    EditText search_title,search_author;

    DatabaseReference mDatabase;
    private ArrayList<HistoryBook> mHistoryBookList,m,filtered;
    ProgressDialog progressDialog;
    DatabaseReference mRef;
    RecyclerView recyclerView;
    private CurrentBookAdapter mAdapter;
    public static HistoryBook selHistoryBook;
    Button button;
    LinearLayout l;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frame = (FrameLayout)findViewById(R.id.frame);
        getLayoutInflater().inflate(R.layout.history_book_activity, frame);
        setTitle(R.string.current);

        SharedPreferences sharedPreferences = getSharedPreferences("StudentDetails", Context.MODE_PRIVATE);
        String RollNo = sharedPreferences.getString("RollNo", "");

        //Toast.makeText(CurrentBookActivity.this,RollNo, Toast.LENGTH_SHORT).show();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        mRef = mDatabase.child("Student").child(RollNo).child("CurrentBookList").getRef();
        search_title =  (EditText) findViewById(R.id.search_title);
        search_author =  (EditText) findViewById(R.id.search_author);
        progressDialog = new ProgressDialog(this);
        mHistoryBookList = new ArrayList<>();
        m = new ArrayList<>();
        filtered = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.historybook_list);
        button= (Button)findViewById(R.id.button);
        l= (LinearLayout)findViewById(R.id.l);

        button.setVisibility(View.VISIBLE);
        l.setVisibility(View.INVISIBLE);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    addingrows(mHistoryBookList);
                    m=mHistoryBookList;

                    new ChildAddAsync().execute();
                }
                else
                {
                    Toast.makeText(CurrentBookActivity.this,"No Books",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CurrentBookActivity.this,viewbook.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                selHistoryBook = m.get(position);
                Intent intent = new Intent(getApplicationContext(),BD.class);
                intent.putExtra("ISBN_No",selHistoryBook.getISBN_No());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position) {
            }

        }));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.INVISIBLE);
                l.setVisibility(View.VISIBLE);
            }
        });

        search_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search_title.getText().equals(""))
                {
                    addingrows(m);
                }
                else
                {
                    filtered.clear();
                    addingrows(filtered);

                    s = s.toString().toLowerCase();
                    for (int i = 0; i < mHistoryBookList.size(); i++) {
                        String item = mHistoryBookList.get(i).getTitle().toString().toLowerCase();

                        if (item.contains(s)) {
                            filtered.add(mHistoryBookList.get(i));
                        }
                    }

                    m = filtered;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.notifyDataSetChanged();
            }
        });

        search_author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search_author.getText().equals(""))
                {
                    addingrows(m);
                }
                else
                {
                    filtered.clear();
                    addingrows(filtered);

                    s = s.toString().toLowerCase();
                    for (int i = 0; i < mHistoryBookList.size(); i++) {
                        String item = mHistoryBookList.get(i).getAuthor().toString().toLowerCase();

                        if (item.contains(s)) {
                            filtered.add(mHistoryBookList.get(i));
                        }
                    }
                    m = filtered;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addingrows(final List<HistoryBook> k)
    {
        mAdapter = new CurrentBookAdapter(k);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //  progressDialog.dismiss();
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this,viewbook.class);
        startActivity(intent);

    }
    class ChildAddAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(CurrentBookActivity.this);
            pd.setMessage("Loading...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(final String... params) {
            try {
                mRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //  progressDialog.show();
                            HistoryBook HistoryBook = dataSnapshot.getValue(HistoryBook.class);
                            mHistoryBookList.add(HistoryBook);
                            mAdapter.notifyDataSetChanged();
                            pd.dismiss();

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        HistoryBook HistoryBook = dataSnapshot.getValue(HistoryBook.class);
                        mHistoryBookList.remove(HistoryBook);
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //   progressDialog.show();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Failure", "FAILURE,FAILURE 111111");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String j)
        {

        }
    }}
