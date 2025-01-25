package com.example.androidapp;

import java.util.List;

public class Category {
    private List<String> categoryList;

    // Constructor
    public Category(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    // Getter
    public List<String> getCategoryList() {
        return categoryList;
    }

    // Setter
    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
