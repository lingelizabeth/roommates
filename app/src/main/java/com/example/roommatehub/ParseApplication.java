package com.example.roommatehub;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application{

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Group.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("38JcH2O8dPOnItWDyvMBusAuI2bSvRGGrVoY2Edp")
                .clientKey("OdRBiP4ZtYcwxUAXhRGTTMgome0qJSA69ToN4olB")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
