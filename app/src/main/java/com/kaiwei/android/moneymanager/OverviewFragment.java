package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;

import java.text.DecimalFormat;

public class OverviewFragment extends Fragment {

    private Button mIncomeButton;
    private TextView mIncomeTotal;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mDatabase = new MoneyManagerBaseHelper(mContext).getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        mIncomeButton = (Button)v.findViewById(R.id.btn_income);
        mIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Income income = new Income();
                IncomeLab.get(getActivity()).addIncome(income);
                Intent intent = IncomePagerActivity
                        .newIntent(getActivity(), income.getIncomeId());
                startActivity(intent);
            }
        });

        mIncomeTotal = (TextView)v.findViewById(R.id.tv_incomeAmount);
        queryIncomeTotal();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryIncomeTotal();
    }

    private Cursor queryIncomeTotal(){
        String query = "SELECT SUM(income_amount) AS total FROM income_record";
        Cursor cursor = mDatabase.rawQuery(
                query,null);
        cursor.moveToFirst();
        cursor.getInt(0);

        DecimalFormat precision = new DecimalFormat("RM 0.00");
        mIncomeTotal.setText(precision.format(cursor.getInt(0)));
        return cursor;
    }
}
