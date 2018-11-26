package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class IncomeListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContaxt){
        Intent intent = new Intent(packageContaxt, IncomeListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new IncomeListFragment();
    }
}
