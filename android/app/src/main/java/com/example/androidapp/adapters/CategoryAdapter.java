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
    public CategoryAdapter(Context context, List<Category> categories, boolean isSingleChoice) {
        super(context, android.R.layout.simple_list_item_multiple_choice, categories);
        this.context = context;
        this.categories = categories;
        this.isSingleChoice = isSingleChoice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // השתמש ב-layout ברירת המחדל עם CheckBox
        if (convertView == null) {
            int layoutResource = isSingleChoice ? android.R.layout.simple_list_item_single_choice : android.R.layout.simple_list_item_multiple_choice;
            convertView = LayoutInflater.from(context).inflate(layoutResource, parent, false);
//            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        }

        Category category = categories.get(position);
        TextView categoryNameTextView = convertView.findViewById(android.R.id.text1);
        categoryNameTextView.setTextColor(Color.WHITE);
        categoryNameTextView.setText(category.getName());

        if (isSingleChoice) {
            CheckBox checkBox = convertView.findViewById(android.R.id.checkbox);

            // אם מצב של בחירה אחת, לא צריך להציג את ה-CheckBox
            checkBox.setVisibility(View.GONE);
        }
        return convertView;
    }
}
