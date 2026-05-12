package com.example.task70.Fragments;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.task70.Database.DbHandler;
import com.example.task70.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Array;


public class CreateAdvertFragment extends Fragment {

    RadioGroup postTypeRadio;
    EditText nameInput;
    EditText phoneInput;
    EditText descriptionInput;
    EditText dateInput;
    EditText location;
    Spinner catergorySpinner;
    Button imageButton;
    Button saveButton;
    String image;

    String[] catergories = new String[]{"Clothing", "Electronics", "Other"};

    public CreateAdvertFragment() {
        // Required empty public constructor
    }

    public static CreateAdvertFragment newInstance(String param1, String param2) {
        CreateAdvertFragment fragment = new CreateAdvertFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_advert, container, false);

        imageButton = view.findViewById(R.id.image_button);
        postTypeRadio = view.findViewById(R.id.post_type_radio);
        nameInput = view.findViewById(R.id.name_input);
        phoneInput = view.findViewById(R.id.phone_input);
        descriptionInput = view.findViewById(R.id.description_input);
        dateInput = view.findViewById(R.id.date_input);
        location = view.findViewById(R.id.location_input);
        catergorySpinner = view.findViewById(R.id.catergory_spinner);
        saveButton = view.findViewById(R.id.save_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, catergories);
        catergorySpinner.setAdapter(adapter);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(postTypeRadio.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(requireContext(), "Missing post type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image == null) {
                    Toast.makeText(requireContext(), "Missing image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nameInput.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Missing name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phoneInput.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Missing phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(descriptionInput.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Missing description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dateInput.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Missing date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(location.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Missing location", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();

                values.put(DbHandler.LostItem.COLUMN_POST_TYPE, postTypeRadio.getCheckedRadioButtonId());
                values.put(DbHandler.LostItem.COLUMN_IMAGE, image);
                values.put(DbHandler.LostItem.COLUMN_NAME, nameInput.getText().toString());
                values.put(DbHandler.LostItem.COLUMN_PHONE, phoneInput.getText().toString());
                values.put(DbHandler.LostItem.COLUMN_DESCRIPTION, descriptionInput.getText().toString());
                values.put(DbHandler.LostItem.COLUMN_DATE, dateInput.getText().toString());
                values.put(DbHandler.LostItem.COLUMN_LOCATION, location.getText().toString());
                values.put(DbHandler.LostItem.COLUMN_CATERGORY, catergorySpinner.getSelectedItemPosition());

                SQLiteDatabase dbHandler = new DbHandler(requireContext()).getWritableDatabase();

                Toast.makeText(requireContext(), "Advert created", Toast.LENGTH_SHORT).show();

                dbHandler.insert(DbHandler.LostItem.TABLE_NAME, null, values);
                NavController navController = Navigation.findNavController(view);

                navController.popBackStack();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Uri imageSelected = data.getData();

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis() + ".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");

            Context context = requireContext();

            Uri uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            try {
                OutputStream out = context.getContentResolver().openOutputStream(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageSelected);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    image = uri.toString();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }



        }
    }
}