package com.example.roommatehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommatehub.models.Group;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    public static final String TAG = "CreateActivity";

    EditText etTitle, etDescription, etMembers;
    Button btnAdd, btnSubmit;
    TextView tvMembers;
    AutocompleteSupportFragment autocompleteSupportFragment;

    List<ParseUser> newMembers;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        newMembers = new ArrayList<>();

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etMembers = findViewById(R.id.etMembers);
        tvMembers = findViewById(R.id.tvMembers);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the add button is clicked,
                // the current user should be added to the new Members list
                // if a user with that username exists

                String username = etMembers.getText().toString();
                ParseQuery<ParseUser> query = ParseUser.getQuery(); // ParseQuery.getQuery(ParseUser.class);
                query.whereEqualTo("username", username);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e != null){
                            Log.e(TAG, "Error getting new member username: "+e);
                            return;
                        }
                        if(objects.isEmpty()){
                            // if invalid username display an error
                            Toast.makeText(CreateActivity.this, "A user with this username doesn't exist!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (containsUser(newMembers, objects.get(0))){
                            // if user has already been added, don't add again
                            Toast.makeText(CreateActivity.this, objects.get(0).getUsername()+" has already been added!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // add user to the new members list
                        newMembers.add(objects.get(0));
                        tvMembers.setText(membersListToString(newMembers));
                    }
                });
            }
        });

        // Places autocomplete
        // Initialize the AutocompleteSupportFragment.
        autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return and type of locations to filter
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS_COMPONENTS, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                AddressComponents addressComponents = place.getAddressComponents();
                Log.i(TAG, "Place: " + place.getAddress() + ", " + place.getId());
                // Save place ID into database for this group
                address = place.getAddress();
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        btnSubmit = findViewById(R.id.btnGoToLogin);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When new group is submitted, all the members in the new member
                // list should be added to the group
                // Edit both the group class & the user class for each user

                // Check if title field is empty
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                if(title.isEmpty()) {
                    Toast.makeText(CreateActivity.this, "Title field cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add current user to group
                newMembers.add(ParseUser.getCurrentUser());

                Group group = new Group();
                group.setTitle(title);
                group.setDescription(description);
                group.addMembers(newMembers);
                group.setAddress(address);
                group.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!= null){
                            Log.e(TAG, "Error saving new group: "+e);
                            return;
                        }
                        Toast.makeText(CreateActivity.this, "Group created successfully!", Toast.LENGTH_SHORT).show();

                        // Send group back to MainActivity to display in groups list
                        Intent i = new Intent();
                        i.putExtra("New group", Parcels.wrap(group));
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });
            }
        });
    }

    private String membersListToString(List<ParseUser> members){
        String output = "";
        for(ParseUser member: members){
            output += member.getUsername() + ", ";
        }
        // Remove the extra ", " at the end
        output = output.substring(0, output.length()-2);
        return output;
    }

    // Code from https://stackoverflow.com/questions/27325322/parse-com-android-boolean-on-if-a-user-belongs-to-an-array
    private boolean containsUser(List<ParseUser> list, ParseUser user) {
        for (ParseUser parseUser : list) {
            if (parseUser.hasSameId(user)) return true;
        }
        return false;

    }
}