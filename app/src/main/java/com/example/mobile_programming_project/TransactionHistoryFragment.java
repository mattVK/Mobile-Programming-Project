package com.example.mobile_programming_project;

import android.database.Cursor;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionHistoryFragment extends Fragment {

    RecyclerView rv;

    int earnedFlag = 0, spentFlag = 0;

    ImageButton earnedButton, spentButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionHistoryFragment newInstance(String param1, String param2) {
        TransactionHistoryFragment fragment = new TransactionHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_transaction_history, container, false);

        earnedButton = view.findViewById(R.id.earnedImageButton);
        spentButton = view.findViewById(R.id.spentImageButton);

        rv = view.findViewById(R.id.transactionHistoryRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider));
        rv.addItemDecoration(dividerItemDecoration);





        TransactionHistoryRecyclerAdapter adapter = new TransactionHistoryRecyclerAdapter(getContext(), getAllTransactions(),(FinancialDetailsActivity) getActivity());
        rv.setAdapter(adapter);


        earnedButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                rv.setVisibility(View.VISIBLE);

                if (earnedFlag == 0){

                    adapter.updateData(getAllBudgetTransactions());
                    earnedFlag = 1;
                } else{
                    adapter.updateData(getAllTransactions());
                    earnedFlag = 0;
                }
                spentFlag = 0;
            }
        });

        spentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (spentFlag == 0){

                    adapter.updateData(getAllSpentTransactions());
                    spentFlag = 1;
                }else{
                    adapter.updateData(getAllTransactions());
                    spentFlag = 0;
                }
                earnedFlag = 0;
            }
        });


        return view;
    }




    List<Transaction> getAllTransactions(){
        List<Transaction> transactionList = new ArrayList<>();
        SQLDatabase transactionsDB = new SQLDatabase(getContext());
        Cursor cursor = transactionsDB.getAllTransactionsSortedByDate();
        if (cursor.getCount() == 0){
            rv.setVisibility(View.INVISIBLE);

        }else{
            while(cursor.moveToNext()){
                transactionList.add(new Transaction(
                        typeOfTransaction(cursor.getString(3)),
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            }
        }
        return transactionList;
    }

    List<Transaction> getAllBudgetTransactions(){
        List<Transaction> transactionList = new ArrayList<>();
        SQLDatabase transactionsDB = new SQLDatabase(getContext());
        Cursor cursor = transactionsDB.getAllBudgetTransactionsSortedByDate();
        if (cursor.getCount() == 0){
            rv.setVisibility(View.INVISIBLE);

        }else{
            while(cursor.moveToNext()){
                transactionList.add(new Transaction(
                        typeOfTransaction(cursor.getString(3)),
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            }
        }
        return transactionList;
    }

    List<Transaction> getAllSpentTransactions(){
        List<Transaction> transactionList = new ArrayList<>();
        SQLDatabase transactionsDB = new SQLDatabase(getContext());
        Cursor cursor = transactionsDB.getAllSpentTransactionsSortedByDate();
        if (cursor.getCount() == 0){
            rv.setVisibility(View.INVISIBLE);

        }else{
            while(cursor.moveToNext()){
                transactionList.add(new Transaction(
                        typeOfTransaction(cursor.getString(3)),
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            }
        }
        return transactionList;
    }



    String typeOfTransaction(String category){
        if (Objects.equals(category, "Food & Drinks") || Objects.equals(category, "Groceries") || Objects.equals(category, "Shopping") || Objects.equals(category, "Transportation")){
            return "spent";
        }
        else{
            return "earned";
        }

    }

}