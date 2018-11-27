package com.kaiwei.android.moneymanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.UUID;

public class CategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String CATEGORY_TYPE_INCOME = "income";
    private static final String CATEGORY_TYPE_EXPEMSES = "expenses";
    private Category mCategory;
    private EditText mCategoryName;
    private RadioGroup mCategoryTypeRadioGroup;
    private RadioButton mCategoryType;

    public static CategoryFragment newInstance(UUID categoryId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY_ID, categoryId);

        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID categoryId = (UUID) getArguments().getSerializable(ARG_CATEGORY_ID);
        mCategory = CategoryLab.get(getActivity()).getCategory(categoryId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        CategoryLab.get(getActivity())
                .updateCategory(mCategory);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_category, container, false);
        getActivity().setTitle(R.string.category);

        mCategoryName = (EditText) v.findViewById(R.id.et_categoryName);
        mCategoryName.setText(mCategory.getCategoryName());
        mCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCategory.setCategoryName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCategoryTypeRadioGroup = (RadioGroup) v.findViewById(R.id.rg_categoryType);
        mCategoryTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mCategoryType = (RadioButton) v.findViewById(mCategoryTypeRadioGroup.getCheckedRadioButtonId());
                mCategory.setCategoryType(mCategoryType.getText().toString());
            }
        });

        return v;
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
                CategoryLab.get(getActivity()).removeCategory(mCategory);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
