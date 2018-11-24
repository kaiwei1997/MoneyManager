package com.kaiwei.android.moneymanager;

import java.util.Date;
import java.util.UUID;

public class Income {

    private UUID mIncomeId;
    private Date mIncomeDate;
    private double mIncomeTotal;
    private String mIncomeCategory;
    private String mIncomeNote;

    public Income(){
        mIncomeId = UUID.randomUUID();
        mIncomeDate = new Date();
    }

    public UUID getIncomeId() {
        return mIncomeId;
    }

    public void setIncomeId(UUID incomeId) {
        mIncomeId = incomeId;
    }

    public Date getIncomeDate() {
        return mIncomeDate;
    }

    public void setIncomeDate(Date incomeDate) {
        mIncomeDate = incomeDate;
    }

    public double getIncomeTotal() {
        return mIncomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        mIncomeTotal = incomeTotal;
    }

    public String getIncomeCategory() {
        return mIncomeCategory;
    }

    public void setIncomeCategory(String incomeCategoryID) {
        mIncomeCategory = incomeCategoryID;
    }

    public String getIncomeNote() {
        return mIncomeNote;
    }

    public void setIncomeNote(String incomeNote) {
        mIncomeNote = incomeNote;
    }
}
