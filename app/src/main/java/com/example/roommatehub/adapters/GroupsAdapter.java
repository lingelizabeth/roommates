package com.example.roommatehub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.HomeActivity;
import com.example.roommatehub.R;

import org.parceler.Parcels;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
    private Context context;
    private List<Group> groups;

    public GroupsAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivGroup;
        private TextView tvTitle, tvDescription;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            ivGroup = itemView.findViewById(R.id.ivGroup);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            itemView.setOnClickListener(this);
        }

        public void bind(Group group){
            tvTitle.setText(group.getTitle());
            tvDescription.setText(group.getDescription());

            Glide.with(context)
                    .load(R.drawable.atlas)
                    .transforms(new RoundedCorners(10), new CenterCrop())
                    .into(ivGroup);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            // If clicked position is valid
            if (position != RecyclerView.NO_POSITION) {
                // Get post at the current position
                Group group = groups.get(position);

                Intent i = new Intent(context, HomeActivity.class);
                i.putExtra("Group", Parcels.wrap(group));
                (context).startActivity(i);
            }
        }
    }
}
