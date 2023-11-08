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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private  DBHandler dbHandler;

    private EditText addressInput;

    private TextView latitudeOutput, longitudeOutput;
    private Button submit, edit;


    private ArrayList<Location> locArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(MainActivity.this);
        locArrayList = new ArrayList<>();

        addressInput = findViewById(R.id.addressField);
        latitudeOutput = findViewById(R.id.LatView);
        longitudeOutput = findViewById(R.id.LongView);

        latitudeOutput.setVisibility(View.INVISIBLE);
        longitudeOutput.setVisibility(View.INVISIBLE);



        locArrayList = dbHandler.readLocations();

        //check if database is already populated
        if(!locArrayList.isEmpty()) {
            for(Location loc : locArrayList) {
                Log.i("location", loc.getId() + " " + loc.getAddress()); //this is just for debugging
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
                startActivity(intent);
            }
        });

        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressInput.getText().toString();
                String result = dbHandler.getCoordinates(address);
                if(result != "") {
                    String values[] = result.split(" ");
                    latitudeOutput.setVisibility(View.VISIBLE);
                    longitudeOutput.setVisibility(View.VISIBLE);
                    latitudeOutput.setText(values[0]);
                    longitudeOutput.setText(values[1]);
                }
            }
        });

    }

    public String reverseGeo(double latitude, double longitude, int NUM_RESULTS) {
       String results = "false";
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this,
                    Locale.getDefault());
            try {
                List<Address> ls = geocoder.getFromLocation(latitude,
                longitude, NUM_RESULTS);
                for (Address addr: ls) {
                    String name = addr.getFeatureName();
                    String address = addr.getAddressLine(0);
                    String city = addr.getLocality();
                    String county = addr.getSubAdminArea();
                    String prov = addr.getAdminArea();
                    String country = addr.getCountryName();
                    String postalCode = addr.getPostalCode();
                    String phone = addr.getPhone();
                    String url = addr.getUrl();
//                    Log.i("address", address);
                    results = address;
                }
            } catch (IOException e) { }

        }
        return results;
    }

    public void readFile(String fileName) throws IOException {
        String[] coords;
        String address = "";

        // Create a FileInputStream and a BufferedReader to read the file
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                coords = line.split(", ");
                address = reverseGeo(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 1);
                if(address != null) {
                    dbHandler.addNewLocation(address, coords[0], coords[1]);
                }
            }
        }
    }

}