package com.example.roommatehub.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roommatehub.R;
import com.example.roommatehub.interfaces.itemFilterSelectedListener;
import com.example.roommatehub.models.UserIcon;
import com.example.roommatehub.viewholders.AssignUserViewHolder;
import com.example.roommatehub.viewholders.BaseViewHolder;
import com.example.roommatehub.viewholders.FilterByUserViewHolder;
import com.example.roommatehub.viewholders.SmallUserIconViewHolder;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserIconAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private List<UserIcon> userIcons;
    private itemFilterSelectedListener listener;

    private static final int TYPE_SMALL = 0;
    private static final int TYPE_FILTER = 1;
    private static final int TYPE_ASSIGN = 2;

    public UserIconAdapter(Context context, List<UserIcon> userIcons) {
        this.context = context;
        this.userIcons = userIcons;
    }

    public UserIconAdapter(Context context, List<UserIcon> userIcons, itemFilterSelectedListener listener) {
        this.context = context;
        this.userIcons = userIcons;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SMALL: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_profile_image, parent, false);
                return new SmallUserIconViewHolder(view, context);
            }
            case TYPE_FILTER: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_profile_image, parent, false);
                return new FilterByUserViewHolder(view, context, userIcons, listener);
            }
            case TYPE_ASSIGN: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_profile_image, parent, false);
                return new AssignUserViewHolder(view, context, userIcons);
            }
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        UserIcon userIcon = userIcons.get(position);
        holder.bind(userIcon);
    }

    @Override
    public int getItemCount() {
        return userIcons.size();
    }

    @Override
    public int getItemViewType(int position) {
        UserIcon element = userIcons.get(position);
        return element.ITEM_TYPE;

//        throw new IllegalArgumentException("Invalid position " + position);
    }

    public void clear() {
        userIcons.clear();
        notifyDataSetChanged();
    }
}
