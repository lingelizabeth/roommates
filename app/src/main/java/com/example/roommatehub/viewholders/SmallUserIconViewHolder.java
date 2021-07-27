package com.example.roommatehub.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.roommatehub.R;
import com.example.roommatehub.models.UserIcon;

public class SmallUserIconViewHolder extends BaseViewHolder<UserIcon> {

    Context context;
    private ImageView ivProfileImage;

    public SmallUserIconViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        ivProfileImage = itemView.findViewById(R.id.ivIcon);
        ivProfileImage.requestLayout();
        // Set smaller image view
        ivProfileImage.getLayoutParams().height = 90;
        ivProfileImage.getLayoutParams().width = 90;
        // Decrease margins between images
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) ivProfileImage.getLayoutParams();
        marginParams.setMargins(0, 0, 30, 0);
        ivProfileImage.setLayoutParams(marginParams);
    }

    @Override
    public void bind(UserIcon userIcon) {
        // Get user profile image or default image
        String url = userIcon.getImageUrl();
        Glide.with(context)
                .load(url)
                .circleCrop()
                .into(ivProfileImage);
    }

}