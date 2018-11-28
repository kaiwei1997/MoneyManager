package com.kaiwei.android.moneymanager;

import java.sql.Blob;
import java.util.Date;
import java.util.UUID;

public class Expense {

    private UUID mExpensesId;
    private Date mExpensesDate;
    private Date mExpensesTime;
    private double mExpensesTotal;
    private String mExpensesCategory;
    private byte[] mExpensesPhotoFile;
    private String mExpensesNote;

    public Expense(){
        this(UUID.randomUUID());
    }

    public Expense(UUID id){
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

    public void setExpensesPhotoFile(byte[] expensesPhotoFile) {
        mExpensesPhotoFile = expensesPhotoFile;
    }

    public byte[] getExpensesPhotoFile() {
        return mExpensesPhotoFile;
    }

    public String getExpensesNote() {
        return mExpensesNote;
    }

    public void setExpensesNote(String expensesNote) {
        mExpensesNote = expensesNote;
    }
}
