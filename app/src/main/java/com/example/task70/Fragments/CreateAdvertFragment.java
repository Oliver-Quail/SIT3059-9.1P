package com.example.task70.Fragments;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.task70.Database.DbHandler;
import com.example.task70.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class CreateAdvertFragment extends Fragment {

    RadioGroup postTypeRadio;
    EditText nameInput;
    EditText phoneInput;
    EditText descriptionInput;
    EditText dateInput;
    Spinner catergorySpinner;
    Button imageButton;
    Button saveButton;
    String image;
    LatLng latLng;
    public String location;
    Button getCurrentLocationButton;
    FusedLocationProviderClient fusedLocationClient;


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
        catergorySpinner = view.findViewById(R.id.catergory_spinner);
        saveButton = view.findViewById(R.id.save_button);
        getCurrentLocationButton = view.findViewById(R.id.get_current_location);

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
                if(latLng == null) {
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
                values.put(DbHandler.LostItem.COLUMN_LOCATION, location);
                values.put(DbHandler.LostItem.COLUMN_CATERGORY, catergorySpinner.getSelectedItemPosition());
                values.put(DbHandler.LostItem.COLUMN_LOGITUDE, latLng.longitude);
                values.put(DbHandler.LostItem.COLUMN_LATITUDE, latLng.latitude);


                SQLiteDatabase dbHandler = new DbHandler(requireContext()).getWritableDatabase();

                Toast.makeText(requireContext(), "Advert created", Toast.LENGTH_SHORT).show();

                dbHandler.insert(DbHandler.LostItem.TABLE_NAME, null, values);
                NavController navController = Navigation.findNavController(view);

                navController.popBackStack();
            }
        });


        if(!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key));
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if(autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(
                    Place.Field.ID,
                    Place.Field.LOCATION,
                    Place.Field.FORMATTED_ADDRESS
            ));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i("SearchFragment", "Place: " + place.getFormattedAddress() + ", " + place.getLocation().toString());
                    latLng = place.getLocation();
                    location = place.getFormattedAddress();
                }

                @Override
                public void onError(@NonNull Status status) {
                }
            });

            getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                        return;
                    }
                    CancellationTokenSource cts = new CancellationTokenSource();
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.getToken()).addOnSuccessListener(getActivity(), location1 -> {
                                if (location1 != null) {
                                    double latitude = location1.getLatitude();
                                    double longitude = location1.getLongitude();
                                    LatLng latLng1 = new LatLng(latitude, longitude);

                                    latLng = latLng1;
                                    location = "Current location";
                                }
                            });
                }
            });
        }


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