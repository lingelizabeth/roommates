package com.example.roommatehub.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.roommatehub.R;
import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.Group;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.parceler.Parcels;
// ...

public class CreateChoreFragment extends DialogFragment {

    public static final String TAG = "CreateChoreFragment";

    private EditText mEditText;
    private Button btnSave;
    private Spinner spinnerChore, spinnerDay;

    public CreateChoreFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CreateChoreFragment newInstance(Group group) {
        CreateChoreFragment frag = new CreateChoreFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", Parcels.wrap(group));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_chore, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get args
        Group group = Parcels.unwrap(getArguments().getParcelable("group"));

        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.etChore);
        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Chore spinner
        spinnerChore = view.findViewById(R.id.spinnerChore);


        spinnerDay = view.findViewById(R.id.spinnerDay);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chore chore = new Chore();
                // Add checks for edit text null
                String choreName = mEditText.getText().toString();
                if(choreName.isEmpty()){
                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                chore.setName(choreName);
                chore.setDayOfWeek(spinnerDay.getSelectedItem().toString());
                chore.setGroup(group);
                chore.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "error saving chore: "+e);
                            return;
                        }
                        Log.i(TAG, "chore saved successfully");
                    }
                });

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    public void queryChoreTypes(){

    }
}