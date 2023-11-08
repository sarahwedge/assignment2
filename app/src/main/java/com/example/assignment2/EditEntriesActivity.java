/*
Class: EditEntriesActivity.java
Date: Nov 8th 2023
Programmer: Sarah Wedge
Description: This class allows the user to interact with the database. Database entries can be added, updated, or deleted.
Users can input the required information in the text fields and the results of their action will be shown as an alert.
 */


package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private static final String[] options = {"Add Entry", "Update Entry", "Delete Entry"}; //all database interaction options
    private String selectedOption = "none"; //entered option
    private Button go, back;
private DBHandler dbHandler;
    private EditText address, newAddress, latitude, longitude; //text field inputs required for database changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entries);

        dbHandler = new DBHandler(getApplicationContext());

        spinner = (Spinner)findViewById(R.id.editOptionSelect); //initialize the spinner object
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditEntriesActivity.this,
                android.R.layout.simple_spinner_item, options); //set the dropdown options to the 'options' array

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //set the adapter to generic spinner dropdown item appearance
        spinner.setAdapter(adapter); //link the adapter to the spinner
        spinner.setOnItemSelectedListener(this); //recognize when an item is selected from the spinner dropdown options

        //Initialize all text fields
        address = findViewById(R.id.addressField2);
        newAddress = findViewById(R.id.newAddressField);
        latitude = findViewById(R.id.latitudeField);
        longitude = findViewById(R.id.longitudeField);

        //Set text fields to invisible by default
        address.setVisibility(View.INVISIBLE);
        newAddress.setVisibility(View.INVISIBLE);
        latitude.setVisibility(View.INVISIBLE);
        longitude.setVisibility(View.INVISIBLE);

        //Initialize back button for returning to main page
        back = findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEntriesActivity.this, MainActivity.class);
                startActivity(intent); //go back to main page
            }
        });

        //initialize go button for submiting database changes
        go = findViewById(R.id.editButton);
        go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String a, na;
                switch(selectedOption) { //read the selected option from the spinner

                    case "Add Entry": //User wants to add an entry to the database
                        a = String.valueOf(address.getText()); //get the entered address

                        try{ //check that the latitude and longitude inputs are numeric and in decimal format
                            Double lat = Double.parseDouble(String.valueOf(latitude.getText()));
                            Double lon = Double.parseDouble(String.valueOf(longitude.getText()));
                        }
                        catch(NumberFormatException nfe) { //At least one value was not numeric
                            //Inform the user of their error
                            Toast.makeText(getApplicationContext(), "Ensure Latitude and Longitude follow a numerical decimal format.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        catch(NullPointerException npe) { //At least one field was blank
                            //Inform the user of their error
                            Toast.makeText(getApplicationContext(), "Ensure Latitude and Longitude fields are not empty.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(a.trim().length() != 0) { //Verify the address field was not empty
                            //Add a new database location entry using the user's input
                            dbHandler.addNewLocation(a, String.valueOf(latitude.getText()), String.valueOf(longitude.getText()));
                            Toast.makeText(getApplicationContext(),"Entry has been added.",Toast.LENGTH_SHORT).show(); //inform user action was successful
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Ensure address field is not empty.",Toast.LENGTH_SHORT).show(); //inform user action was not successful
                        }
                        break;


                    case "Update Entry": //User wants to edit an existing entry
                            a = String.valueOf(address.getText()); //Obtain existing address input
                            na = String.valueOf(newAddress.getText()); //Obtain new address input

                        if(a.trim().length() != 0 && na.trim().length() != 0) { //Check that an address is not empty

                                if(dbHandler.updateLocation(a, na)) { //update the address in the database
                                    Toast.makeText(getApplicationContext(),"Entry has been updated.",Toast.LENGTH_SHORT).show(); //inform user entry was updated
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Entry does not exist.",Toast.LENGTH_SHORT).show(); //inform user the address did not exist
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Ensure both address fields are not empty.",Toast.LENGTH_SHORT).show(); //inform user address fields were empty
                            }
                            break;

                    case "Delete Entry": //User wants to delete an entry
                        a = String.valueOf(address.getText()); //obtain address input
                        if(a.trim().length() != 0) { //check it was not empty
                            if(dbHandler.removeLocation(a)) { //try removing entry
                                Toast.makeText(getApplicationContext(),"Entry has been deleted.",Toast.LENGTH_SHORT).show(); //inform user the entry was deleted
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Entry does not exist.",Toast.LENGTH_SHORT).show(); //inform user the entry was not in database
                            }
                        }
                       else {
                            Toast.makeText(getApplicationContext(),"Ensure address field is not empty.",Toast.LENGTH_SHORT).show(); //inform user input was invalid
                        }
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Please Select an Option.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Set the selected input option and show the necessary fields
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedOption = parent.getItemAtPosition(position).toString(); //get the currently selected dropdown item
        switch(selectedOption) {
            case "Add Entry":
                //show all input fields required
                address.setVisibility(View.VISIBLE);
                newAddress.setVisibility(View.INVISIBLE);
                latitude.setVisibility(View.VISIBLE);
                longitude.setVisibility(View.VISIBLE);
                break;
            case "Update Entry":
                //show all input fields required
                address.setVisibility(View.VISIBLE);
                newAddress.setVisibility(View.VISIBLE);
                latitude.setVisibility(View.INVISIBLE);
                longitude.setVisibility(View.INVISIBLE);
                break;
            case "Delete Entry":
                //show all input fields required
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