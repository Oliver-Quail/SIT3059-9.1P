package com.example.task70.Fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task70.Database.DbHandler;
import com.example.task70.Models.Item;
import com.example.task70.R;


public class ViewItemFragment extends Fragment {

    TextView postTypeText;
    ImageView itemImage;
    TextView itemNameText;
    TextView itemDateText;
    TextView itemDescriptionText;
    TextView itemLocationText;
    TextView phoneText;
    Button removeButton;
    Button backButton;

    public ViewItemFragment() {
        // Required empty public constructor
    }

    public static ViewItemFragment newInstance(String param1, String param2) {
        ViewItemFragment fragment = new ViewItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_item, container, false);

        postTypeText = view.findViewById(R.id.post_type_text);
        itemImage = view.findViewById(R.id.item_image);
        itemNameText = view.findViewById(R.id.item_name_text);
        itemDateText = view.findViewById(R.id.item_date_text);
        itemDescriptionText = view.findViewById(R.id.item_description_text);
        itemLocationText = view.findViewById(R.id.item_location_text);
        phoneText = view.findViewById(R.id.phone_text);
        removeButton = view.findViewById(R.id.remove_button);
        backButton = view.findViewById(R.id.back_button);

        int id = getArguments().getInt("id");

        SQLiteDatabase dbHandler = new DbHandler(requireContext()).getReadableDatabase();

        String query = "SELECT * FROM " + DbHandler.LostItem.TABLE_NAME + " WHERE " + DbHandler.LostItem.COLUMN_ID + " = " + String.valueOf(id);
        Cursor cursor = dbHandler.rawQuery(query, null);
        Log.d("temp", String.valueOf(id));

        if(cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_NAME));
            @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_PHONE));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_DESCRIPTION));
            @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_LOCATION));
            @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_IMAGE));
            @SuppressLint("Range") String catergory = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_CATERGORY));
            @SuppressLint("Range") int postType = cursor.getInt(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_POST_TYPE));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DbHandler.LostItem.COLUMN_DATE));

            Item item = new Item(name, phoneNumber, description, location, catergory, image, id, postType, date);

            if(item.postType == 0) {
                postTypeText.setText("Missing item");
            }
            else {
                postTypeText.setText("Missing item");
            }

            itemImage.setImageURI(Uri.parse(item.image));
            itemNameText.setText(item.name);
            itemDateText.setText(item.date);
            itemDescriptionText.setText(item.description);
            itemLocationText.setText(item.location);
            phoneText.setText(item.phoneNumber);



        }
        else {

        }

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = new DbHandler(requireContext()).getWritableDatabase();
                String[] bindArgs = new String[]{String.valueOf(id)};
                db.delete(DbHandler.LostItem.TABLE_NAME, DbHandler.LostItem.COLUMN_ID + " = ? ", bindArgs);
                NavController navController = Navigation.findNavController(view);
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show();

                navController.navigate(R.id.home_fragment);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);

                navController.popBackStack();
            }
        });


        return view;
    }
}