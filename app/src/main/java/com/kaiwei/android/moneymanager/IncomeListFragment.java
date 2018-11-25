package com.kaiwei.android.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class IncomeListFragment extends Fragment {
    private RecyclerView mIncomeRecyclerView;
    private IncomeAdapter mIncomeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_list, container, false);

        mIncomeRecyclerView = (RecyclerView)view
                .findViewById(R.id.income_recycler_view);
        mIncomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        IncomeLab incomeLab = IncomeLab.get(getActivity());
        List<Income> incomes = incomeLab.getIncomes();

        if(mIncomeAdapter==null) {
            mIncomeAdapter = new IncomeAdapter(incomes);
            mIncomeRecyclerView.setAdapter(mIncomeAdapter);
        }else {
            mIncomeAdapter.notifyDataSetChanged();
        }
    }

    private class IncomeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private Income mIncome;
        private TextView mCategoryTextView;
        private TextView mDateTextView;
        private TextView mAmountTextView;

        public IncomeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_income, parent, false));
            itemView.setOnClickListener(this);

            mCategoryTextView = (TextView) itemView.findViewById(R.id.incomeCategory);
            mDateTextView = (TextView) itemView.findViewById(R.id.incomeDate);
            mAmountTextView = (TextView) itemView.findViewById(R.id.incomeAmount);
        }

        public void bind(Income income){
            mIncome = income;
            mCategoryTextView.setText(mIncome.getIncomeCategory());
            mDateTextView.setText(mIncome.getIncomeDate().toString());
            DecimalFormat precision = new DecimalFormat("RM 0.00");
            mAmountTextView.setText(precision.format(mIncome.getIncomeTotal()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = IncomePagerActivity.newIntent(getActivity(), mIncome.getIncomeId());
            startActivity(intent);
        }
    }

    private class IncomeAdapter extends RecyclerView.Adapter<IncomeHolder>{

        private List<Income> mIncomes;

        public IncomeAdapter(List<Income> incomes){
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
