package com.example.roommatehub.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roommatehub.Helper;
import com.example.roommatehub.R;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.UserIcon;

import java.text.ParseException;
import java.util.Date;

public class ActivityUserViewHolder extends BaseViewHolder<UserIcon> {

    // Parse attribute keys
    public static final String KEY_TIME_AGO = "locationUpdatedTime";
    public static final String KEY_FIRST_NAME = "firstName";

    Context context;
    private ImageView ivProfileImage;
    private TextView tvTimeAgo, tvFirstName;
    private Group group;

    public ActivityUserViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        this.group = group;
        ivProfileImage = itemView.findViewById(R.id.ivIcon);
        ivProfileImage.requestLayout();
        // Set smaller image view
        ivProfileImage.getLayoutParams().height = 170;
        ivProfileImage.getLayoutParams().width = 170;
        // Decrease margins between images
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) ivProfileImage.getLayoutParams();
        marginParams.setMargins(0, 0, 100, 0);
        ivProfileImage.setLayoutParams(marginParams);

        // Display activity
        tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
        tvFirstName = itemView.findViewById(R.id.tvFirstName);
    }

    @Override
    public void bind(UserIcon userIcon) {
        // Get last known location relative time ago
        Date locationUpdatedTime = (Date) userIcon.getUser().get(KEY_TIME_AGO);
        if(locationUpdatedTime != null){
            tvTimeAgo.setVisibility(View.VISIBLE);
            tvTimeAgo.setText(Helper.getAbbrevTimeAgo(locationUpdatedTime));
        }
        // Name
        tvFirstName.setVisibility(View.VISIBLE);
        tvFirstName.setText((String) userIcon.getUser().get(KEY_FIRST_NAME));

        // Get user profile image or default image
        String url = userIcon.getImageUrl();
        Glide.with(context)
                .load(url)
                .circleCrop()
                .into(ivProfileImage);
    }

}