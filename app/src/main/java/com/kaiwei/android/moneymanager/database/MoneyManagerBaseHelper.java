package com.kaiwei.android.moneymanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kaiwei.android.moneymanager.Debt;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.CategoryTable;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.DebtTable;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.ExpenseTable;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.IncomeTable;

public class MoneyManagerBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "moneyManager.db";

    public MoneyManagerBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + IncomeTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                IncomeTable.Cols.UUID + ", " +
                IncomeTable.Cols.DATE + ", " +
                IncomeTable.Cols.AMOUNT + ", " +
                IncomeTable.Cols.CATEGORY + ", " +
                IncomeTable.Cols.NOTE +
                ")"
        );

        db.execSQL("create table " + CategoryTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                CategoryTable.Cols.UUID + ", " +
                CategoryTable.Cols.NAME + ", " +
                CategoryTable.Cols.TYPE +
                ")"
        );

        db.execSQL("create table " + ExpenseTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ExpenseTable.Cols.UUID + ", " +
                ExpenseTable.Cols.DATE + ", " +
                ExpenseTable.Cols.TIME + ", " +
                ExpenseTable.Cols.AMOUNT + ", " +
                ExpenseTable.Cols.CATEGORY + ", " +
                ExpenseTable.Cols.PHOTO + ", " +
                ExpenseTable.Cols.NOTE +
                ")"
        );

        db.execSQL("create table " + DebtTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                DebtTable.Cols.UUID + ", " +
                DebtTable.Cols.TITLE + ", " +
                DebtTable.Cols.DATE + ", " +
                DebtTable.Cols.DESCRIPTION + ", " +
                DebtTable.Cols.AMOUNT + ", " +
                DebtTable.Cols.RETURNED + ", " +
                DebtTable.Cols.DEBTOR + ", " +
                DebtTable.Cols.CONTACT + ", " +
                DebtTable.Cols.PHOTO +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
