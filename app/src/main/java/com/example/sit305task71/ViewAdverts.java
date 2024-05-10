package com.example.sit305task71;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//This class will be responsible when viewing individual adverts, as well as deleting them
public class ViewAdverts extends AppCompatActivity {
    private Database Database;
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); //Running the code in addition to the saved instance
        setContentView(R.layout.view_adverts); //Setting the content view to 'view_adverts.xml'

        Database = new Database(this);
        int itemId = getIntent().getIntExtra("itemId", -1);
        SQLiteDatabase db = Database.getReadableDatabase(); //Opening the database to read

        String[] dbinfo = //Creating an array of strings 'dbinfo' that contains various information from the database
        {
            Database.COLUMN_NAME,
            Database.COLUMN_DESCRIPTION,
            Database.COLUMN_LOCATION,
            Database.COLUMN_DATE,
            Database.COLUMN_IS_LOST,
            Database.COLUMN_IS_FOUND,
            Database.COLUMN_PHONE

        };

        String selection = Database.COLUMN_ID + " = ?";
        String[] selectionA = {Integer.toString(itemId)};
        Cursor cursor = db.query( //Query will request for data specified below from the database plus the other functions we have created
                Database.TABLE_NAME,
                dbinfo,
                selection,
                selectionA,
                null, null, null
        );

        //Finding the id's of the text boxes/check boxes in the 'view_adverts.xml' file for text view
        TextView nameView = findViewById(R.id.view_name); //For example: this will look for 'view_name' which will be found within 'view_adverts.xml'
        TextView phoneView = findViewById(R.id.view_phone);
        TextView descriptionView = findViewById(R.id.view_description);
        TextView locationView = findViewById(R.id.view_location);
        TextView dateView = findViewById(R.id.view_date);
        TextView statusView = findViewById(R.id.view_is_lost_or_found);

        if (cursor.moveToFirst()) //Moving the cursor to the first result. This can test whether the query returns an empty set or not
        {
            //Getting advert details from the database
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DESCRIPTION));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_LOCATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DATE));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_PHONE));
            int isLost = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_IS_LOST));
            int isFound = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_IS_FOUND));

            //Setting the values below to be displayed
            nameView.setText(name); //For example: 'nameView' was created above, with the value being 'view_name' from 'view_adverts.xml'
            phoneView.setText(phone);
            descriptionView.setText(description);
            locationView.setText(location);
            dateView.setText(date);

            if (isLost == 1) //If the value of 'isLost' is 1, then the status will be changed to 'Lost' in the statusView
            {
                statusView.setText("Status: Lost");
            }
            else if (isFound == 1) //If the value of 'isFound' is 1, then the status will be changed to 'Found' in the statusView
            {
                statusView.setText("Status: Found");
            }
        }


        //Setting up the button to remove an advert after it has been found
        cursor.close();
        Button remove = findViewById(R.id.button_remove); //Finding a view with the id 'button_remove' from the 'view_adverts.xml' file to display as a button
        remove.setOnClickListener(new View.OnClickListener() //Setting an on click listener to 'button_remove'
        {
            @Override public void onClick(View v) //On click, the code below is used
            {
                SQLiteDatabase db = Database.getWritableDatabase(); //Opening the database to write

                int id = getIntent().getIntExtra("itemId", -1); //This is to pass data over to the intent
                String selection = Database.COLUMN_ID + " = ? ";
                String[] selectionA = {Integer.toString(itemId)}; //Creating a new string array 'selectionA' which converts the itemId to a string value

                db.delete(Database.TABLE_NAME, selection, selectionA); //Deleting the advert information from the database
                Database.close(); //Closing the database

                Toast.makeText(ViewAdverts.this, "Item has been removed", Toast.LENGTH_SHORT).show(); //Making a visual confirmation for the user to see in the application

                Intent intent = new Intent(ViewAdverts.this, MainActivity.class); //Creating a new intent which one run, will return the user to the 'MainActivity' class
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Setting a flag which will clear all of the activities on the top of the target activity
                startActivity(intent); //Starting the activity using the intent created above
            }

        });

    }

    @Override protected void onDestroy()
    {
        Database.close(); //Closes the database
        super.onDestroy(); //Dismisses any dialogs the activity was managing
    }
}
