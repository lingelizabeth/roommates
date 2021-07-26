package com.example.roommatehub.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roommatehub.R;
import com.example.roommatehub.models.UserIcon;

import java.util.List;

public class AssignUserViewHolder extends BaseViewHolder<UserIcon> implements View.OnClickListener{

    Context context;
    List<UserIcon> userIcons;
    private ImageView ivProfileImage;

    public AssignUserViewHolder(View itemView, Context context, List<UserIcon> userIcons) {
        super(itemView);
        this.context = context;
        this.userIcons = userIcons;
        ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        ivProfileImage.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();

        if(position != RecyclerView.NO_POSITION){
            UserIcon userIcon = userIcons.get(position);
            // Set items to "selected" when clicked and display a "selected" state
            userIcon.setSelected(!userIcon.isSelected());
            ivProfileImage.setBackground(userIcon.isSelected() ?
                    context.getDrawable(R.drawable.blue_circle) :
                    context.getDrawable(R.drawable.white));
        }
    }
}