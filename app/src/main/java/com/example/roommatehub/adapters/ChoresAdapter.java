package com.example.roommatehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatehub.R;
import com.example.roommatehub.models.Chore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChoresAdapter extends RecyclerView.Adapter<ChoresAdapter.ViewHolder>{

    private Context context;
    private List<Chore> chores;

    public ChoresAdapter(Context context, List<Chore> chores) {
        this.context = context;
        this.chores = chores;
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox cbChore;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cbChore = itemView.findViewById(R.id.cbChore);
        }

        public void bind(Chore chore){
            cbChore.setText(chore.getName());
        }
    }
}
