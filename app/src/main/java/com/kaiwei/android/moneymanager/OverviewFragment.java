package com.kaiwei.android.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OverviewFragment extends Fragment {

    private Button mIncomeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        mIncomeButton = (Button)v.findViewById(R.id.btn_income);
        mIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Income income = new Income();
                IncomeLab.get(getActivity()).addIncome(income);
                Intent intent = IncomePagerActivity
                        .newIntent(getActivity(), income.getIncomeId());
                startActivity(intent);
            }
        });

        return v;
    }
}
