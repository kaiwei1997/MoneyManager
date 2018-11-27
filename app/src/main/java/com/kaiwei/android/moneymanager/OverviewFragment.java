package com.kaiwei.android.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.text.DecimalFormat;
import java.util.List;

public class OverviewFragment extends Fragment {

    private Button mIncomeButton;
    private TextView mIncomeTotal;
    private TextView mExpensesTotal;
    private Button mIncomeDetailButton;

    private RecyclerView mOverviewRecyclerView;

    private OverviewAdapter mOverviewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        mIncomeButton = (Button) v.findViewById(R.id.btn_income);
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

        mIncomeTotal = (TextView) v.findViewById(R.id.tv_incomeAmount);
        updateIncomeTotal();

        mExpensesTotal = (TextView)v.findViewById(R.id.tv_expensesAmount);
        updateExpensesTotal();

        mIncomeDetailButton = (Button) v.findViewById(R.id.btn_incomeDetail);
        mIncomeDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = IncomeListActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        mOverviewRecyclerView = (RecyclerView) v
                .findViewById(R.id.overview_recycler_view);
        mOverviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateOverview();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateIncomeTotal();
        updateOverview();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_overview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.category:
                Intent intent = new Intent(getActivity(), CategoryListActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateIncomeTotal() {
        OverviewLab overviewLab = OverviewLab.get(getActivity());
        Double incomeTotal = overviewLab.getIncomeTotal();

        DecimalFormat precision = new DecimalFormat("RM 0.00");
        mIncomeTotal.setText(precision.format(incomeTotal));
    }

    private void updateExpensesTotal(){
        OverviewLab overviewLab = OverviewLab.get(getActivity());
        Double expensesTotal = overviewLab.getExpensesTotal();

        DecimalFormat precision = new DecimalFormat("RM 0.00");
        mExpensesTotal.setText(precision.format(expensesTotal));
    }

    private void updateOverview() {
        OverviewLab overviewLab = OverviewLab.get(getActivity());
        List<Overview> overviews = overviewLab.getOverview();

        if (mOverviewAdapter == null) {
            mOverviewAdapter = new OverviewAdapter(overviews);
            mOverviewRecyclerView.setAdapter(mOverviewAdapter);
        } else {
            mOverviewAdapter.setOverviews(overviews);
            mOverviewAdapter.notifyDataSetChanged();
        }
    }

    private class OverviewHolder extends RecyclerView.ViewHolder {
        private Overview mOverview;
        private TextView mCategoryTextView;
        private TextView mTotalTextView;

        public OverviewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_item_overview, parent, false));

            mCategoryTextView = (TextView) itemView.findViewById(R.id.tv_categoryName);
            mTotalTextView = (TextView) itemView.findViewById(R.id.tv_total);
        }

        public void bind(Overview overview) {
            mOverview = overview;
            mCategoryTextView.setText(overview.getCategory());
            DecimalFormat precision = new DecimalFormat("RM 0.00");
            mTotalTextView.setText(precision.format(mOverview.getTotal()));
        }
    }


    private class OverviewAdapter extends RecyclerView.Adapter<OverviewHolder> {

        private List<Overview> mOverviews;

        public void setOverviews(List<Overview> overviews) {
            mOverviews = overviews;
        }

        public OverviewAdapter(List<Overview> overviews) {
            mOverviews = overviews;
        }

        @NonNull
        @Override
        public OverviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new OverviewHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull OverviewHolder holder, int position) {
            Overview overview = mOverviews.get(position);
            holder.bind(overview);
        }

        @Override
        public int getItemCount() {
            return mOverviews.size();
        }
    }
}
