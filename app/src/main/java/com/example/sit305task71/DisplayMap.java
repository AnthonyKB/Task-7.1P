package com.example.sit305task71;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//This class will be responsible for the backbones of the map activities. It will be using Google maps
public class DisplayMap extends AppCompatActivity implements OnMapReadyCallback {

    //Initialising a googlemap and mapview to be used for map activities
    private GoogleMap GoogleMap;
    private MapView Mapview;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); //Running the code in addition to the saved instance
        setContentView(R.layout.show_map); //Setting the content view to 'show_map'
        Mapview = findViewById(R.id.showMap); //Setting mapview to 'showMap' in the 'show_map.xml' file
        Mapview.onCreate(savedInstanceState); //Setting the mapview to the saved instance when the code is run
        Mapview.getMapAsync(this);
    }

    @Override public void onResume()
    {
        super.onResume(); //Dismisses any other dialogues
        Mapview.onResume(); //Resumes the Mapview
    }

    @Override public void onPause()
    {
        super.onPause(); //Dismisses any other dialogues
        Mapview.onPause(); //Pauses the Mapview
    }

    @Override public void onDestroy()
    {
        super.onDestroy(); //Dismisses any other dialogues
        Mapview.onDestroy(); //Stopping the mapview
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        Mapview.onLowMemory();
    }

    @Override public void onMapReady(@Nullable GoogleMap map)
    {
        GoogleMap = map;
        Database Database = new Database(this);
        SQLiteDatabase db = Database.getReadableDatabase(); //Retrieving the database to read
        String[] dbinfo = {Database.COLUMN_LATITUDE, Database.COLUMN_LONGITUDE};
        Cursor cursor = db.query(Database.TABLE_NAME, dbinfo, null, null, null, null, null); //Creating a new cursor

        while (cursor.moveToNext()) //While the list iterates rows
        {
            //Retrieving the longitude and latitude values from the database, converting into double values
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(Database.COLUMN_LONGITUDE));
            LatLng Coordinates = new LatLng(latitude, longitude); //Creating a new LatLng that gets the latitude and longitude
            GoogleMap.addMarker(new MarkerOptions().position(Coordinates)); //Adding a marker on the map using the coordinates determined by the latitude and longitude
        }
        cursor.close(); //Closing the cursor

        LatLng Default = new LatLng(-37.9541, 145.0512); //Setting the default location for the map
        GoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Default, 12f)); //Setting the zoom level and also the location using 'default' created in the line above
    }
}
