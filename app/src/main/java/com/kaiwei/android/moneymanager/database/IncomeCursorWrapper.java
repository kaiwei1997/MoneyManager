package com.kaiwei.android.moneymanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kaiwei.android.moneymanager.Income;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.IncomeTable;

import java.util.Date;
import java.util.UUID;

public class IncomeCursorWrapper extends CursorWrapper {
    public IncomeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Income getIncome(){
        String uuidString = getString(getColumnIndex(IncomeTable.Cols.UUID));
        Double amount = getDouble(getColumnIndex(IncomeTable.Cols.AMOUNT));
        String category = getString(getColumnIndex(IncomeTable.Cols.CATEGORY));
        long date = getLong(getColumnIndex(IncomeTable.Cols.DATE));
        String note = getString(getColumnIndex(IncomeTable.Cols.NOTE));

        Income income = new Income(UUID.fromString(uuidString));
        income.setIncomeTotal(amount);
        income.setIncomeCategory(category);
        income.setIncomeDate(new Date(date));
        income.setIncomeNote(note);

        return income;
    }
}
