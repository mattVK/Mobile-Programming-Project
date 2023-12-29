package com.example.mobile_programming_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DetailedCategoryListAdapter extends BaseAdapter {


    Context context;
    List<String[]> dataList;

    DetailedCategoryListAdapter(Context context, List<String[]> dataList) {
        this.context = context;
        this.dataList = dataList;
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_view_limit_categories_elements, parent, false);
        }


        ImageView iconView = convertView.findViewById(R.id.iconList);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBarList);
        TextView categoriesTextView = convertView.findViewById(R.id.categoryList);
        TextView totalAmountTextView = convertView.findViewById(R.id.totalAmountList);
        TextView totalPercentLimitTextView = convertView.findViewById(R.id.percentageList);

        iconView.setImageResource(getCategoryIconId(position));


        float percent = ((float) Integer.parseInt(dataList.get(position)[1]) / Integer.parseInt(dataList.get(position)[2])) * 100;

        if (percent > 100){
            progressBar.setProgress(progressBar.getMax());
        }else{
            progressBar.setProgress((int)percent);
        }



        categoriesTextView.setText(dataList.get(position)[0]);
        totalAmountTextView.setText(formatInteger(Integer.parseInt(dataList.get(position)[1])));
        totalPercentLimitTextView.setText(String.format(Locale.getDefault(), "%d%%", (int) percent));




        return convertView;
    }



    void updateData(List<String[]> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
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
    private String formatInteger(int value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();



        return numberFormat.format(value).replace(",", ".");
    }
}

