package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ExpensesListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, ExpensesListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new ExpensesListFragment();
    }
}
