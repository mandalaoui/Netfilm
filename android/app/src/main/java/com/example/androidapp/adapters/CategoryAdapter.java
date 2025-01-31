package com.example.androidapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.androidapp.entities.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, android.R.layout.simple_list_item_multiple_choice, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // השתמש ב-layout ברירת המחדל עם CheckBox
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        }

        Category category = categories.get(position);
        TextView categoryNameTextView = convertView.findViewById(android.R.id.text1);
        categoryNameTextView.setTextColor(Color.WHITE);
        categoryNameTextView.setText(category.getName());

        return convertView;
    }
}
