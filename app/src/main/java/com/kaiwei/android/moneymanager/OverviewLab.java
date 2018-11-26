package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;
import com.kaiwei.android.moneymanager.database.OverviewCursorWrapper;

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

        OverviewCursorWrapper cursor = queryOverview();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                overviews.add(cursor.getOverview());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return overviews;
    }

    private OverviewCursorWrapper queryOverview(){
        String query = "SELECT income_category, income_date, SUM(income_amount) AS total FROM income_record GROUP BY income_category ORDER BY income_date desc";
        Cursor cursor = mDatabase.rawQuery(query,null);

        return new OverviewCursorWrapper(cursor);
    }
}
