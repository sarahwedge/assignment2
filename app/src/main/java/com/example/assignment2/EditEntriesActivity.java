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

    private EditText address, latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entries);

        spinner = (Spinner)findViewById(R.id.editOptionSelect); //initialize the spinner IR object (IR = interest rate) to the interest rate spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditEntriesActivity.this,
                android.R.layout.simple_spinner_item, options); //set the dropdown options to the interest array

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //set the adapter to generic spinner dropdown item appearance
        spinner.setAdapter(adapter); //link the adapter to the IR spinner
        spinner.setOnItemSelectedListener(this); //recognize when an item is selected from the spinner dropdown options

        address = findViewById(R.id.addressField2);
        latitude = findViewById(R.id.latitudeField);
        longitude = findViewById(R.id.longitudeField);

        address.setVisibility(View.INVISIBLE);
        latitude.setVisibility(View.INVISIBLE);
        longitude.setVisibility(View.INVISIBLE);


        go = findViewById(R.id.editButton);
        go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch(selectedOption) {
                    case "Add Entry":
                        
                        break;
                    case "Update Entry":

                        break;
                    case "Delete Entry":

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
                latitude.setVisibility(View.VISIBLE);
                longitude.setVisibility(View.VISIBLE);
                break;
            case "Update Entry":
            case "Delete Entry":
                address.setVisibility(View.VISIBLE);
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