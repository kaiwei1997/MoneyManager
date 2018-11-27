package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ExpenseListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, ExpenseListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new ExpenseListFragment();
    }
}
