package com.kaiwei.android.moneymanager;

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

import java.util.List;

public class CategoryListFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private CategoryAdapter mCategoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_list, container, false);

        mCategoryRecyclerView = (RecyclerView) v.findViewById(R.id.category_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        List<Category> categories = categoryLab.getCategories();

        if(mCategoryAdapter == null){
            mCategoryAdapter = new CategoryAdapter(categories);
            mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        }else{
            mCategoryAdapter.setCategories(categories);
            mCategoryAdapter.notifyDataSetChanged();
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Category mCategory;
        private TextView mCategoryNameTextView;
        private TextView mCategoryTypeTextView;

        public CategoryHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_category, parent, false));
            itemView.setOnClickListener(this);

            mCategoryNameTextView = (TextView)itemView.findViewById(R.id.tv_categoryName);
            mCategoryTypeTextView = (TextView)itemView.findViewById(R.id.tv_categoryType);
        }

        public void bind(Category category){
            mCategory = category;
            mCategoryTypeTextView.setText(mCategory.getCategoryName());
            mCategoryTypeTextView.setText(mCategory.getCategoryType());
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> mCategories;

        public void setCategories(List<Category> categories){mCategories = categories;}
        public CategoryAdapter(List<Category> categories){
            mCategories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CategoryHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder categoryHolder, int i) {
            Category category = mCategories.get(i);
            categoryHolder.bind(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }
    }
}
