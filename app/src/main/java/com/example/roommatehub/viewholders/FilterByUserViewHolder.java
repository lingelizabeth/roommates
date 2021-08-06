package com.example.roommatehub.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roommatehub.R;
import com.example.roommatehub.interfaces.itemFilterSelectedListener;
import com.example.roommatehub.models.UserIcon;

import java.util.List;

public class FilterByUserViewHolder extends BaseViewHolder<UserIcon> implements View.OnClickListener{

    Context context;
    List<UserIcon> userIcons;
//    List<UserIcon> selectedIcons;
    private ImageView ivProfileImage;
    private itemFilterSelectedListener listener;

    public FilterByUserViewHolder(View itemView, Context context, List<UserIcon> userIcons, itemFilterSelectedListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;
        this.userIcons = userIcons;
//        this.selectedIcons = selectedIcons;
        ivProfileImage = itemView.findViewById(R.id.ivIcon);
        ivProfileImage.setOnClickListener(this);
        ivProfileImage.requestLayout();
        // Set smaller image view
        ivProfileImage.getLayoutParams().height = 120;
        ivProfileImage.getLayoutParams().width = 120;
        // Decrease margins between images
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) ivProfileImage.getLayoutParams();
        marginParams.setMargins(0, 0, 50, 0);
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

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();

        if(position != RecyclerView.NO_POSITION) {
            UserIcon userIcon = userIcons.get(position);
            // Set items to "selected" when clicked and display a "selected" state
            userIcon.setSelected(!userIcon.isSelected());
            ivProfileImage.setBackground(userIcon.isSelected() ?
                    context.getDrawable(R.drawable.blue_circle) :
                    context.getDrawable(R.drawable.ic_baseline_navigate_next_24));
            listener.onitemFilterSelected(UserIcon.getSelectedUsers(userIcons));
        }
    }
}