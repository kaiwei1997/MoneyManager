package com.kaiwei.android.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kaiwei.android.moneymanager.database.ExpenseCursorWrapper;
import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.ExpenseTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpenseLab {
    private static ExpenseLab sExpensesLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ExpenseLab get(Context context){
        if(sExpensesLab == null){
            sExpensesLab = new ExpenseLab(context);
        }
        return sExpensesLab;
    }

    private ExpenseLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MoneyManagerBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addExpense(Expense e){
        ContentValues values = getContentValues(e);

        mDatabase.insert(ExpenseTable.NAME,null, values);
    }

    public void removeExpense(Expense e){
        String uuidString = e.getExpensesId().toString();

        mDatabase.delete(ExpenseTable.NAME,
                ExpenseTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public List<Expense> getExpenses(){
        List<Expense> expenses = new ArrayList<>();

        ExpenseCursorWrapper cursor = queryExpenses(null,null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                expenses.add(cursor.getExpense());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return expenses;
    }

    public Expense getExpense(UUID id){
        ExpenseCursorWrapper cursor = queryExpenses(
                ExpenseTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getExpense();
        }finally {
            cursor.close();
        }
    }

    public void updateExpense(Expense expense){
        String uuidString = expense.getExpensesId().toString();
        ContentValues values = getContentValues(expense);

        mDatabase.update(ExpenseTable.NAME, values,
                ExpenseTable.Cols.UUID + "= ?",
                new String[]{uuidString});
    }

    private ExpenseCursorWrapper queryExpenses(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ExpenseTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ExpenseCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Expense expenses){
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.Cols.UUID, expenses.getExpensesId().toString());
        values.put(ExpenseTable.Cols.DATE, expenses.getExpensesDate().getTime());
        values.put(ExpenseTable.Cols.TIME, expenses.getExpensesTime().getTime());
        values.put(ExpenseTable.Cols.AMOUNT, expenses.getExpensesTotal());
        values.put(ExpenseTable.Cols.CATEGORY, expenses.getExpensesCategory());
        values.put(ExpenseTable.Cols.PHOTO, expenses.getExpensesPhotoFile());
        values.put(ExpenseTable.Cols.NOTE, expenses.getExpensesNote());

        return values;
    }
}
