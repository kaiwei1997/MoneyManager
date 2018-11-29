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

public class DebtPagerActivity extends AppCompatActivity {
    private static final String EXTRA_DEBT_ID =
            "com.kaiwei.android.moneymanager.debt_id";

    private ViewPager mViewPager;
    private List<Debt> mDebts;

    private Button mJumpToFirstButton;
    private Button mJumpToLastButton;

    public static Intent newIntent(Context packageContext, UUID debtId){
        Intent intent = new Intent(packageContext, DebtPagerActivity.class);
        intent.putExtra(EXTRA_DEBT_ID, debtId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_pager);
        setTitle(R.string.debt_detail);

        UUID debtId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_DEBT_ID);

        mViewPager = (ViewPager) findViewById(R.id.debt_view_pager);
        mJumpToFirstButton = (Button)findViewById(R.id.btn_jump_to_first);
        mJumpToLastButton = (Button)findViewById(R.id.btn_jump_to_last);

        mDebts = DebtLab.get(this).getDebt();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Debt debt = mDebts.get(i);
                return DebtFragment.newInstance(debt.getDebtId());
            }

            @Override
            public int getCount() {
                return mDebts.size();
            }
        });

        for(int i =0; i<mDebts.size(); i++){
            if(mDebts.get(i).getDebtId().equals(debtId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        if(mViewPager.getCurrentItem() == 0){
            mJumpToFirstButton.setEnabled(false);
        }else if(mViewPager.getCurrentItem() == mDebts.size() -1){
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
                }else if(position == mDebts.size() - 1){
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
                mViewPager.setCurrentItem(mDebts.size() - 1);
            }
        });
    }
}
