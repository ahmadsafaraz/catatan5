package com.example.catatan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private Context context;

    public NoteAdapter(Context context) {
        this.context = context;
    }

    public void setNotes(List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());

        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd MMM yyyy",
                new Locale("id", "ID")
        );
        holder.tvDate.setText(sdf.format(new Date(note.getTimestamp())));

        // 🔹 CLICK → EDIT
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateNoteActivity.class);
            intent.putExtra("note_id", note.getId());
            intent.putExtra("note_title", note.getTitle());
            intent.putExtra("note_content", note.getContent());
            context.startActivity(intent);
        });

        // 🔹 LONG CLICK → DELETE
        holder.itemView.setOnLongClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).deleteNote(note.getId());
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return noteList != null ? noteList.size() : 0;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvContent;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            tvContent = itemView.findViewById(R.id.tv_item_excerpt);
        }
    }
}
