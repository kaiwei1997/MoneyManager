package com.kaiwei.android.moneymanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kaiwei.android.moneymanager.Debt;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.DebtTable;

import java.util.Date;
import java.util.UUID;

public class DebtCursorWrapper extends CursorWrapper {
    public DebtCursorWrapper(Cursor cursor){ super(cursor);}

    public Debt getDebt(){
        String uuid = getString(getColumnIndex(DebtTable.Cols.UUID));
        String title = getString(getColumnIndex(DebtTable.Cols.TITLE));
        long date = getLong(getColumnIndex(DebtTable.Cols.DATE));
        String description = getString(getColumnIndex(DebtTable.Cols.DESCRIPTION));
        double amount = getDouble(getColumnIndex(DebtTable.Cols.AMOUNT));
        int returned = getInt(getColumnIndex(DebtTable.Cols.RETURNED));
        String debtor = getString(getColumnIndex(DebtTable.Cols.DEBTOR));
        String contact = getString(getColumnIndex(DebtTable.Cols.CONTACT));
        byte[] photoFile = getBlob(getColumnIndex(DebtTable.Cols.PHOTO));

        Debt debt = new Debt(UUID.fromString(uuid));
        debt.setDebtTitle(title);
        debt.setDebtDate(new Date(date));
        debt.setDebtDescription(description);
        debt.setDebtAmount(amount);
        debt.setReturned(returned != 0);
        debt.setDebtor(debtor);
        debt.setContact(contact);
        debt.setDebtPhotoFile(photoFile);

        return debt;
    }
}
