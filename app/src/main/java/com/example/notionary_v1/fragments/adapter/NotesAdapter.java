package com.example.notionary_v1.fragments.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notionary_v1.AddNoteActivity;
import com.example.notionary_v1.R;
import com.example.notionary_v1.fragments.data.Note;

import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes;

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.date.setText(note.getDate());
//        holder.cardView.setCardBackgroundColor(note.getColor());

        String[] colors = {"#ffffff", "#D4E8FF", "#FFD4F4", "#D4FFDC"};

        if (note.getEsFavorito() == 1) {
            holder.favIcon.setVisibility(View.VISIBLE);
        } else {
            holder.favIcon.setVisibility(View.GONE);
        }

        Random random = new Random();
        int randomIndex = random.nextInt(colors.length);

        holder.cardView.setCardBackgroundColor(Color.parseColor(colors[randomIndex]));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddNoteActivity.class);
            intent.putExtra("note_id", note.getId());
            intent.putExtra("note_title", note.getTitle());
            intent.putExtra("note_description", note.getDescription());
            intent.putExtra("note_date", note.getDescription());
            intent.putExtra("note_like", note.getEsFavorito());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, color, date;
        CardView cardView;
        ImageButton favIcon;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            description = itemView.findViewById(R.id.note_description);
            date = itemView.findViewById(R.id.note_date);
//            color = itemView.findViewById(R.id.color);
            cardView = itemView.findViewById(R.id.card_view);
            favIcon = itemView.findViewById(R.id.note_fav);
        }
    }
}
