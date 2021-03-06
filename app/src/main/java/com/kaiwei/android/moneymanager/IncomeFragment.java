package com.kaiwei.android.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.nmaltais.calcdialog.CalcDialog;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IncomeFragment extends Fragment implements CalcDialog.CalcDialogCallback{
    private Income mIncome;
    private Button mDateButton;
    private EditText mAmountField;
    private Spinner mCategorySpinner;
    private EditText mNoteField;
    private ImageButton mCalculator;

    private static final String ARG_INCOME_ID = "income_id";
    private static final String CATEGORY_TYPE = "Income";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int CALC_REQUEST_CODE = 2;

    private BigDecimal value;

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
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.income_detail);
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

        mDateButton = (Button) v.findViewById(R.id.expenses_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mIncome.getIncomeDate());
                dialog.setTargetFragment(IncomeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mAmountField = (EditText) v.findViewById(R.id.expense_total);
        DecimalFormat precision = new DecimalFormat("0.00");
        mAmountField.setText(String.valueOf(precision.format(mIncome.getIncomeTotal())));
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

        mCalculator = (ImageButton)v.findViewById(R.id.btn_calculator);
        value = null;
        mCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                final CalcDialog calcDialog = CalcDialog.newInstance(CALC_REQUEST_CODE);
                calcDialog.setValue(value);
                calcDialog.setTargetFragment(IncomeFragment.this, CALC_REQUEST_CODE);
                calcDialog.show(fragmentManager, "calc_dialog");
            }
        });

        mCategorySpinner = (Spinner) v.findViewById(R.id.spinner_expenseCategory);
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        final List<String> categoryList = categoryLab.getCategoryForType(CATEGORY_TYPE);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, categoryList);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String selectedValue = mIncome.getIncomeCategory();
        mCategorySpinner.setAdapter(spinnerArrayAdapter);
        if(selectedValue != null){
            int spinnerPosition = spinnerArrayAdapter.getPosition(selectedValue);
            mCategorySpinner.setSelection(spinnerPosition);
        }
        mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIncome.setIncomeCategory(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mNoteField = (EditText) v.findViewById(R.id.expense_note);
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

    private void updateDate() {
        DateFormat df = new SimpleDateFormat("E, MMMM dd, yyyy");
        mDateButton.setText(df.format(mIncome.getIncomeDate()));
    }

    @Override
    public void onValueEntered(int requestCode, BigDecimal value) {
        if(requestCode == CALC_REQUEST_CODE){
            DecimalFormat precision = new DecimalFormat("0.00");
            mAmountField.setText(precision.format(Double.valueOf(value.toPlainString())));
            mIncome.setIncomeTotal(Double.valueOf(value.toPlainString()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mIncome.setIncomeDate(date);
            updateDate();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                IncomeLab.get(getActivity()).removeIncome(mIncome);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
