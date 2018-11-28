package com.kaiwei.android.moneymanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kaiwei.android.moneymanager.Expense;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.ExpenseTable;

import java.util.Date;
import java.util.UUID;

public class ExpenseCursorWrapper extends CursorWrapper {
    public ExpenseCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Expense getExpense(){
        String uuidString = getString(getColumnIndex(ExpenseTable.Cols.UUID));
        long date = getLong(getColumnIndex(ExpenseTable.Cols.DATE));
        long time = getLong(getColumnIndex(ExpenseTable.Cols.TIME));
        Double amount = getDouble(getColumnIndex(ExpenseTable.Cols.AMOUNT));
        String category = getString(getColumnIndex(ExpenseTable.Cols.CATEGORY));
        //Byte[] photoFile = (getColumnIndex(ExpenseTable.Cols.PHOTO));
        String note = getString(getColumnIndex(ExpenseTable.Cols.NOTE));

        Expense expenses = new Expense(UUID.fromString(uuidString));
        expenses.setExpensesDate(new Date(date));
        expenses.setExpensesTime(new Date(time));
        expenses.setExpensesTotal(amount);
        expenses.setExpensesCategory(category);
        //expenses.setExpensesPhotoFile(photoFile);
        expenses.setExpensesNote(note);

        return expenses;
    }

}
