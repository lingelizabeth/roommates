package com.example.roommatehub.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roommatehub.Helper;
import com.example.roommatehub.R;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public static final String TAG = "UserAdapter";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_TIME_AGO = "locationUpdatedTime";
    public static final String KEY_IMAGE = "profileImage";

    private Context context;
    private List<ParseUser> users;
    private String address;

    public UserAdapter(Context context, List<ParseUser> users, String address) {
        this.context = context;
        this.users = users;
        this.address = address;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvLocation, tvTimeAgo;
        ImageView ivProfileImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvMessage);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivProfileImage = itemView.findViewById(R.id.ivIcon);
        }

        public void bind(ParseUser user){
            tvName.setText(user.get("firstName")+" "+user.get("lastName"));
            Glide.with(context)
                    .load(user.getParseFile(KEY_IMAGE).getUrl())
                    .circleCrop()
                    .into(ivProfileImage);


            // Get last known location if it exists
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            ParseGeoPoint location = (ParseGeoPoint) user.get(KEY_LOCATION);
            if(location != null){
                try{
                    List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        // Check if address is home address
                        Log.i(TAG, addresses.get(0).getAddressLine(0)+" "+address);
                        if(addresses.get(0).getAddressLine(0).equalsIgnoreCase(address)){
                            tvLocation.setText("Last seen at home");
                        }else{
                            String stateAbbrev = "";
                            if(addresses.get(0).getCountryName().equalsIgnoreCase("United States")) {
                                Log.i(TAG, addresses.get(0).getAdminArea());
                                Helper helper = new Helper();
                                stateAbbrev += ", " + helper.states.get(addresses.get(0).getAdminArea());
                            }
                            tvLocation.setText("Last seen in "+addresses.get(0).getLocality()+stateAbbrev);
                        }
                    }
                } catch(IOException e){
                    Log.e(TAG, "Error getting city/state data: "+e);
                }
            }
            // Get last known location relative time ago
            Date locationUpdatedTime = (Date) user.get(KEY_TIME_AGO);
            if(locationUpdatedTime != null){
                tvTimeAgo.setText(Helper.getRelativeTimeAgo(locationUpdatedTime));
            }

        }
    }
}
