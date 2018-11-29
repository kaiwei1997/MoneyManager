package com.kaiwei.android.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kaiwei.android.moneymanager.database.DebtCursorWrapper;
import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;
import com.kaiwei.android.moneymanager.database.MoneyManagerDbSchema.DebtTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DebtLab {
    private static DebtLab sDebtLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DebtLab get(Context context){
        if(sDebtLab == null){
            sDebtLab = new DebtLab(context);
        }
        return sDebtLab;
    }

    private DebtLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MoneyManagerBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addDebt(Debt d){
        ContentValues values = getContentValues(d);

        mDatabase.insert(DebtTable.NAME, null, values);
    }

    public void removeDebt(Debt d){
        String uuidString = d.getDebtId().toString();

        mDatabase.delete(DebtTable.NAME,
                DebtTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void updateDebt(Debt debt){
        String uuidString = debt.getDebtId().toString();
        ContentValues values = getContentValues(debt);

        mDatabase.update(DebtTable.NAME, values,
                DebtTable.Cols.UUID + "= ?",
                new String[]{uuidString});
    }

    public List<Debt> getDebt(){
        List<Debt> debts = new ArrayList<>();

        DebtCursorWrapper cursor = queryDebt(null,null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                debts.add(cursor.getDebt());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return debts;
    }

    public Debt getDebt(UUID id){
        DebtCursorWrapper cursor = queryDebt(
                DebtTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getDebt();
        }finally {
            cursor.close();
        }
    }

    private DebtCursorWrapper queryDebt(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                DebtTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new DebtCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Debt debt){
        ContentValues values = new ContentValues();
        values.put(DebtTable.Cols.UUID, debt.getDebtId().toString());
        values.put(DebtTable.Cols.TITLE, debt.getDebtTitle());
        values.put(DebtTable.Cols.DATE, debt.getDebtDate().getTime());
        values.put(DebtTable.Cols.DESCRIPTION, debt.getDebtDescription());
        values.put(DebtTable.Cols.AMOUNT, debt.getDebtAmount());
        values.put(DebtTable.Cols.RETURNED, debt.isReturned() ? 1:0);
        values.put(DebtTable.Cols.DEBTOR, debt.getDebtor());
        values.put(DebtTable.Cols.CONTACT, debt.getContact());
        values.put(DebtTable.Cols.PHOTO, debt.getDebtPhotoFile());
        return values;
    }

}
