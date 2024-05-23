package com.example.sit305task71;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.CheckBox;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

//This part of the database manages the gathering the values from user input and inserting into the database
//This will be relevant when creating adverts
public class CreateAdvert extends AppCompatActivity
{
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private Database Database;
    private FusedLocationProviderClient locationClient;

    private CheckBox isLostCheckBox;
    private CheckBox isFoundCheckBox;
    private EditText nameEdit;
    private EditText phoneEdit;
    private EditText descriptionEdit;
    private EditText locationEdit;
    private Button saveButton;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); //Running the code in addition to the saved instance
        setContentView(R.layout.activity_create); //Setting the content view to activity_create.xml

        Database = new Database(this);

        //Finds a view under id's from the 'activity_create.xml' file to connect the backend code with the UI
        //Final is a non-access modifier which indicates that the value is non-changeable
        isLostCheckBox = findViewById(R.id.isLostCheckBox); //For example, this will look for 'isLostCheckBox' in the 'activity_create.xml' file
        isFoundCheckBox = findViewById(R.id.isFoundCheckBox);
        nameEdit = findViewById(R.id.nameEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        locationEdit = findViewById(R.id.locationEdit);
        saveButton = findViewById(R.id.saveButton);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        final Button locationButton = findViewById(R.id.currentLocation); //Finding the view by id 'currentLocation' for view in the advert create page

        Places.initialize(getApplicationContext(), "AIzaSyBTO6pLwVGMa3fUTfhhnn3kOV6XHcRRF8o"); //Initialising the location using a google maps API key

        locationEdit.setFocusable(false); //Setting the focusable state to false
        locationEdit.setClickable(true); //Setting the focusable state to true
        locationEdit.setOnClickListener(new View.OnClickListener() //Setting an on click listener to the 'locationEdit' button
        {
            @Override public void onClick(View view) { //On click, the code below runs
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(CreateAdvert.this); //Creating a new builder for an intent to launch the autocomplete activity
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE); //Starting the activity using the intent but also to receive a result back
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() //Setting an on click listener to the 'get current location' button
        {
            @Override public void onClick(View view) //On click, the code below runs
            {
                if (checkLocationPermission()) //Checks for permission
                {
                    getCurrentLocation(); //If permission is granted, then the it will return the current location
                }
                else
                {
                    //If not, it will request the location permission
                    requestLocationPermission();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() //Setting an on click listener to the save button (when you finish filling out information)
        {
            @Override public void onClick(View view)
            {
                SQLiteDatabase db = Database.getReadableDatabase(); //Opening the database to write in
                ContentValues values = new ContentValues(); //Creating a new contentvalues, which will be used to store values

                //These are the values that will be inserted into the database once the save button is clicked
                //It will use the contentvalues store which was created above
                values.put(Database.COLUMN_IS_LOST, isLostCheckBox.isChecked()?1:0);
                values.put(Database.COLUMN_IS_FOUND, isFoundCheckBox.isChecked()?1:0);
                values.put(Database.COLUMN_NAME, nameEdit.getText().toString()); //For example: COLUMN_NAME value will be determined by the 'nameEdit' value input from the user
                                                                                 //It will then be stored as a string
                values.put(Database.COLUMN_PHONE, phoneEdit.getText().toString());
                values.put(Database.COLUMN_DESCRIPTION, descriptionEdit.getText().toString());
                values.put(Database.COLUMN_LOCATION, locationEdit.getText().toString());

                //Setting the values of longitude and latitude
                double longitude = 0.0;
                double latitude = 0.0;
                String address = locationEdit.getText().toString(); //creating 'address' which will get the text from the 'locationEdit' text box

                if (!address.isEmpty()) //If 'locationEdit' text box is not empty
                {
                    Geocoder Geocoder = new Geocoder(CreateAdvert.this, Locale.getDefault()); //Creating a new geocoder

                    try
                    {
                        List<Address> addresses = Geocoder.getFromLocationName(address, 1); //Return an array of addresses that describe the named location

                        if (!addresses.isEmpty()) //If 'locationEdit' text box is not empty
                        {
                            Address location = addresses.get(0);
                            longitude = location.getLongitude(); //Get the longitude
                            latitude = location.getLatitude(); //Get the latitude
                        }
                    }
                    catch (IOException e) //Error catching
                    {
                        e.printStackTrace();
                    }
                }

                values.put(Database.COLUMN_LONGITUDE, longitude); //The new value of longitude defined above will be replaced in the database
                values.put(Database.COLUMN_LATITUDE, latitude); //The new value of latitude defined above will be replaced in the database

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy"); //Setting a new simple date format
                String Date = simpleDateFormat.format(new Date());
                values.put(Database.COLUMN_DATE, Date); //Inserting the date using the new format

                db.insert(Database.TABLE_NAME, null, values); //Inserting the values put into the contentvalues into the database
                Toast.makeText(CreateAdvert.this, "Created successfully", Toast.LENGTH_SHORT).show(); //Displaying a confirmation message to the user
                finish(); //end
            }
        });
    }





    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data); //Starting the activity with the intent in order to start another activity/receive results back
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getAddress(); //'address' returns the location in String value
                locationEdit.setText(address); //Setting the 'locationEdit' text to the value of 'address'
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) //Essentially error catching
            {
                Toast.makeText(CreateAdvert.this, "Error: " + Autocomplete.getStatusFromIntent(data).getStatusMessage(), Toast.LENGTH_SHORT).show(); //Displaying an error message to the user
            }
        }
    }

    private boolean checkLocationPermission() //Handling of location permission
    {
        int permResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION); //Returns whether the permission is granted or denied
        return permResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() //Handling of request of location permission
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE); //Requesting permissions
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); //Callback for the result from requesting permissions

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation(); //Get the current location using the 'getCurrentLocation' function
            }
            else
            {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show(); //Message to the user
            }
        }
    }

    private void getCurrentLocation()
    {
        if (checkLocationPermission())
        {
            locationClient.getLastLocation() //Getting the last location using the FusedLocationProviderClient
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() //Adding an addOnSuccessListener which calls when the task is completed successfully
                    {
                        @Override public void onSuccess(Location location)
                        {
                            if (location != null) //If the value of 'location' is not null
                            {
                                double longitude = location.getLongitude();// Getting the longitude in double value
                                double latitude = location.getLatitude(); //Getting the latitude in double value
                                getAddressFromLocation(latitude, longitude); //Getting the address from location using the new longitude and latitude values
                            }
                            else
                            {
                                Toast.makeText(CreateAdvert.this, "Current location unavailable", Toast.LENGTH_SHORT).show(); //Message to the user
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show(); //Message to the user
        }
    }

    private void getAddressFromLocation(double latitude, double longitude)
    {
        Geocoder Geocoder = new Geocoder(this, Locale.getDefault()); //Creating a new geocoder
        try
        {
            List<Address> addresses = Geocoder.getFromLocation(latitude, longitude, 1); //Return an array of addresses that describe the named location using the longitude and latitude
            if (!addresses.isEmpty()) //If the 'addresses' is not empty
            {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0); //'addressLine' returns the address as a string
                locationEdit.setText(addressLine); //Editing the text in the 'locationEdit' text box to the value of 'addressLine' set above
            }
        }
        catch (IOException e) //Error catching
        {
            e.printStackTrace();
        }
    }

    @Override protected void onDestroy()
    {
        Database.close(); //Closes the database
        super.onDestroy();; //Dismisses any dialogs the activity was managing
    }
}
