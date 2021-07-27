package com.example.roommatehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roommatehub.R;
import com.example.roommatehub.models.Notification;

import org.jetbrains.annotations.NotNull;

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
        ImageView ivIcon;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
        public void bind(Notification notification){
            tvTitle.setText(notification.getTitle());
            tvMessage.setText(notification.getMessage());

            Glide.with(context)
                    .load(notification.getIconUrl())
                    .circleCrop()
                    .into(ivIcon);
        }
    }
}
