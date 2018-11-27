package com.kaiwei.android.moneymanager;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class ExpenseActivity extends SingleFragmentActivity {
    private static final String EXTRA_EXPENSES_ID =
            "com.kaiwei.android.moneymanager.expenses_id";

    @Override
    protected Fragment createFragment() {
        UUID expensesId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_EXPENSES_ID);
        return ExpenseFragment.newInstance(expensesId);
    }
}
