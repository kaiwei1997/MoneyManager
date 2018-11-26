package com.kaiwei.android.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.CategoryTable;

public class CategoryLab {
    private static CategoryLab sCategoryLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CategoryLab get(Context context) {
        if (sCategoryLab == null) {
            sCategoryLab = new CategoryLab(context);
        }
        return sCategoryLab;
    }

    private CategoryLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MoneyManagerBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addCategory(Category c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(CategoryTable.NAME, null, values);
    }



    public static ContentValues getContentValues(Category category){
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.NAME, category.getCategoryName());
        values.put(CategoryTable.Cols.TYPE, category.getCategoryType());

        return values;
    }
}
