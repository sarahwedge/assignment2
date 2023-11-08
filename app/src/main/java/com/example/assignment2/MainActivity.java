/*
Class: MainActivity.java
Date: Nov 8th 2023
Programmer: Sarah Wedge
Description: This class is the main page of the app. It allows users to input an address and receive its latitude and longitude coordinates.
It also navigates users to the EditEntriesActivity, where they can make changes to the database.
This class is also responsible for reading from the coordinates file and for using the Geocoder class to get the address values.
 */

package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private  DBHandler dbHandler;

    private EditText addressInput;

    private TextView latitudeOutput, longitudeOutput; //fields for showing the coordinates of an address
    private Button submit, edit;

    private ArrayList<Location> locArrayList; //array list holding all of the location objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(MainActivity.this);  //initalize the database handler
        locArrayList = new ArrayList<>();

        //initialize all text fields
        addressInput = findViewById(R.id.addressField);
        latitudeOutput = findViewById(R.id.LatView);
        longitudeOutput = findViewById(R.id.LongView);

        //hide the latitude and longitude output fields
        latitudeOutput.setVisibility(View.INVISIBLE);
        longitudeOutput.setVisibility(View.INVISIBLE);

        //Read all locations in the database and store them in the array list
        locArrayList = dbHandler.readLocations();

        //check if database is already populated
        if(!locArrayList.isEmpty()) {
            for(Location loc : locArrayList) {
                Log.i("location", loc.getId() + " " + loc.getAddress()); //this is for debugging, all addresses will be printed in logcat in the format "id address"
            }
        }

        //call the readFile method so the database can be populated with the 50 addresses from the coordinate pairs and store the location info inside the array list
        else {
            String fileName = "values.txt";
            try {
                readFile(fileName);
                locArrayList = dbHandler.readLocations();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        edit = findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditEntriesActivity.class);
                startActivity(intent); //user can press the edit button to navigate to the EditEntries activity
            }
        });

        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() { // pressing the submit button will display the latitiude and longitude of the address the user entered
            @Override
            public void onClick(View view) {
                String address = addressInput.getText().toString(); // get the address input

                if(address.trim().length() != 0 ) { //check that the address input was not empty
                    String result = dbHandler.getCoordinates(address); // retrieve coordinates for the address from the database
                    if(result != "") { //if the address was found in the database, show the user the result
                        String values[] = result.split(" ");
                        latitudeOutput.setVisibility(View.VISIBLE);
                        longitudeOutput.setVisibility(View.VISIBLE);
                        latitudeOutput.setText(values[0]); //show latitude
                        longitudeOutput.setText(values[1]); //show longitude
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ensure address field is not empty.",Toast.LENGTH_SHORT).show(); //Inform user the address was not inputted
                }
            }
        });

    }

    //Method is used to obtain the address at a set of coordinates
    public String reverseGeo(double latitude, double longitude, int NUM_RESULTS) {
       String results = "false";
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this,
                    Locale.getDefault());
            try {
                List<Address> ls = geocoder.getFromLocation(latitude,
                longitude, NUM_RESULTS); //get the information for those coordinates
                for (Address addr: ls) { //for each address at those coordinates
//                    String name = addr.getFeatureName();
                    String address = addr.getAddressLine(0); //save the first address
//                    String city = addr.getLocality();
//                    String county = addr.getSubAdminArea();
//                    String prov = addr.getAdminArea();
//                    String country = addr.getCountryName();
//                    String postalCode = addr.getPostalCode();
//                    String phone = addr.getPhone();
//                    String url = addr.getUrl();
                    results = address;
                }
            } catch (IOException e) { }

        }
        return results; //return the address
    }

    //Read the coordinates from the values.txt file
    public void readFile(String fileName) throws IOException {
        String[] coords;
        String address = "";

        // Create a FileInputStream and a BufferedReader to read the file
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) { //for every line in the file,
                coords = line.split(", "); //split latitude and longitude
                address = reverseGeo(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 1); //send the coordinates and obtain their address
                if(address != "false") {
                    dbHandler.addNewLocation(address, coords[0], coords[1]); //add this address and its coordinates to the database
                }
            }
        }
    }

}