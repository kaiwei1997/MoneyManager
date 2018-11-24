package com.kaiwei.android.moneymanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MoneyManagerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new IncomeFragment();
    }
}
