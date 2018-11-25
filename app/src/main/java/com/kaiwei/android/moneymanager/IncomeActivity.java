package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class IncomeActivity extends SingleFragmentActivity {

    private static final String EXTRA_INCOME_ID =
            "com.kaiwei.android.moneymanager.income_id";

    public static Intent newIntent(Context packageContext, UUID incomeId){
        Intent intent = new Intent(packageContext, IncomeActivity.class);
        intent.putExtra(EXTRA_INCOME_ID, incomeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID incomeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_INCOME_ID);
        return IncomeFragment.newInstance(incomeId);
    }
}
