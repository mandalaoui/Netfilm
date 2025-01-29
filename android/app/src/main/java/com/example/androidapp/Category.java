package com.example.androidapp;

import java.util.List;

public class Category {
    private String _id;
    private List<String> categoryList;
    private String isPromoted;
    private String name ;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name ;
    }

    public void setName(String name ) {
        this.name = name ;
    }

    public String getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(String isPromoted) {
        this.isPromoted = isPromoted;
    }

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
