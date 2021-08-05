package com.example.roommatehub;

import android.util.Log;

import com.example.roommatehub.models.Notification;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.parse.ParseException;
import com.parse.SaveCallback;


import org.json.JSONException;
import org.json.JSONObject;

public class OneSignalNotificationSender {

    public static final String TAG = "OneSignalNotification";

    public static void sendDeviceNotification(final Notification notification) {
        new Thread(() -> {
            OSDeviceState deviceState = OneSignal.getDeviceState();
            String userId = deviceState != null ? deviceState.getUserId() : null;
            boolean isSubscribed = deviceState != null && deviceState.isSubscribed();

            if (!isSubscribed)
                return;

            // TODO: Get player IDs and check if subscribed for all members of group
//            notification.saveParseGroup();
//            String external_id_string = "";
//            try{
//                List<ParseUser> groupMembers = notification.getParseGroup().getMemberList();
//                for(ParseUser member:groupMembers){
//                    external_id_string += "'"+member.getObjectId()+"', ";
//                }
//                external_id_string = external_id_string.substring(0, external_id_string.length()-2);
//                Log.i(TAG, "getting all object IDs for this group: "+external_id_string);
//
//            }catch(ParseException e){
//                Log.e(TAG, "Error getting group members"+e);
//            }

            try {
                JSONObject notificationContent = new JSONObject("{'include_player_ids': ['" + userId + "']," +
                        "'headings': {'en': '" + notification.getTitle() + "'}," +
                        "'contents': {'en': '" + notification.getMessage() + "'}," +
                        "'small_icon': '" + notification.getSmallIconRes() + "'," +
                        "'large_icon': '" + notification.getLargeIconUrl() + "'," +
                        "'android_group': '" + notification.getGroup() + "'," +
                        "'buttons': " + notification.getButtons() + "," +
                        "'android_led_color': 'FFE9444E'," +
                        "'android_accent_color': 'FFE9444E'," +
                        "'android_sound': 'nil'}");

                OneSignal.postNotification(notificationContent, new OneSignal.PostNotificationResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d(TAG, "Success sending notification: " + response.toString());
                        // If successful, add this JSON and save
                        // NotificationData notificationData = new NotificationData();
                        notification.setContent(notificationContent);
                        notification.saveParseGroup();
                        notification.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e != null){
                                    Log.e(TAG, "error saving notification: "+e);
                                    return;
                                }
                                Log.i(TAG, "Notification save successful");
                            }
                        });
                    }

                    @Override
                    public void onFailure(JSONObject response) {
                        Log.e(TAG, "Failure sending notification: " + response.toString());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

}