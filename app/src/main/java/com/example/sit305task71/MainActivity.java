package com.example.sit305task71;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//The MainActivity class will be responsible for starting the program, and navigating to the other classes
public class MainActivity extends AppCompatActivity
{
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); //Run the code in addition to saved instance
        setContentView(R.layout.activity_main); //Setting the content view to 'activity_main' which contains the 'create advert' and 'show adverts' buttons

        Button btnCreateAdvert = findViewById(R.id.createAdvertButton); //Finding a view with the id 'createAdvertButton' in the 'activity_main.xml' file to display as a button
        btnCreateAdvert.setOnClickListener(new View.OnClickListener() //Setting an on click listener to the 'Create Advert' button on the main page
        {
            @Override public void onClick(View v) //When the button is clicked, the code below is used
            {
                Intent intent = new Intent(MainActivity.this, CreateAdvert.class); //A new intent is created that will run the 'CreateAdvert' class
                startActivity(intent); //Starting the activity using the intent created in the line above
            }
        });

        Button btnShowItems = findViewById(R.id.showItemsButton); //Finding a view with the id 'showItemsButton' in the 'activity_main.xml' file to display as a button
        btnShowItems.setOnClickListener(new View.OnClickListener() //Setting an on click listener to the 'Show Items' button located on the main page
        {
            @Override public void onClick(View v) //When the button is clicked, the code below is used
            {
                Intent intent = new Intent(MainActivity.this, DisplayAdverts.class); //A new intent is created that will run the 'DisplayAdvert' class
                startActivity(intent); //Starting the activity using the intent created in the line above
            }
        });

        Button btnShowMap = findViewById(R.id.showMap); //Finding a view with the id 'showMap' in the 'activity_main.xml' file to display as a button
        btnShowMap.setOnClickListener(new View.OnClickListener() //Setting an on click listener to the 'Show Map' button located on the main page
        {
            @Override public void onClick(View v) { //When the button is clicked, the code below is used
                Intent intent = new Intent(MainActivity.this, DisplayMap.class); //A new intent is created that will run the 'DisplayMap' class
                startActivity(intent); //Starting the activity using the intent created in the line above
            }
        });


    };
}