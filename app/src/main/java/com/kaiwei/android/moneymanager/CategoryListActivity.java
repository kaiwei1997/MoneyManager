package com.kaiwei.android.moneymanager;

import android.support.v4.app.Fragment;

public class CategoryListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CategoryListFragment();
    }
}
