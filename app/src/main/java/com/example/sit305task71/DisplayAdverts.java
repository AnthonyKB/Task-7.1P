package com.example.sit305task71;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

//This class will be responsible when it comes to displaying the adverts
public class DisplayAdverts extends AppCompatActivity
{
    private Database Database;
    @Override protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState); //Running the code in addition to the saved instance
        setContentView(R.layout.show_adverts); //Setting the content view to 'show_adverts.xml'

        Database = new Database(this);

        ListView listView = findViewById(R.id.list_view); //Creating a new listview with the value of 'list_view' from 'show_adverts.xml'

        ArrayList<String> itemsList = new ArrayList<>(); //Creating a new array list (in string form) 'itemsList'
        final ArrayList<Integer> idList = new ArrayList<>();//Creating a new array list (in integer form) 'idList', with 'final' indicating that the value is final

        SQLiteDatabase db = Database.getReadableDatabase(); //Opening the database to read

        String sortingOrder = Database.COLUMN_DATE + " DESC"; //Initialising 'sortingOrder' which will be the order in which the table is sorted (based on date)
        String[] dbinfo = //Creating an array of strings 'dbinfo' that contains various information from the database
        {
            Database.COLUMN_ID,
            Database.COLUMN_NAME,
            Database.COLUMN_IS_LOST,
            Database.COLUMN_IS_FOUND
        };

        Cursor cursor = db.query( //Query will request for data specified below from the database plus the other functions we have created
                Database.TABLE_NAME,
                dbinfo,
                null, null, null, null,
                sortingOrder
        );

        while (cursor.moveToNext()) //While the list iterates rows
        {
            //It gathers id, name, islost, and isfound values
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME));
            int isLost = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_IS_LOST));
            int isFound = cursor.getInt((cursor.getColumnIndexOrThrow(Database.COLUMN_IS_FOUND)));

            String itemString = "Name: " + name; //Initialising 'itemString', which will be used below to gather islost/isFound value, then adding the string to itemsList
            if (isLost == 1)
            {
                itemString += "\nLost"; //If isLost value is equal to 1, then this text will be added to 'itemString'
            }
            else if (isFound == 1)
            {
                itemString += "\nFound"; //If isFound value is equal to 1, then this text will be added to 'itemString'
            }

            itemsList.add(itemString); //Adding the 'itemString' string to the 'itemsList' array which was created above
            idList.add(id); //Adding the id to 'idList' array that was created above
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsList); //Creating an adapter to display the values in 'itemsList' in a list view
        listView.setAdapter(adapter); //Setting the adapter to 'adapter' that we created in the line above

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = idList.get(position); //Getting the 'idList' at the current position
                Intent intent = new Intent(DisplayAdverts.this, ViewAdverts.class); //Creating a new intent, which consists of the 'DisplayAdverts' and 'ViewAdverts' classes
                intent.putExtra("itemId", itemId);
                startActivity(intent); //Intent is used to start the activity
            }
        });

        cursor.close(); //Closing cursor database tool
    }

    @Override protected void onDestroy()
    {
        Database.close(); //Closes the database
        super.onDestroy(); //Dismisses any dialogs the activity was managing
    }
}


