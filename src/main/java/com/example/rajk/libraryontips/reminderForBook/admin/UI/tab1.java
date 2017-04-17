package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.Book;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.ValueEventListener;

//@SuppressLint("ValidFragment")
public class tab1 extends Fragment {

    private List<viewbd> bookList = new ArrayList<>();
    private List<viewbd> b = new ArrayList<>();
    public List<viewbd> filtered = new ArrayList<>();
    public RecyclerView recyclerView;
    private ViewBookAdapter mAdapter;
    private EditText search_book,search_book_subject;
    DatabaseReference mDatabase,db;
    String branch;
    public tab1(String b) {
        this.branch = b;
    }
    public tab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        search_book=(EditText)rootView.findViewById(R.id.search_book);
        search_book_subject = (EditText) rootView.findViewById(R.id.search_book_subject);
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addingrows(bookList);
        b=bookList;
        new ChildAddAsync().execute();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                viewbd book = b.get(position);
                Intent i = new Intent(getActivity(),BD.class);
                i.putExtra("ISBN_No",book.getBook_ISBN());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //search functionality
        search_book.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search_book.getText().equals(""))
                {
                    addingrows(b);
                }
                else
                {
                    filtered.clear();
                    addingrows(filtered);

                    s = s.toString().toLowerCase();
                    bookList.size();
                    for (int i = 0; i < bookList.size(); i++) {

                        String item = bookList.get(i).getBook_name().toLowerCase();

                        if (item.contains(s)) {
                            filtered.add(bookList.get(i));
                        }
                    }

                    b = filtered;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.notifyDataSetChanged();
            }
        });


        search_book_subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search_book_subject.getText().equals(""))
                {
                    addingrows(b);
                }
                else
                {
                    filtered.clear();
                    addingrows(filtered);

                    s = s.toString().toLowerCase();
                    for (int i = 0; i < bookList.size(); i++) {
                        String item = bookList.get(i).getBook_author().toLowerCase();

                        if (item.contains(s)) {
                            filtered.add(bookList.get(i));
                        }
                    }

                    b = filtered;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    class ChildAddAsync extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected String doInBackground(final String... params) {
            try {

                db = mDatabase.child("Book").getRef();

                db.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Book book = dataSnapshot.getValue(Book.class);
                        if( book.getBranch().equals(branch))
                        {
                            viewbd bk = new viewbd();
                            bk.setBook_name(book.getTitle());
                            bk.setBook_author(book.getAuthor());
                            bk.setBook_ISBN(dataSnapshot.getKey());
                            bk.setBook_branch(book.getBranch());
                            bk.setBook_subject(book.getSubject());

                            bookList.add(bk);
                            mAdapter.notifyDataSetChanged();
                            pd.dismiss();
                        }
                        else
                        {
                            pd.dismiss();
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

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String j) {

        }
    }

    private void addingrows(final List<viewbd> k)
    {
        mAdapter = new ViewBookAdapter(k);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
    }
}