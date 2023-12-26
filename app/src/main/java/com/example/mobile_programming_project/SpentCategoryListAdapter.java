package com.example.mobile_programming_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SpentCategoryListAdapter extends BaseAdapter {

    private Context context;
    private int sumOfAllSpentCategories;

    private List<String[]> dataList;

    public SpentCategoryListAdapter(Context context, List<String[]> dataList, int sumOfAllCategories){
        this.context = context;
        this.dataList = dataList;
        this.sumOfAllSpentCategories = sumOfAllCategories;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_view_main_screen_elements, parent, false);
        };

        ImageView listImageView = convertView.findViewById(R.id.iconList);
        TextView categoryTextView = convertView.findViewById(R.id.categoryList);
        TextView numberOfTransactionsTextView = convertView.findViewById(R.id.numberOfTransactionsList);
        TextView percentTextView = convertView.findViewById(R.id.percentList);
        ProgressBar progressBar = convertView.findViewById(R.id.percentProgressBar);

        listImageView.setImageResource(getCategoryIconId(position));
        categoryTextView.setText(dataList.get(position)[0]);
        numberOfTransactionsTextView.setText(dataList.get(position)[2]);
        int percentOfCategory;
        if (sumOfAllSpentCategories != 0) {
            percentOfCategory = (Integer.parseInt(dataList.get(position)[1]) * 100) / sumOfAllSpentCategories;
        } else {
            percentOfCategory = 0;
        }
        percentTextView.setText(String.format(Locale.getDefault(),"%d%%", percentOfCategory));
        progressBar.setProgress((int) percentOfCategory);

        return convertView;
    }

    int getCategoryIconId(int position){
        if (Objects.equals(dataList.get(position)[0], "Shopping")){
            return R.drawable.shopping_icon;
        }
        else if(Objects.equals(dataList.get(position)[0], "Groceries")){
            return R.drawable.groceries_icon;
        }
        else if (Objects.equals(dataList.get(position)[0], "Transportation")){
            return R.drawable.transportation_icon;
        }
        else if (Objects.equals(dataList.get(position)[0], "Food & Drinks")){
            return R.drawable.foodndrinks_icon;
        }
        return R.drawable.shopping_icon;
    }





}
