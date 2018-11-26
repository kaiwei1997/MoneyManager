package com.kaiwei.android.moneymanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kaiwei.android.moneymanager.Category;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.CategoryTable;

public class CategoryCursorWrapper extends CursorWrapper {
    public CategoryCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Category getCategory(){
        String name = getString(getColumnIndex(CategoryTable.Cols.NAME));
        String type = getString(getColumnIndex(CategoryTable.Cols.TYPE));

        Category category = new Category();
        category.setCategoryName(name);
        category.setCategoryType(type);

        return category;
    }
}
