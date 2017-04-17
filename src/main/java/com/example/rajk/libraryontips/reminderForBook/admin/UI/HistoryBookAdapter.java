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

/**
 * Created by RajK on 12-03-2017.
 */
public class HistoryBookAdapter extends RecyclerView.Adapter<HistoryBookAdapter.MyViewHolder> implements View.OnClickListener {

private List<HistoryBook> historyBookList;
        DatabaseReference mDatabase;
public HistoryBookAdapter(List<HistoryBook> historyBookList) {
        this.historyBookList = historyBookList;
        }
    @Override
    public void onClick(View v) {

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView book_title, book_author, book_issueDate, book_returnDate,book_fine;

        public MyViewHolder(View view) {
            super(view);
            book_title = (TextView) view.findViewById(R.id.tv_title);
            book_returnDate = (TextView) view.findViewById(R.id.tv_returnDate);
            book_author = (TextView) view.findViewById(R.id.tv_author);
            book_issueDate = (TextView) view.findViewById(R.id.tv_issueDate);
            book_fine = (TextView) view.findViewById(R.id.tv_fine);

            view.setOnClickListener(HistoryBookAdapter.this);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historybook_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HistoryBook historyBook = historyBookList.get(position);
        holder.book_title.setText(historyBook.getTitle());
        holder.book_author.setText(historyBook.getAuthor());
        holder.book_issueDate.setText(historyBook.getIssuedDate());
        holder.book_returnDate.setText(historyBook.getReturnDate());
        holder.book_fine.setText(historyBook.getFine());

    }

    @Override
    public int getItemCount() {
        return historyBookList.size();
    }



}
