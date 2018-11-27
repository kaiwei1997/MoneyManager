package com.kaiwei.android.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CategoryPagerActivity extends AppCompatActivity {
    private static final String EXTRA_CATEGORY_ID =
            "com.kaiwei.android.moneymanager.category_id";

    private ViewPager mViewPager;
    private List<Category> mCategories;

    public static Intent newIntent(Context packageContext, UUID categoryID){
        Intent intent = new Intent(packageContext, CategoryPagerActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_pager);

        UUID categoryId = (UUID) getIntent()
        .getSerializableExtra(EXTRA_CATEGORY_ID);

        mViewPager = (ViewPager) findViewById(R.id.category_view_pager);

        mCategories = CategoryLab.get(this).getCategories();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Category category = mCategories.get(i);
                return CategoryFragment.newInstance(category.getCategoryId());
            }

            @Override
            public int getCount() {
                return mCategories.size();
            }
        });

        for(int i =0; i<mCategories.size(); i++){
            if(mCategories.get(i).getCategoryId().equals(categoryId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
