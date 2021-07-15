package com.example.roommatehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatehub.models.ListItem;
import com.example.roommatehub.models.Note;
import com.example.roommatehub.R;

import org.jetbrains.annotations.NotNull;

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

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvFirstItem, tvSecondItem;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFirstItem = itemView.findViewById(R.id.tvFirstItem);
            tvSecondItem = itemView.findViewById(R.id.tvSecondItem);
        }

        public void bind(Note note){
            tvTitle.setText(note.getTitle());

            // Check if length of items list >= 2, if it is, populate the first two textviews
            if(note.getItemList().size() >= 1){
                ListItem firstItem = (ListItem) note.getItemList().get(0);
                tvFirstItem.setText(firstItem.getText());
            }
            if(note.getItemList().size() >= 2){
                ListItem secondItem = (ListItem) note.getItemList().get(1);
                tvSecondItem.setText(secondItem.getText());
            }
        }
    }
}
