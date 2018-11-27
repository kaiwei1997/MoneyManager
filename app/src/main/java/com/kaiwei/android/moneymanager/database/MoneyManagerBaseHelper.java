package com.kaiwei.android.moneymanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kaiwei.android.moneymanager.Category;
import com.kaiwei.android.moneymanager.Expenses;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.CategoryTable;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.ExpensesTable;
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

        db.execSQL("create table " + ExpensesTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ExpensesTable.Cols.UUID + ", " +
                ExpensesTable.Cols.DATE + ", " +
                ExpensesTable.Cols.TIME + ", " +
                ExpensesTable.Cols.AMOUNT + ", " +
                ExpensesTable.Cols.CATEGORY + ", " +
                ExpensesTable.Cols.PHOTO + ", " +
                ExpensesTable.Cols.NOTE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
