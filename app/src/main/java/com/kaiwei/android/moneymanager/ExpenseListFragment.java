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

public class ExpenseListFragment extends Fragment {
    private RecyclerView mExpenseRecyclerView;
    private ExpenseAdapter mExpenseAdapter;
    private TextView mEmptyTextView;
    private Button mNewExpenseButton;

    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.expense_list);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses_list, container, false);

        mExpenseRecyclerView = (RecyclerView) view
                .findViewById(R.id.expenses_recycler_view);
        mExpenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyTextView = (TextView)view.findViewById(R.id.expense_empty_text);
        mNewExpenseButton = (Button) view.findViewById(R.id.new_expense_button);
        mNewExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewExpense();
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
        inflater.inflate(R.menu.fargment_expense_list, menu);

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
            case R.id.new_expense:
                CreateNewExpense();
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
        ExpenseLab expenseLab = ExpenseLab.get(getActivity());
        int expenseCount = expenseLab.getExpenses().size();
        String subtitle = getResources().getQuantityString(R.plurals.expense_subtitle_plural, expenseCount, expenseCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

    private void CreateNewExpense(){
        Expense expense = new Expense();
        ExpenseLab.get(getActivity()).addExpense(expense);
        Intent intent = ExpensePagerActivity.newIntent(getActivity(), expense.getExpensesId());
        startActivity(intent);
    }


    private void updateUI() {
        ExpenseLab expenseLab = ExpenseLab.get(getActivity());
        List<Expense> expenses = expenseLab.getExpenses();

        if(expenses.size() == 0){
            mEmptyTextView.setVisibility(View.VISIBLE);
            mNewExpenseButton.setVisibility(View.VISIBLE);
        }else{
            mEmptyTextView.setVisibility(View.GONE);
            mNewExpenseButton.setVisibility(View.GONE);
        }

        if (mExpenseAdapter == null) {
            mExpenseAdapter = new ExpenseAdapter(expenses);
            mExpenseRecyclerView.setAdapter(mExpenseAdapter);
        } else {
            mExpenseAdapter.setExpenses(expenses);
            mExpenseAdapter.notifyDataSetChanged();
        }
    }

    private class ExpenseHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Expense mExpense;
        private TextView mCategoryTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private TextView mAmountTextView;

        public ExpenseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_expenses, parent, false));
            itemView.setOnClickListener(this);

            mCategoryTextView = (TextView) itemView.findViewById(R.id.expense_category);
            mDateTextView = (TextView) itemView.findViewById(R.id.expense_date);
            mTimeTextView = (TextView) itemView.findViewById(R.id.expense_time);
            mAmountTextView = (TextView) itemView.findViewById(R.id.expense_amount);
        }

        public void bind(Expense expense) {
            mExpense = expense;
            DateFormat df = new SimpleDateFormat("E, MMMM dd, yyyy");
            DateFormat tf = new SimpleDateFormat("hh:mm a");
            String formatDate = df.format(mExpense.getExpensesDate());
            String formatTime = tf.format(mExpense.getExpensesTime());
            mCategoryTextView.setText(mExpense.getExpensesCategory());
            mDateTextView.setText(formatDate);
            mTimeTextView.setText(formatTime);
            DecimalFormat precision = new DecimalFormat("RM 0.00");
            mAmountTextView.setText(precision.format(mExpense.getExpensesTotal()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = ExpensePagerActivity.newIntent(getActivity(), mExpense.getExpensesId());
            startActivity(intent);
        }
    }

    private class ExpenseAdapter extends RecyclerView.Adapter<ExpenseHolder> {

        private List<Expense> mExpenses;

        public void setExpenses(List<Expense> expenses) {
            mExpenses = expenses;
        }

        public ExpenseAdapter(List<Expense> expenses) {
            mExpenses = expenses;
        }

        @NonNull
        @Override
        public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ExpenseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
            Expense expense = mExpenses.get(position);
            holder.bind(expense);
        }

        @Override
        public int getItemCount() {
            return mExpenses.size();
        }
    }
}
