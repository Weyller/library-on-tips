package com.example.rajk.libraryontips.reminderForBook.admin.ModelClass;

/**
 * Created by RajK on 03-03-2017.
 */
public class CopyBook {
    private String BarCode,Price,ISBN_No,DateOfPurchase;
    private String IssuedStatus;  // null if not issued otherwise roll no of student

    public CopyBook() {
    }

    public CopyBook(String barCode, String price, String ISBN_No, String dateOfPurchase, String issuedStatus, String dateIssued, String dueDate) {
        BarCode = barCode;
        Price = price;
        this.ISBN_No = ISBN_No;
        DateOfPurchase = dateOfPurchase;
        IssuedStatus = issuedStatus;
        DateIssued = dateIssued;
        DueDate = dueDate;
    }

    String DateIssued;

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    String DueDate;    // null if not issued otherwise DOI

    public String getDateIssued() {
        return DateIssued;
    }

    public void setDateIssued(String dateIssued) {
        DateIssued = dateIssued;
    }

    public String getIssuedStatus() {
        return IssuedStatus;
    }

    public void setIssuedStatus(String issuedStatus) {
        IssuedStatus = issuedStatus;
    }

    public String getDateOfPurchase() {
        return DateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        DateOfPurchase = dateOfPurchase;
    }

    public String getISBN_No() {
        return ISBN_No;
    }

    public void setISBN_No(String ISBN_No) {
        this.ISBN_No = ISBN_No;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }


}
