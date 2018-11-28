package com.kaiwei.android.moneymanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kaiwei.android.moneymanager.Overview;

public class OverviewCursorWrapper extends CursorWrapper {

    private static final String INOME_CATEGORY_INDEX = "income_category";
    private static final String EXPENSE_CATEGORY_INDEX = "expenses_category";
    private static final String TOTAL_INDEX = "total";

    public OverviewCursorWrapper(Cursor cursor){super(cursor);}

    public Overview getIncomeOverview(){
        String category = getString(getColumnIndexOrThrow(INOME_CATEGORY_INDEX));
        Double total = getDouble(getColumnIndexOrThrow(TOTAL_INDEX));

        Overview overview = new Overview(category, total);

        return overview;
    }

    public Overview getExpenseOverview(){
        String category = getString(getColumnIndexOrThrow(EXPENSE_CATEGORY_INDEX));
        Double total = getDouble(getColumnIndexOrThrow(TOTAL_INDEX));

        Overview overview = new Overview(category, total);

        return overview;
    }
}
