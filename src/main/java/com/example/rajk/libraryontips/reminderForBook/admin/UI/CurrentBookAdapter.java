package com.example.rajk.libraryontips.reminderForBook.admin.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rajk.libraryontips.R;
import com.example.rajk.libraryontips.reminderForBook.admin.ModelClass.HistoryBook;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CurrentBookAdapter extends RecyclerView.Adapter<CurrentBookAdapter.MyViewHolder> implements View.OnClickListener {

    private List<HistoryBook> CurrentBookList;
    DatabaseReference mDatabase;
    public CurrentBookAdapter(List<HistoryBook> CurrentBookList) {
        this.CurrentBookList = CurrentBookList;
    }
    @Override
    public void onClick(View v) {

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView book_title, book_author, book_issueDate, book_returnDate;

        public MyViewHolder(View view) {
            super(view);
            book_title = (TextView) view.findViewById(R.id.tv_title);
            book_returnDate = (TextView) view.findViewById(R.id.tv_returnDate);
            book_author = (TextView) view.findViewById(R.id.tv_author);
            book_issueDate = (TextView) view.findViewById(R.id.tv_issueDate);
            view.setOnClickListener(CurrentBookAdapter.this);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currentbook_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HistoryBook CurrentBook = CurrentBookList.get(position);
        holder.book_title.setText(CurrentBook.getTitle());
        holder.book_author.setText(CurrentBook.getAuthor());
        holder.book_issueDate.setText(CurrentBook.getIssuedDate());
        holder.book_returnDate.setText(CurrentBook.getReturnDate());
    }

    @Override
    public int getItemCount() {
        return CurrentBookList.size();
    }

}
