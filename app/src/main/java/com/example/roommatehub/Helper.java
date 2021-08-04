package com.example.roommatehub;

import android.text.format.DateUtils;
import android.util.Log;

import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {
    public static final String TAG = "Helper";
    public static Map<String, String> states;

    public Helper() {
        states = new HashMap<String, String>();
        states.put("Alabama", "AL");
        states.put("Alaska", "AK");
        states.put("Alberta", "AB");
        states.put("American Samoa", "AS");
        states.put("Arizona", "AZ");
        states.put("Arkansas", "AR");
        states.put("Armed Forces (AE)", "AE");
        states.put("Armed Forces Americas", "AA");
        states.put("Armed Forces Pacific", "AP");
        states.put("British Columbia", "BC");
        states.put("California", "CA");
        states.put("Colorado", "CO");
        states.put("Connecticut", "CT");
        states.put("Delaware", "DE");
        states.put("District Of Columbia", "DC");
        states.put("Florida", "FL");
        states.put("Georgia", "GA");
        states.put("Guam", "GU");
        states.put("Hawaii", "HI");
        states.put("Idaho", "ID");
        states.put("Illinois", "IL");
        states.put("Indiana", "IN");
        states.put("Iowa", "IA");
        states.put("Kansas", "KS");
        states.put("Kentucky", "KY");
        states.put("Louisiana", "LA");
        states.put("Maine", "ME");
        states.put("Manitoba", "MB");
        states.put("Maryland", "MD");
        states.put("Massachusetts", "MA");
        states.put("Michigan", "MI");
        states.put("Minnesota", "MN");
        states.put("Mississippi", "MS");
        states.put("Missouri", "MO");
        states.put("Montana", "MT");
        states.put("Nebraska", "NE");
        states.put("Nevada", "NV");
        states.put("New Brunswick", "NB");
        states.put("New Hampshire", "NH");
        states.put("New Jersey", "NJ");
        states.put("New Mexico", "NM");
        states.put("New York", "NY");
        states.put("Newfoundland", "NF");
        states.put("North Carolina", "NC");
        states.put("North Dakota", "ND");
        states.put("Northwest Territories", "NT");
        states.put("Nova Scotia", "NS");
        states.put("Nunavut", "NU");
        states.put("Ohio", "OH");
        states.put("Oklahoma", "OK");
        states.put("Ontario", "ON");
        states.put("Oregon", "OR");
        states.put("Pennsylvania", "PA");
        states.put("Prince Edward Island", "PE");
        states.put("Puerto Rico", "PR");
        states.put("Quebec", "QC");
        states.put("Rhode Island", "RI");
        states.put("Saskatchewan", "SK");
        states.put("South Carolina", "SC");
        states.put("South Dakota", "SD");
        states.put("Tennessee", "TN");
        states.put("Texas", "TX");
        states.put("Utah", "UT");
        states.put("Vermont", "VT");
        states.put("Virgin Islands", "VI");
        states.put("Virginia", "VA");
        states.put("Washington", "WA");
        states.put("West Virginia", "WV");
        states.put("Wisconsin", "WI");
        states.put("Wyoming", "WY");
        states.put("Yukon Territory", "YT");
    }

    public static String getRelativeTimeAgo(Date date) {
        String relativeDate = "";
        long dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }

    public static int getProgressOnDay(String day, Group group){
        ParseQuery query = ParseQuery.getQuery(Chore.class);
        // Query chores for given group
        query.whereEqualTo(Chore.KEY_GROUP, group);
        // Query chores for the current tab/day of the week
        query.whereEqualTo(Chore.KEY_DAY, day);
        try {
            List<Chore> chores = query.find();
            // Recalculate and update progress bar
            int choresDone = 0;
            for(Chore chore: chores){
                if(chore.isChecked())
                    choresDone++;
            }
            int progress = (int) Math.round(choresDone * 1.0 / chores.size()*100);
            Log.i(TAG, progress+"% progress on "+day);
            return progress;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Copied from https://stackoverflow.com/questions/1086123/is-there-a-method-for-string-conversion-to-title-case
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}





