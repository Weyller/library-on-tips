package com.example.rajk.libraryontips.reminderForBook.admin.ModelClass;

import java.util.ArrayList;

/**
 * Created by RajK on 03-03-2017.
 */
public class Student {
    String RollNo,Name;
    private String Password;
    String TotalFine;
   // ArrayList<HistoryBook> CurrentBookList=new ArrayList<>();
   // ArrayList<HistoryBook> HistoryBookList = new ArrayList<>();
   // ArrayList<String> MarkedBookISBN = new ArrayList<>();

    public Student() {
    }

    public Student(String rollNo, String name, String password, String totalFine) {
        RollNo = rollNo;
        Name = name;
        Password = password;
        TotalFine = totalFine;
    }
    /*
    public ArrayList<HistoryBook> getCurrentBookList() {
        return CurrentBookList;
    }

    public void setCurrentBookList(ArrayList<HistoryBook> currentBookList) {
        CurrentBookList = currentBookList;
    }
  public ArrayList<HistoryBook> getHistoryBookList() {
        return HistoryBookList;
    }

    public void setHistoryBookList(ArrayList<HistoryBook> historyBookList) {
        HistoryBookList = historyBookList;
    }

    public ArrayList<String> getMarkedBookISBN() {
        return MarkedBookISBN;
    }

    public void setMarkedBookISBN(ArrayList<String> markedBookISBN) {
        MarkedBookISBN = markedBookISBN;
    }

*/
    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getTotalFine() {
        return TotalFine;
    }

    public void setTotalFine(String totalFine) {
        TotalFine = totalFine;
    }



}

