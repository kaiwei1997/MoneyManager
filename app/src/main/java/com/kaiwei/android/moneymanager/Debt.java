package com.kaiwei.android.moneymanager;

import java.util.Date;
import java.util.UUID;

public class Debt {
    private UUID mDebtId;
    private String mDebtTitle;
    private Date mDebtDate;
    private String mDebtDescription;
    private Double mDebtAmount;
    private boolean mReturned;
    private String mDebtor;
    private String mContact;
    private byte[] mDebtPhotoFile;

    public Debt(){
        this(UUID.randomUUID());
    }

    public Debt(UUID id){
        mDebtId = id;
        mDebtDate =  new Date();
    }

    public UUID getDebtId() {
        return mDebtId;
    }

    public String getDebtTitle() {
        return mDebtTitle;
    }

    public void setDebtTitle(String debtTitle) {
        mDebtTitle = debtTitle;
    }

    public Date getDebtDate() {
        return mDebtDate;
    }

    public void setDebtDate(Date debtDate) {
        mDebtDate = debtDate;
    }

    public String getDebtDescription() {
        return mDebtDescription;
    }

    public void setDebtDescription(String debtDescription) {
        mDebtDescription = debtDescription;
    }

    public boolean isReturned() {
        return mReturned;
    }

    public void setReturned(boolean aReturn) {
        mReturned = aReturn;
    }

    public String getDebtor() {
        return mDebtor;
    }

    public void setDebtor(String debtor) {
        mDebtor = debtor;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public byte[] getDebtPhotoFile() {
        return mDebtPhotoFile;
    }

    public void setDebtPhotoFile(byte[] debtPhotoFile) {
        mDebtPhotoFile = debtPhotoFile;
    }

    public Double getDebtAmount() {
        return mDebtAmount;
    }

    public void setDebtAmount(Double debtAmount) {
        mDebtAmount = debtAmount;
    }
}
