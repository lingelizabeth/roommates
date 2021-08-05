package com.example.roommatehub;

import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.ChoreType;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.ListItem;
import com.example.roommatehub.models.Note;
import com.example.roommatehub.models.Notification;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.onesignal.OneSignal;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application{

    private static final String ONESIGNAL_APP_ID = "23d72eac-5987-4d2e-ab52-b9205ea9a655";

    // Initializes Parse SDK and OneSignal plugin as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // Google Places API initialization
        // Initialize the SDK
        String places_api_key = BuildConfig.PLACES_API_KEY;
        Places.initialize(getApplicationContext(), places_api_key);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Parse Initialization
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(Note.class);
        ParseObject.registerSubclass(ListItem.class);
        ParseObject.registerSubclass(Chore.class);
        ParseObject.registerSubclass(ChoreType.class);
        ParseObject.registerSubclass(Notification.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("38JcH2O8dPOnItWDyvMBusAuI2bSvRGGrVoY2Edp")
                .clientKey("OdRBiP4ZtYcwxUAXhRGTTMgome0qJSA69ToN4olB")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
