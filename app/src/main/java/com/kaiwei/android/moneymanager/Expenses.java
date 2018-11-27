package com.kaiwei.android.moneymanager;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class Expenses {

    private UUID mExpensesId;
    private Date mExpensesDate;
    private Date mExpensesTime;
    private double mExpensesTotal;
    private String mExpensesCategory;
    private String mExpensesPhotoFile;
    private String mExpensesNote;

    public Expenses(){
        this(UUID.randomUUID());
    }

    public Expenses(UUID id){
        mExpensesId = id;
        mExpensesDate =  new Date();
        mExpensesTime = new Date();
    }

    public UUID getExpensesId() {
        return mExpensesId;
    }

    public Date getExpensesDate() {
        return mExpensesDate;
    }

    public void setExpensesDate(Date expensesDate) {
        mExpensesDate = expensesDate;
    }

    public Date getExpensesTime() {
        return mExpensesTime;
    }

    public void setExpensesTime(Date expensesTime) {
        mExpensesTime = expensesTime;
    }

    public double getExpensesTotal() {
        return mExpensesTotal;
    }

    public void setExpensesTotal(double expensesTotal) {
        mExpensesTotal = expensesTotal;
    }

    public String getExpensesCategory() {
        return mExpensesCategory;
    }

    public void setExpensesCategory(String expensesCategory) {
        mExpensesCategory = expensesCategory;
    }

    public String getExpensesPhotoFile() {
        return mExpensesPhotoFile;
    }

    public void setExpensesPhotoFile(String expensesPhotoFile) {
        mExpensesPhotoFile = expensesPhotoFile;
    }

    public String getExpensesNote() {
        return mExpensesNote;
    }

    public void setExpensesNote(String expensesNote) {
        mExpensesNote = expensesNote;
    }
}
