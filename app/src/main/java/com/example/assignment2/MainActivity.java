package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public String adds = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int NUM_RESULTS = 3;
        double longitude = -120.37124139999997;
        double latitude = 50.6720143;

        reverseGeo(latitude, longitude, NUM_RESULTS);

        String path = "values.txt";
        String result;
        try {
            readFile(path);
            Log.i("result", adds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




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

    public void geocode(String address, int NUM_RESULTS) {
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            String address = "900 McGill Road";
            try {
                List<Address> ls=
                        geocoder.getFromLocationName(address,NUM_RESULTS);
                for (Address addr: ls) {
                    double latitude = addr.getLatitude();
                    double longitude = addr.getLongitude();
                    Log.i("lat", String.valueOf(latitude));
                    Log.i("long", String.valueOf(longitude));

                }
            } catch (IOException e) {  }
        }
    }

    public void readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        String[] coords;
        List<String> addresses = new ArrayList<>();

        // Create a FileInputStream and a BufferedReader to read the file
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
//                content.append(line).append('\n'); // Read each line and append it to the content
                coords = line.split(", ");
                addresses.add(reverseGeo(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 1));
            }
        }
        for(String add: addresses) {
            adds += add + "\n";

        }
    }
}