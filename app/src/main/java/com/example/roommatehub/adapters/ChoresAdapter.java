package com.example.roommatehub.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatehub.OneSignalNotificationSender;
import com.example.roommatehub.R;
import com.example.roommatehub.interfaces.onCheckboxCheckedListener;
import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.Notification;
import com.example.roommatehub.models.UserIcon;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChoresAdapter extends RecyclerView.Adapter<ChoresAdapter.ViewHolder>{

    private Context context;
    private List<Chore> chores;
    private onCheckboxCheckedListener listener;
    private Group group;


    public ChoresAdapter(Context context, List<Chore> chores) {
        this.context = context;
        this.chores = chores;

    }
    public ChoresAdapter(Context context, List<Chore> chores, onCheckboxCheckedListener listener, Group group) {
        this.context = context;
        this.chores = chores;
        this.listener = listener;
        this.group = group;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Chore chore = chores.get(position);
        holder.bind(chore);
    }


    @Override
    public int getItemCount() {
        return chores.size();
    }

    public void clear() {
        // Clear both the chores list and the user icons for each chore
        chores.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Chore> list) {
        chores.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox cbChore;
        RecyclerView rvUsers;
        private List<UserIcon> allUsers;
        private UserIconAdapter adapter;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cbChore = itemView.findViewById(R.id.cbChore);
            cbChore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    // Save checked state to Parse database when toggled
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        Chore currentChore = chores.get(position);
                        currentChore.setChecked(checked);
                        currentChore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    Log.e("ChoresAdapter", "Error saving chore status: "+e);
                                }
                                Log.i("ChoresAdapter", "Chore marked done");

                                // Add notification that chore was marked completed
                                if(checked)
                                    createNotification(currentChore);
                            }
                        });

                        listener.onCheckboxChecked(checked);
                    }


                }
            });

            // Set up users recycler view
            rvUsers = itemView.findViewById(R.id.rvUsers);
            allUsers = new ArrayList<>();

            adapter = new UserIconAdapter(context, allUsers);
            rvUsers.setAdapter(adapter);
            rvUsers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }

        public void bind(Chore chore){
            cbChore.setText(chore.getName());
            cbChore.setChecked(chore.isChecked());

            //Populate users for this chore
            List<UserIcon> userIcons = null;
            try {
                int type = 0;
                userIcons = UserIcon.fromUserArray(chore.getUserList(), type);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            allUsers.clear(); // makes it slower but ensures no duplicates
            allUsers.addAll(userIcons);
            adapter.notifyDataSetChanged();
        }

        private void createNotification(Chore currentChore) {
            String[] data = Notification.getCompleteChoreNotificationData(
                    ParseUser.getCurrentUser(),
                    currentChore,
                    group,
                    context.getString(R.string.complete_chore_icon_url));
            Notification notification = new Notification("Chores",
                    data,
                    "ic_bell_white_24dp",
                    "[]",
                    true,
                    group);
            OneSignalNotificationSender.sendDeviceNotification(notification);

        }

    }
}
