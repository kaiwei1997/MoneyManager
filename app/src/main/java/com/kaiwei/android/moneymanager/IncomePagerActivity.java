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
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class IncomePagerActivity extends AppCompatActivity {
    private static final String EXTRA_INCOME_ID =
            "com.kaiwei.android.moneymanager.income_id";

    private ViewPager mViewPager;
    private List<Income> mIncomes;

    private Button mJumpToFirstButton;
    private Button mJumpToLastButton;

    public static Intent newIntent(Context packageContext, UUID incomeID){
        Intent intent = new Intent(packageContext, IncomePagerActivity.class);
        intent.putExtra(EXTRA_INCOME_ID, incomeID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_pager);
        setTitle(R.string.income_detail);

        UUID incomeId = (UUID) getIntent()
        .getSerializableExtra(EXTRA_INCOME_ID);

        mViewPager = (ViewPager) findViewById(R.id.income_view_pager);
        mJumpToFirstButton = (Button)findViewById(R.id.btn_jump_to_first);
        mJumpToLastButton = (Button)findViewById(R.id.btn_jump_to_last);

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

        if(mViewPager.getCurrentItem() == 0){
            mJumpToFirstButton.setEnabled(false);
        }else if(mViewPager.getCurrentItem() == mIncomes.size() -1){
            mJumpToLastButton.setEnabled(false);
        }
        else{
            mJumpToFirstButton.setEnabled(true);
            mJumpToLastButton.setEnabled(true);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    mJumpToFirstButton.setEnabled(false);
                    mJumpToLastButton.setEnabled(true);
                }else if(position == mIncomes.size() - 1){
                    mJumpToLastButton.setEnabled(false);
                    mJumpToFirstButton.setEnabled(true);
                }else{
                    mJumpToFirstButton.setEnabled(true);
                    mJumpToLastButton.setEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mJumpToFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mJumpToLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mIncomes.size() - 1);
            }
        });
    }
}
