package com.example.androidapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.androidapp.entities.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Context context;
    private List<Category> categories;
    private boolean isSingleChoice;

    // Constructor to initialize the adapter with context and categories
    public CategoryAdapter(Context context, List<Category> categories, boolean isSingleChoice) {
        super(context, android.R.layout.simple_list_item_multiple_choice, categories);
        this.context = context;
        this.categories = categories;
        this.isSingleChoice = isSingleChoice;
    }

    // Method to customize the view for each category item in the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int layoutResource = isSingleChoice ? android.R.layout.simple_list_item_single_choice : android.R.layout.simple_list_item_multiple_choice;
            convertView = LayoutInflater.from(context).inflate(layoutResource, parent, false);
        }
        // Get the current category item
        Category category = categories.get(position);
        TextView categoryNameTextView = convertView.findViewById(android.R.id.text1);
        categoryNameTextView.setTextColor(Color.WHITE);
        categoryNameTextView.setText(category.getName());

        // If it's single-choice, hide the checkbox
        if (isSingleChoice) {
            CheckBox checkBox = convertView.findViewById(android.R.id.checkbox);

            checkBox.setVisibility(View.GONE);
        }
        return convertView;
    }
}
