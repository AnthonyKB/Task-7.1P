package com.example.sit305task71;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

//This part of the database manages the gathering the values from user input and inserting into the database
//This will be relevant when creating adverts
public class CreateAdvert extends AppCompatActivity
{
    private Database Database;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); //Running the code in addition to the saved instance
        setContentView(R.layout.activity_create); //Setting the content view to activity_create.xml

        Database = new Database(this);

        //Finds a view under id's from the 'activity_create.xml' file to connect the backend code with the UI
        //Final is a non-access modifier which indicates that the value is non-changeable
        final CheckBox isLostCheckBox = findViewById(R.id.isLostCheckBox); //For example, this will look for 'isLostCheckBox' in the 'activity_create.xml' file
        final CheckBox isFoundCheckBox = findViewById(R.id.isFoundCheckBox);
        final EditText nameEdit = findViewById(R.id.nameEdit);
        final EditText phoneEdit = findViewById(R.id.phoneEdit);
        final EditText descriptionEdit = findViewById(R.id.descriptionEdit);
        final EditText locationEdit = findViewById(R.id.locationEdit);
        final Button saveButton= findViewById(R.id.saveButton);


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

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy"); //Setting a new simple date format
                String Date = simpleDateFormat.format(new Date());
                values.put(Database.COLUMN_DATE, Date); //Inserting the date using the new format

                db.insert(Database.TABLE_NAME, null, values); //Inserting the values put into the contentvalues into the database
                Toast.makeText(CreateAdvert.this, "Created successfully", Toast.LENGTH_SHORT).show(); //Displaying a confirmation message to the user
                finish(); //end
            }
        });
    }

    @Override protected void onDestroy()
    {
        Database.close(); //Closes the database
        super.onDestroy();; //Dismisses any dialogs the activity was managing
    }

}
