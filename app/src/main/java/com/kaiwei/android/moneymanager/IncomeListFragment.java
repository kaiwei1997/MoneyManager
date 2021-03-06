package com.kaiwei.android.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class IncomeListFragment extends Fragment {
    private RecyclerView mIncomeRecyclerView;
    private IncomeAdapter mIncomeAdapter;
    private TextView mEmptyTextView;
    private Button mNewIncomeButton;

    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.income_list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_list, container, false);

        mIncomeRecyclerView = (RecyclerView) view
                .findViewById(R.id.income_recycler_view);
        mIncomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEmptyTextView = (TextView)view.findViewById(R.id.income_empty_text);
        mNewIncomeButton = (Button) view.findViewById(R.id.new_income_button);
        mNewIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewIncome();
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fargment_income_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_income:
                CreateNewIncome();
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        IncomeLab incomeLab = IncomeLab.get(getActivity());
        int incomeCount = incomeLab.getIncomes().size();
        String subtitle = getResources().getQuantityString(R.plurals.income_subtitle_plural, incomeCount, incomeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

    private void CreateNewIncome(){
        Income income = new Income();
        IncomeLab.get(getActivity()).addIncome(income);
        Intent intent = IncomePagerActivity.newIntent(getActivity(), income.getIncomeId());
        startActivity(intent);
    }

    private void updateUI() {
        IncomeLab incomeLab = IncomeLab.get(getActivity());
        List<Income> incomes = incomeLab.getIncomes();

        if(incomes.size() == 0){
            mEmptyTextView.setVisibility(View.VISIBLE);
            mNewIncomeButton.setVisibility(View.VISIBLE);
        }else{
            mEmptyTextView.setVisibility(View.GONE);
            mNewIncomeButton.setVisibility(View.GONE);
        }

        if (mIncomeAdapter == null) {
            mIncomeAdapter = new IncomeAdapter(incomes);
            mIncomeRecyclerView.setAdapter(mIncomeAdapter);
        } else {
            mIncomeAdapter.setIncomes(incomes);
            mIncomeAdapter.notifyDataSetChanged();
        }
    }

    private class IncomeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Income mIncome;
        private TextView mCategoryTextView;
        private TextView mDateTextView;
        private TextView mAmountTextView;

        public IncomeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_income, parent, false));
            itemView.setOnClickListener(this);

            mCategoryTextView = (TextView) itemView.findViewById(R.id.incomeCategory);
            mDateTextView = (TextView) itemView.findViewById(R.id.incomeDate);
            mAmountTextView = (TextView) itemView.findViewById(R.id.incomeAmount);
        }

        public void bind(Income income) {
            mIncome = income;
            DateFormat df = new SimpleDateFormat("E, MMMM dd, yyyy");
            String formatDate = df.format(mIncome.getIncomeDate());
            mCategoryTextView.setText(mIncome.getIncomeCategory());
            mDateTextView.setText(formatDate);
            DecimalFormat precision = new DecimalFormat("RM 0.00");
            mAmountTextView.setText(precision.format(mIncome.getIncomeTotal()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = IncomePagerActivity.newIntent(getActivity(), mIncome.getIncomeId());
            startActivity(intent);
        }
    }

    private class IncomeAdapter extends RecyclerView.Adapter<IncomeHolder> {

        private List<Income> mIncomes;

        public void setIncomes(List<Income> incomes) {
            mIncomes = incomes;
        }
        public IncomeAdapter(List<Income> incomes) {
            mIncomes = incomes;
        }

        @NonNull
        @Override
        public IncomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new IncomeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull IncomeHolder holder, int position) {
            Income income = mIncomes.get(position);
            holder.bind(income);
        }

        @Override
        public int getItemCount() {
            return mIncomes.size();
        }
    }
}
