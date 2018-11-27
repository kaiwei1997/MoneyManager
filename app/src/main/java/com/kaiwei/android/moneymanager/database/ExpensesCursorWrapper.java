package com.kaiwei.android.moneymanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kaiwei.android.moneymanager.Expenses;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.ExpensesTable;

import java.util.Date;
import java.util.UUID;

public class ExpensesCursorWrapper extends CursorWrapper {
    public ExpensesCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Expenses getExpenses(){
        String uuidString = getString(getColumnIndex(ExpensesTable.Cols.UUID));
        long date = getLong(getColumnIndex(ExpensesTable.Cols.DATE));
        long time = getLong(getColumnIndex(ExpensesTable.Cols.TIME));
        Double amount = getDouble(getColumnIndex(ExpensesTable.Cols.AMOUNT));
        String category = getString(getColumnIndex(ExpensesTable.Cols.CATEGORY));
        String photoFile = getString(getColumnIndex(ExpensesTable.Cols.PHOTO));
        String note = getString(getColumnIndex(ExpensesTable.Cols.NOTE));

        Expenses expenses = new Expenses(UUID.fromString(uuidString));
        expenses.setExpensesDate(new Date(date));
        expenses.setExpensesTime(new Date(time));
        expenses.setExpensesTotal(amount);
        expenses.setExpensesCategory(category);
        expenses.setExpensesPhotoFile(photoFile);
        expenses.setExpensesNote(note);

        return expenses;
    }

}
