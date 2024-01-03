package com.example.mobile_programming_project;



import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionHistoryRecyclerAdapter extends RecyclerView.Adapter<TransactionHistoryRecyclerAdapter.ViewHolder> {

    private List<Transaction> dataList;

    private Context context;
    private int clickPosition, selectedItemPosition = RecyclerView.NO_POSITION;

    private FinancialDetailsActivity financialDetailsActivity;


    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            SQLDatabase transactionsDB = new SQLDatabase(context);

            transactionsDB.deleteRow(dataList.get(clickPosition).id, dataList.get(clickPosition).transactionType);
            dataList.remove(clickPosition);
            financialDetailsActivity.updateFinancialDetailsUI();
            notifyItemRemoved(clickPosition);
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (selectedItemPosition != RecyclerView.NO_POSITION) {
                // Reset the selected item position
                int previousSelectedItem = selectedItemPosition;
                selectedItemPosition = RecyclerView.NO_POSITION;

                // Notify the adapter that the item has changed
                notifyItemChanged(previousSelectedItem);
            }
        }
    };


    TransactionHistoryRecyclerAdapter(Context context, List<Transaction> dataList, FinancialDetailsActivity activity) {
        this.dataList = dataList;
        this.context = context;
        this.financialDetailsActivity = activity;
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
        Long amount = dataList.get(position).amount;

        String symbol = Objects.equals(dataList.get(position).transactionType, "spent") ? "-" : "+";

        holder.iconView.setImageResource(sourceIcon);
        holder.categoryTextView.setText(category);
        holder.dateTextView.setText(date);
        holder.amountTextView.setText(String.format("%s%s", symbol, formatInteger(amount)));
        holder.amountTextView.setTextColor(Objects.equals(dataList.get(position).transactionType, "spent") ? 0xFFFF5141 : 0xFF00D83C);

        if (position == selectedItemPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.whitefade));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void updateData(List<Transaction> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }


    int getCategoryIconId(int position) {
        if (Objects.equals(dataList.get(position).category, "Shopping")) {
            return R.drawable.shopping_icon;
        } else if (Objects.equals(dataList.get(position).category, "Groceries")) {
            return R.drawable.groceries_icon;
        } else if (Objects.equals(dataList.get(position).category, "Transportation")) {
            return R.drawable.transportation_icon;
        } else if (Objects.equals(dataList.get(position).category, "Food & Drinks")) {
            return R.drawable.foodndrinks_icon;
        }
        return R.drawable.mbanking_icon;
    }

    private String formatInteger(Long value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();


        return numberFormat.format(value).replace(",", ".");
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemLayoutGroup;
        ImageView iconView;

        TextView categoryTextView, dateTextView, amountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconView = itemView.findViewById(R.id.iconRecyclerView);
            categoryTextView = itemView.findViewById(R.id.categoryRecyclerView);
            dateTextView = itemView.findViewById(R.id.dateRecyclerView);
            amountTextView = itemView.findViewById(R.id.amountRecyclerView);
            itemLayoutGroup = itemView.findViewById(R.id.itemLayoutGroup);


            itemLayoutGroup.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    clickPosition = getAdapterPosition();
                    selectedItemPosition = clickPosition;



                    notifyItemChanged(clickPosition);
                    v.startActionMode(actionModeCallbacks);

                    return true;
                }

            });
        }


    }





}