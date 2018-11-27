package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kaiwei.android.moneymanager.database.MoneyManagerBaseHelper;

import java.text.DecimalFormat;
import java.util.List;

public class OverviewFragment extends Fragment {

    private Button mIncomeButton;
    private TextView mIncomeTotal;
    private Button mIncomeDetailButton;

    private RecyclerView mOverviewRecyclerView;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private OverviewAdapter mOverviewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mDatabase = new MoneyManagerBaseHelper(mContext).getWritableDatabase();
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

    private void updateIncomeTotal() {
        OverviewLab overviewLab = OverviewLab.get(getActivity());
        Double incomeTotal = overviewLab.getIncomeTotal();

        DecimalFormat precision = new DecimalFormat("RM 0.00");
        mIncomeTotal.setText(precision.format(incomeTotal));
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
