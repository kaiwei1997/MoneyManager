package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;
import com.kaiwei.android.moneymanager.database.OverviewCursorWrapper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OverviewLab {
    private static OverviewLab sOverviewLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static  OverviewLab get(Context context){
        if(sOverviewLab == null){
            sOverviewLab = new OverviewLab(context);
        }

        return sOverviewLab;
    }

    private OverviewLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MoneyManagerBaseHelper(mContext)
                .getWritableDatabase();

    }

    public List<Overview> getOverview(){
        List<Overview> overviews = new ArrayList<>();

        OverviewCursorWrapper cursor = queryIncomeOverview();
        OverviewCursorWrapper cursor1 = queryExpensesOverview();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                overviews.add(cursor.getOverview());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        try{
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()){
                overviews.add(cursor1.getOverview());
                cursor1.moveToNext();
            }
        }finally {
            cursor1.close();
        }

        return overviews;
    }

    public Double getIncomeTotal(){
        Double total;

        OverviewCursorWrapper cursor = queryIncomeTotal();

        try{
            cursor.moveToFirst();
            total = cursor.getDouble(0);
        }finally {
            cursor.close();
        }
        return total;
    }

    public Double getExpensesTotal(){
        Double total;

        OverviewCursorWrapper cursor = queryExpensesTotal();

        try{
            cursor.moveToFirst();
            total = cursor.getDouble(0);
        }finally {
            cursor.close();
        }
        return total;
    }


    private OverviewCursorWrapper queryIncomeOverview(){
        String query = "SELECT income_category, income_date, SUM(income_amount) AS total FROM income_record GROUP BY income_category ORDER BY income_date desc";
        Cursor cursor = mDatabase.rawQuery(query,null);

        return new OverviewCursorWrapper(cursor);
    }

    private OverviewCursorWrapper queryExpensesOverview(){
        String query = "SELECT expenses_category, expenses_date, SUM(expenses_amount) AS total FROM expenses_record GROUP BY expenses_category ORDER BY expenses_date desc";
        Cursor cursor = mDatabase.rawQuery(query,null);

        return new OverviewCursorWrapper(cursor);
    }

    private OverviewCursorWrapper queryIncomeTotal(){
        String query = "SELECT SUM(income_amount) AS total FROM income_record";
        Cursor cursor = mDatabase.rawQuery(query,null);

        return new OverviewCursorWrapper(cursor);
    }

    private OverviewCursorWrapper queryExpensesTotal(){
        String query = "SELECT SUM(expenses_amount) AS total FROM expenses_record";
        Cursor cursor = mDatabase.rawQuery(query,null);

        return new OverviewCursorWrapper(cursor);
    }
}
