package com.kaiwei.android.moneymanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

public class ExpensesCursorWrapper extends CursorWrapper {
    public ExpensesCursorWrapper(Cursor cursor){
        super(cursor);
    }


}
