package com.example.task70.Fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.task70.Adapters.ItemAdapter;
import com.example.task70.Database.DbHandler;
import com.example.task70.Models.Item;
import com.example.task70.R;

import java.util.ArrayList;


public class LostAndFoundFragment extends Fragment {

    String[] catergories = new String[]{"Clothing", "Electronics", "Other"};


    Button backButton;
    ArrayList<Item> items = new ArrayList<Item>();
    ArrayList<Item> filterdItems = new ArrayList<Item>();

    RecyclerView itemRecycler;

    Button clothingButton;
    Button electronicsButton;
    Button otherButton;

    public LostAndFoundFragment() {
        // Required empty public constructor
    }

    public static LostAndFoundFragment newInstance(String param1, String param2) {
        LostAndFoundFragment fragment = new LostAndFoundFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost_and_found, container, false);
        backButton = view.findViewById(R.id.back_button);
        itemRecycler = view.findViewById(R.id.item_recycler);
        clothingButton = view.findViewById(R.id.clothing_button);
        electronicsButton = view.findViewById(R.id.electronics_button);
        otherButton = view.findViewById(R.id.other_button);

        SQLiteDatabase dbHandler = new DbHandler(requireContext()).getReadableDatabase();

        String query = "SELECT * FROM " + DbHandler.LostItem.TABLE_NAME;
        Cursor cursor = dbHandler.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_PHONE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_DESCRIPTION));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_LOCATION));
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_IMAGE));
                @SuppressLint("Range") String catergory = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_CATERGORY));
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_ID));
                @SuppressLint("Range") int postType = cursor.getInt(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_POST_TYPE));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_DATE));

                Item item = new Item(name, phoneNumber, description, location, catergory, image, id, postType, date);

                items.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();

        ItemAdapter itemAdapter = new ItemAdapter(requireContext(), items);

        itemRecycler.setAdapter(itemAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);

                navController.popBackStack();
            }
        });

        clothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterdItems = new ArrayList<>();

                for(Item item : items) {
                    if(item.catergory.equals("0")) {
                        filterdItems.add(item);
                    }
                }
                ItemAdapter itemAdapter = new ItemAdapter(requireContext(), filterdItems);
                itemRecycler.setAdapter(itemAdapter);
            }
        });

        electronicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterdItems = new ArrayList<>();

                for(Item item : items) {
                    if(item.catergory.equals("1")) {
                        filterdItems.add(item);
                    }
                }
                ItemAdapter itemAdapter = new ItemAdapter(requireContext(), filterdItems);
                itemRecycler.setAdapter(itemAdapter);
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterdItems = new ArrayList<>();

                for(Item item : items) {
                    if(item.catergory.equals("2")) {
                        filterdItems.add(item);
                    }
                }
                ItemAdapter itemAdapter = new ItemAdapter(requireContext(), filterdItems);
                itemRecycler.setAdapter(itemAdapter);
            }
        });



        return view;
    }
}