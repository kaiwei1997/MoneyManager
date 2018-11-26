package com.kaiwei.android.moneymanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class IncomeFragment extends Fragment {
    private Income mIncome;
    private Button mDateButton;
    private EditText mAmountField;
    private Spinner mCategorySpinner;
    private EditText mNoteField;

    String[] category = new String[]{
            "Deposit",
            "Salary"
    };

    private static final String ARG_INCOME_ID = "income_id";


    public static IncomeFragment newInstance(UUID incomeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_INCOME_ID, incomeId);

        IncomeFragment incomeFragment = new IncomeFragment();
        incomeFragment.setArguments(args);
        return incomeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID incomeId = (UUID) getArguments().getSerializable(ARG_INCOME_ID);
        mIncome = IncomeLab.get(getActivity()).getIncome(incomeId);

    }

    @Override
    public void onPause() {
        super.onPause();

        IncomeLab.get(getActivity())
                .updateIncome(mIncome);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_income, container, false);
        getActivity().setTitle(R.string.title_income);

        mDateButton = (Button) v.findViewById(R.id.income_date);
        mDateButton.setText(mIncome.getIncomeDate().toString());
        mDateButton.setEnabled(false);

        mAmountField = (EditText) v.findViewById(R.id.income_total);
        mAmountField.setText(String.valueOf(mIncome.getIncomeTotal()));
        mAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mAmountField.setText("0");
                } else {
                    mIncome.setIncomeTotal(Double.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCategorySpinner = (Spinner) v.findViewById(R.id.spinner_incomeCategory);
        final List<String> categoryList = new ArrayList<>(Arrays.asList(category));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item,categoryList);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(spinnerArrayAdapter);
        mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIncome.setIncomeCategory(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mNoteField = (EditText) v.findViewById(R.id.income_note);
        mNoteField.setText(mIncome.getIncomeNote());
        mNoteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIncome.setIncomeNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
}
