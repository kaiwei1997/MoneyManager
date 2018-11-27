package com.kaiwei.android.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kaiwei.android.moneymanager.database.CategoryCursorWrapper;
import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.CategoryTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();

        CategoryCursorWrapper cursor = queryCategories(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                categories.add(cursor.getCategory());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return categories;
    }

    public List<String> getCategoryForType(String type){
        List<String> categories = new ArrayList<>();

        CategoryCursorWrapper cursor = queryCategories(
                CategoryTable.Cols.TYPE + " = ? ",
                new String[]{type}
        );

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                categories.add(cursor.getCategory().getCategoryName());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return categories;
    }

    public Category getCategory(UUID id){
        CategoryCursorWrapper cursor = queryCategories(
                CategoryTable.Cols.UUID + " = ? ",
                new String[]{id.toString()}
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCategory();
        }finally {
            cursor.close();
        }
    }

    public void updateCategory(Category category){
        String uuidString = category.getCategoryId().toString();
        ContentValues values = getContentValues(category);

        mDatabase.update(CategoryTable.NAME, values,
                CategoryTable.Cols.UUID + "= ?",
                new String[]{uuidString});
    }

    private CategoryCursorWrapper queryCategories(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CategoryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CategoryCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Category category){
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.UUID, category.getCategoryId().toString());
        values.put(CategoryTable.Cols.NAME, category.getCategoryName());
        values.put(CategoryTable.Cols.TYPE, category.getCategoryType());

        return values;
    }
}
