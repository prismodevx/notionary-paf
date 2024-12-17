package com.example.notionary_v1.fragments.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notionary_v1.AddNoteActivity;
import com.example.notionary_v1.AddReminderActivity;
import com.example.notionary_v1.R;
import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.Reminder;

import java.util.List;
import java.util.Random;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder> {
    private List<Reminder> reminders;

    public RemindersAdapter(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void updateReminders(List<Reminder> newReminders) {
        this.reminders = newReminders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new RemindersAdapter.ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersAdapter.ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.titulo.setText(reminder.getTitulo());
        holder.fechaHora.setText(reminder.getFechaHora());

        String[] colors = {"#ffffff", "#D4E8FF", "#FFD4F4", "#D4FFDC"};

        if (reminder.getEsFavorito() == 1) {
            holder.favIcon.setVisibility(View.VISIBLE);
        } else {
            holder.favIcon.setVisibility(View.GONE);
        }

        Random random = new Random();
        int randomIndex = random.nextInt(colors.length);

        holder.cardView.setCardBackgroundColor(Color.parseColor(colors[randomIndex]));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
            intent.putExtra("reminder_id", reminder.getId());
            intent.putExtra("reminder_titulo", reminder.getTitulo());
            intent.putExtra("reminder_fecha_hora", reminder.getFechaHora());
            intent.putExtra("reminder_like", reminder.getEsFavorito());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reminders != null ? reminders.size() : 0;
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fechaHora, color;
        CardView cardView;
        ImageButton favIcon;

        ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.reminder_titulo);
            fechaHora = itemView.findViewById(R.id.reminder_fecha_hora);
            cardView = itemView.findViewById(R.id.card_view);
            favIcon = itemView.findViewById(R.id.note_fav);
        }
    }
}
