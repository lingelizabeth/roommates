package com.example.roommatehub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatehub.Helper;
import com.example.roommatehub.MainActivity;
import com.example.roommatehub.fragments.NotesDetailFragment;
import com.example.roommatehub.fragments.NotesFragment;
import com.example.roommatehub.models.ListItem;
import com.example.roommatehub.models.Note;
import com.example.roommatehub.R;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private Context context;
    private List<Note> notes;

    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle, tvFirstItem, tvSecondItem, tvUpdatedTime;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFirstItem = itemView.findViewById(R.id.tvFirstItem);
            tvSecondItem = itemView.findViewById(R.id.tvSecondItem);
            tvUpdatedTime = itemView.findViewById(R.id.tvUpdatedTime);

            itemView.setOnClickListener(this);
        }

        public void bind(Note note){
            tvTitle.setText(note.getTitle());
            tvUpdatedTime.setText(Helper.getAbbrevTimeAgo(note.getUpdatedAt()));


            // Check if length of items list >= 2, if it is, populate the first two textviews
            if(note.getItemList().size() >= 1){
                ListItem firstItem = (ListItem) note.getItemList().get(0);
                tvFirstItem.setText("??? "+firstItem.getText());
            }
            if(note.getItemList().size() >= 2){
                ListItem secondItem = (ListItem) note.getItemList().get(1);
                tvSecondItem.setText("??? "+secondItem.getText());
            }
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                // Get post at the current position
                Note note = notes.get(position);

                // Pass Note ID to the detail fragment
                // Detail fragment will query each list item on its own
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = NotesDetailFragment.newInstance(note);
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.flContainer, myFragment)
                        .addToBackStack(null)
                        .commit();
            }

        }
    }
}
