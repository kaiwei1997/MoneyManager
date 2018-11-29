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
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DebtListFragment extends Fragment {
    private RecyclerView mDebtRecyclerView;
    private DebtAdapter mDebtAdapter;
    private TextView mEmptyTextView;
    private Button mNewDebtButton;

    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.debt_list);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debt_list, container, false);

        mDebtRecyclerView = (RecyclerView) view
                .findViewById(R.id.debt_recycler_view);
        mDebtRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyTextView = (TextView)view.findViewById(R.id.debt_empty_text);
        mNewDebtButton = (Button) view.findViewById(R.id.new_debt_button);
        mNewDebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewDebt();
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
                CreateNewDebt();
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
        DebtLab debtLab = DebtLab.get(getActivity());
        int debtCount = debtLab.getDebt().size();
        String subtitle = getResources().getQuantityString(R.plurals.debt_subtitle_plural, debtCount, debtCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

    private void CreateNewDebt(){
        Debt debt = new Debt();
        DebtLab.get(getActivity()).addDebt(debt);
        Intent intent = DebtPagerActivity.newIntent(getActivity(), debt.getDebtId());
        startActivity(intent);
    }


    private void updateUI() {
        DebtLab debtLab = DebtLab.get(getActivity());
        List<Debt> debts = debtLab.getDebt();

        if(debts.size() == 0){
            mEmptyTextView.setVisibility(View.VISIBLE);
            mNewDebtButton.setVisibility(View.VISIBLE);
        }else{
            mEmptyTextView.setVisibility(View.GONE);
            mNewDebtButton.setVisibility(View.GONE);
        }

        if (mDebtAdapter == null) {
            mDebtAdapter = new DebtAdapter(debts);
            mDebtRecyclerView.setAdapter(mDebtAdapter);
        } else {
            mDebtAdapter.setDebts(debts);
            mDebtAdapter.notifyDataSetChanged();
        }
    }

    private class DebtHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Debt mDebt;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mAmountTextView;
        private ImageView mReturenedImageView;

        public DebtHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_debt, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.debt_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.debt_date);
            mAmountTextView = (TextView) itemView.findViewById(R.id.debt_amount);
            mReturenedImageView = (ImageView)itemView.findViewById(R.id.debt_returned);
        }

        public void bind(Debt debt) {
            mDebt = debt;
            DateFormat df = new SimpleDateFormat("E, MMMM dd, yyyy");
            String formatDate = df.format(mDebt.getDebtDate());
            mTitleTextView.setText(mDebt.getDebtTitle());
            mDateTextView.setText(formatDate);
            DecimalFormat precision = new DecimalFormat("RM 0.00");
            mAmountTextView.setText(precision.format(mDebt.getDebtAmount()));
            mReturenedImageView.setVisibility(debt.isReturned() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = DebtPagerActivity.newIntent(getActivity(), mDebt.getDebtId());
            startActivity(intent);
        }
    }

    private class DebtAdapter extends RecyclerView.Adapter<DebtHolder> {

        private List<Debt> mDebts;

        public void setDebts(List<Debt> debts) {
            mDebts = debts;
        }

        public DebtAdapter(List<Debt> debts) {
            mDebts = debts;
        }

        @NonNull
        @Override
        public DebtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new DebtHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DebtHolder holder, int position) {
            Debt debt = mDebts.get(position);
            holder.bind(debt);
        }

        @Override
        public int getItemCount() {
            return mDebts.size();
        }
    }
}
