package com.kaiwei.android.moneymanager;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class DebtActivity extends SingleFragmentActivity {
    private static final String EXTRA_DEBT_ID =
            "com.kaiwei.android.moneymanager.debt_id";

    @Override
    protected Fragment createFragment() {
        UUID debtId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_DEBT_ID);
        return DebtFragment.newInstance(debtId);
    }
}
