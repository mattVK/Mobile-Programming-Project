package com.example.mobile_programming_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        ImageButton imageButton = convertView.findViewById(R.id.editButtonList);


        imageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showEditDialog(position);
            }
        });


        iconView.setImageResource(getCategoryIconId(position));


        float percent = ((float) Integer.parseInt(dataList.get(position)[1]) / Integer.parseInt(dataList.get(position)[2])) * 100;

        if (percent > 100){
            progressBar.setProgress(progressBar.getMax());
        }else{
            progressBar.setProgress((int)percent);
        }



        categoriesTextView.setText(dataList.get(position)[0]);
        totalAmountTextView.setText(formatInteger(Integer.parseInt(dataList.get(position)[1])));
        totalPercentLimitTextView.setText(formatInteger(Integer.parseInt(dataList.get(position)[2])));




        return convertView;
    }




    void updateData(List<String[]> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }

    void showEditDialog(int position){


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Limit");

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText editText = new EditText(context);

        layout.addView(editText);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String enteredText = editText.getText().toString().trim();
                if (enteredText.equals("")){
                    dialog.dismiss();
                }else{
                    if (Integer.parseInt(enteredText) < 0){
                        dialog.dismiss();
                    }
                    else{
                        updateLimit(position, Integer.parseInt(enteredText));
                    }
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    void updateLimit(Integer position, Integer limitChange){
        dataList.set(position, new String[]{dataList.get(position)[0], dataList.get(position)[1], String.valueOf(limitChange)});
        SQLDatabase transactionsDB = new SQLDatabase(context);
        transactionsDB.updateLimitOfCategory(dataList.get(position)[0], limitChange);
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

