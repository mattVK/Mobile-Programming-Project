package com.example.mobile_programming_project;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

public class TransactionHistoryRecyclerAdapter extends RecyclerView.Adapter<TransactionHistoryRecyclerAdapter.ViewHolder> {

    private List<Transaction> dataList;

    TransactionHistoryRecyclerAdapter(List<Transaction> dataList){
            this.dataList = dataList;
    }
    @NonNull
    @Override
    public TransactionHistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_transaction_history_elements, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryRecyclerAdapter.ViewHolder holder, int position) {
        int sourceIcon = getCategoryIconId(position);
        String category = dataList.get(position).category;
        String date = dataList.get(position).date;
        int amount = dataList.get(position).amount;

        String symbol = Objects.equals(dataList.get(position).transactionType, "spent") ? "-" : "+";

        holder.iconView.setImageResource(sourceIcon);
        holder.categoryTextView.setText(category);
        holder.dateTextView.setText(date);
        holder.amountTextView.setText(String.format("%s%s", symbol, formatInteger(amount)));
        holder.amountTextView.setTextColor(Objects.equals(dataList.get(position).transactionType, "spent") ? 0xFFFF5141 : 0xFF00D83C);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void updateData(List<Transaction> newData){
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }


    int getCategoryIconId(int position){
        if (Objects.equals(dataList.get(position).category, "Shopping")){
            return R.drawable.shopping_icon;
        }
        else if(Objects.equals(dataList.get(position).category, "Groceries")){
            return R.drawable.groceries_icon;
        }
        else if (Objects.equals(dataList.get(position).category, "Transportation")){
            return R.drawable.transportation_icon;
        }
        else if (Objects.equals(dataList.get(position).category, "Food & Drinks")){
            return R.drawable.foodndrinks_icon;
        }
        return R.drawable.mbanking_icon;
    }

    private String formatInteger(int value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();



        return numberFormat.format(value).replace(",", ".");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iconView;

        TextView categoryTextView, dateTextView, amountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconView = itemView.findViewById(R.id.iconRecyclerView);
            categoryTextView = itemView.findViewById(R.id.categoryRecyclerView);
            dateTextView = itemView.findViewById(R.id.dateRecyclerView);
            amountTextView = itemView.findViewById(R.id.amountRecyclerView);

        }
    }
}

