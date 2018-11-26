package com.kaiwei.android.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kaiwei.android.moneymanager.database.IncomeCursorWrapper;
import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.IncomeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IncomeLab {
    private static IncomeLab sIncomeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static IncomeLab get(Context context) {
        if (sIncomeLab == null) {
            sIncomeLab = new IncomeLab(context);
        }
        return sIncomeLab;
    }

    private IncomeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MoneyManagerBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addIncome(Income i){
        ContentValues values = getContentValues(i);

        mDatabase.insert(IncomeTable.NAME, null, values);
    }

    public List<Income> getIncomes() {
        List<Income> incomes = new ArrayList<>();

        IncomeCursorWrapper cursor = queryIncomes(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                incomes.add(cursor.getIncome());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return incomes;
    }

    public Income getIncome(UUID id) {

        IncomeCursorWrapper cursor = queryIncomes(
                IncomeTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getIncome();
        }finally {
            cursor.close();
        }
    }

    public void updateIncome(Income income){
        String uuidString = income.getIncomeId().toString();
        ContentValues values = getContentValues(income);

        mDatabase.update(IncomeTable.NAME, values,
                IncomeTable.Cols.UUID + "= ?",
                new String[]{uuidString});
    }

    private IncomeCursorWrapper queryIncomes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                IncomeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new IncomeCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Income income){
        ContentValues values = new ContentValues();
        values.put(IncomeTable.Cols.UUID, income.getIncomeId().toString());
        values.put(IncomeTable.Cols.DATE, income.getIncomeDate().getTime());
        values.put(IncomeTable.Cols.CATEGORY, income.getIncomeCategory());
        values.put(IncomeTable.Cols.AMOUNT, income.getIncomeTotal());
        values.put(IncomeTable.Cols.NOTE, income.getIncomeNote());

        return values;
    }
}
