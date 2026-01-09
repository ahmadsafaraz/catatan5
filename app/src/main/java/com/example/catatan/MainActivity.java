package com.example.catatan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCatatanku;
    private EditText etSearch;
    private FloatingActionButton fabAddNote;

    private RecyclerView rvNotes;
    private NoteAdapter adapter;
    private NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🔹 APPLY BAHASA MANUAL
        LanguageHelper.applyLanguage(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = NoteDatabase.getDatabase(this);

        btnCatatanku = findViewById(R.id.btn_catatanku);
        etSearch = findViewById(R.id.et_search);
        fabAddNote = findViewById(R.id.fab_add_note);
        rvNotes = findViewById(R.id.rv_notes);

        adapter = new NoteAdapter(this);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(adapter);

        fabAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            startActivity(intent);
        });

        btnCatatanku.setOnClickListener(v -> loadNotes());

        // 🔹 GANTI BAHASA (LONG CLICK)
        btnCatatanku.setOnLongClickListener(v -> {
            showLanguageDialog();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        List<Note> notes = db.noteDao().getAllNotes();
        adapter.setNotes(notes);
    }

    // 🔹 dipanggil dari NoteAdapter (long click)
    public void deleteNote(int noteId) {
        db.noteDao().deleteById(noteId);
        loadNotes();
        Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
    }

    // 🔹 DIALOG PILIH BAHASA
    private void showLanguageDialog() {
        String[] languages = {
                getString(R.string.language_indonesian),
                getString(R.string.language_english)
        };

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.choose_language))
                .setItems(languages, (dialog, which) -> {
                    if (which == 0) {
                        LanguageHelper.setLanguage(this, "id");
                    } else {
                        LanguageHelper.setLanguage(this, "en");
                    }
                    recreate();
                })
                .show();
    }
}
