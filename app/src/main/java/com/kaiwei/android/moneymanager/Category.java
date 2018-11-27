package com.kaiwei.android.moneymanager;

import java.util.UUID;

public class Category {

    private UUID mCategoryId;
    private String categoryName;
    private String categoryType;

    public Category() {
        this(UUID.randomUUID());
    }

    public Category(UUID id){
        mCategoryId = id;
    }

    public UUID getCategoryId() {
        return mCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
