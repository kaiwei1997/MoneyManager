package com.kaiwei.android.moneymanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class ExpenseFragment extends Fragment {

    private static final String ARG_EXPENSES_ID = "expenses_id";

    public static ExpenseFragment newInstance(UUID expensesId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXPENSES_ID, expensesId);

        ExpenseFragment expensesFragment = new ExpenseFragment();
        expensesFragment.setArguments(args);
        return expensesFragment;
    }
}
