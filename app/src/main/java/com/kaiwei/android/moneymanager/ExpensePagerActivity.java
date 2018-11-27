package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class ExpensePagerActivity extends AppCompatActivity {
    private static final String EXTRA_EXPENSE_ID =
            "com.kaiwei.android.moneymanager.expense_id";

    private ViewPager mViewPager;
    private List<Expense> mExpenses;

    public static Intent newIntent(Context packageContext, UUID expenseId){
        Intent intent = new Intent(packageContext, ExpensePagerActivity.class);
        intent.putExtra(EXTRA_EXPENSE_ID, expenseId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_pager);

        UUID expenseId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_EXPENSE_ID);

        mViewPager = (ViewPager) findViewById(R.id.expense_view_pager);

        mExpenses = ExpenseLab.get(this).getExpenses();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Expense expense = mExpenses.get(i);
                return IncomeFragment.newInstance(expense.getExpensesId());
            }

            @Override
            public int getCount() {
                return mExpenses.size();
            }
        });

        for(int i =0; i<mExpenses.size(); i++){
            if(mExpenses.get(i).getExpensesId().equals(expenseId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
