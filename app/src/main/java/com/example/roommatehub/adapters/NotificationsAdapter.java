package com.example.roommatehub.adapters;

import android.content.Context;
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
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.Notification;
import com.parse.Parse;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{
    private Context context;
    private List<Notification> notifications;

    public NotificationsAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvMessage, tvTimeAgo;
        ImageView ivIcon, ivNew;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivNew = itemView.findViewById(R.id.ivNew);
        }
        public void bind(Notification notification){
            // Customize title text to "You" if the notification was created by the current user
            String title = notification.getTitle().replace(ParseUser.getCurrentUser().getUsername(), "You");
            tvTitle.setText(title);
            tvTimeAgo.setText(Helper.getRelativeTimeAgo(notification.getCreatedAt()));

            // for chore creation: message contains a list of all the users this is assigned to, customize for each user
            if(notification.getGroup().equals("Create chore")){
                String notifMessage = notification.getMessage();
                String message;
                if(notifMessage.contains(ParseUser.getCurrentUser().getUsername())){
                    message = "You are assigned this on"+notifMessage.substring(notifMessage.lastIndexOf(" "));
                } else{
                    message = "You are not assigned anything";
                }
                tvMessage.setText(message);
            }else{
                tvMessage.setText(notification.getMessage());
            }

            Glide.with(context)
                    .load(notification.getLargeIconUrl())
                    .circleCrop()
                    .into(ivIcon);

            // If this notification is created after the current user's last view time, show new icon
            try {
                Group group = notification.getParseGroup().fetchIfNeeded();
                JSONObject memberActivity = group.getActivityJSON();
                String dateString = (String) memberActivity.get(ParseUser.getCurrentUser().getObjectId());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date notifDate = notification.getCreatedAt();
                Log.i("NotificationsAdapter", "HomeFragment comparing dates: "+dateFormat.parse(dateString)+" "+notifDate+" "+notifDate.compareTo(dateFormat.parse(dateString)));
                if(notifDate.compareTo(dateFormat.parse(dateString)) > 0){
                    // if notification is created after user's last view
                    ivNew.setVisibility(View.VISIBLE);
                    Log.i("NotificationsAdapter", "HomeFragment "+notification.getTitle()+" is NEW");
                }
            } catch (JSONException | ParseException | com.parse.ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
