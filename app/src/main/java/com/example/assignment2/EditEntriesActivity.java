package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditEntriesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private static final String[] options = {"Add Entry", "Update Entry", "Delete Entry"};
    private String selectedOption = "none"; //entered option
    private Button go;
private DBHandler dbHandler;
    private EditText address, newAddress, latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entries);

        dbHandler = new DBHandler(getApplicationContext());

        spinner = (Spinner)findViewById(R.id.editOptionSelect); //initialize the spinner IR object (IR = interest rate) to the interest rate spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditEntriesActivity.this,
                android.R.layout.simple_spinner_item, options); //set the dropdown options to the interest array

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //set the adapter to generic spinner dropdown item appearance
        spinner.setAdapter(adapter); //link the adapter to the IR spinner
        spinner.setOnItemSelectedListener(this); //recognize when an item is selected from the spinner dropdown options

        address = findViewById(R.id.addressField2);
        newAddress = findViewById(R.id.newAddressField);
        latitude = findViewById(R.id.latitudeField);
        longitude = findViewById(R.id.longitudeField);

        address.setVisibility(View.INVISIBLE);
        newAddress.setVisibility(View.INVISIBLE);
        latitude.setVisibility(View.INVISIBLE);
        longitude.setVisibility(View.INVISIBLE);


        go = findViewById(R.id.editButton);
        go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String a, na;
                switch(selectedOption) {
                    case "Add Entry":
                        a = String.valueOf(address.getText());

                        try{
                            Double lat = Double.parseDouble(String.valueOf(latitude.getText()));
                            Double lon = Double.parseDouble(String.valueOf(longitude.getText()));
                        }
                        catch(NumberFormatException nfe) {
                            Toast.makeText(getApplicationContext(), "Ensure Latitude and Longitude follow a numerical decimal format.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        catch(NullPointerException npe) {
                            Toast.makeText(getApplicationContext(), "Ensure Latitude and Longitude fields are not empty.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(a.trim().length() != 0) {
                            dbHandler.addNewLocation(a, String.valueOf(latitude.getText()), String.valueOf(longitude.getText()));
                            Toast.makeText(getApplicationContext(),"Entry has been added.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Ensure address field is not empty.",Toast.LENGTH_SHORT).show();
                        }
                        break;


                    case "Update Entry":
                            a = String.valueOf(address.getText());
                            na = String.valueOf(newAddress.getText());

                        if(a.trim().length() != 0 && na.trim().length() != 0) {

                                if(dbHandler.updateLocation(a, na)) {
                                    Toast.makeText(getApplicationContext(),"Entry has been updated.",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Entry does not exist.",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Ensure both address fields are not empty.",Toast.LENGTH_SHORT).show();
                            }
                            break;

                    case "Delete Entry":
                        a = String.valueOf(address.getText());
                        if(a.trim().length() != 0) {
                            if(dbHandler.removeLocation(a)) {
                                Toast.makeText(getApplicationContext(),"Entry has been deleted.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Entry does not exist.",Toast.LENGTH_SHORT).show();
                            }
                        }
                       else {
                            Toast.makeText(getApplicationContext(),"Ensure address field is not empty.",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Please Select an Option.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedOption = parent.getItemAtPosition(position).toString(); //get the currently selected dropdown item
        switch(selectedOption) {
            case "Add Entry":
                address.setVisibility(View.VISIBLE);
                newAddress.setVisibility(View.INVISIBLE);
                latitude.setVisibility(View.VISIBLE);
                longitude.setVisibility(View.VISIBLE);
                break;
            case "Update Entry":
                address.setVisibility(View.VISIBLE);
                newAddress.setVisibility(View.VISIBLE);
                latitude.setVisibility(View.INVISIBLE);
                longitude.setVisibility(View.INVISIBLE);
                break;
            case "Delete Entry":
                address.setVisibility(View.VISIBLE);
                newAddress.setVisibility(View.INVISIBLE);
                latitude.setVisibility(View.INVISIBLE);
                longitude.setVisibility(View.INVISIBLE);
                break;
            default:
                Toast.makeText(getApplicationContext(),"Please Select an Option.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(),"Please Select an Option.",Toast.LENGTH_SHORT).show();
    }
}