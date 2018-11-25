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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class IncomeFragment extends Fragment {
    private Income mIncome;
    private Button mDateButton;
    private EditText mAmountField;
    private Spinner mCategorySpinner;
    private EditText mNoteField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIncome = new Income();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_income, container, false);
        getActivity().setTitle(R.string.fragment_income);

        mDateButton = (Button) v.findViewById(R.id.income_date);
        mDateButton.setText(mIncome.getIncomeDate().toString());
        mDateButton.setEnabled(false);

        mAmountField = (EditText) v.findViewById(R.id.income_total);
        mAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString().trim())){
                    mAmountField.setText("0");
                }else{
                    mIncome.setIncomeTotal(Double.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCategorySpinner = (Spinner) v.findViewById(R.id.spinner_incomeCategory);
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