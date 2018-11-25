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

public class IncomePagerActivity extends AppCompatActivity {
    private static final String EXTRA_INCOME_ID =
            "com.kaiwei.android.moneymanager.income_id";

    private ViewPager mViewPager;
    private List<Income> mIncomes;

    public static Intent newIntent(Context packageContext, UUID incomeID){
        Intent intent = new Intent(packageContext, IncomePagerActivity.class);
        intent.putExtra(EXTRA_INCOME_ID, incomeID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_pager);

        UUID incomeId = (UUID) getIntent()
        .getSerializableExtra(EXTRA_INCOME_ID);

        mViewPager = (ViewPager) findViewById(R.id.income_view_pager);

        mIncomes = IncomeLab.get(this).getIncomes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Income income = mIncomes.get(i);
                return IncomeFragment.newInstance(income.getIncomeId());
            }

            @Override
            public int getCount() {
                return mIncomes.size();
            }
        });

        for(int i =0; i<mIncomes.size(); i++){
            if(mIncomes.get(i).getIncomeId().equals(incomeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
