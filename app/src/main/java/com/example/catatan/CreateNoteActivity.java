package com.example.catatan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnSave;
    private EditText etTitle;
    private EditText etContent;
    private TextView tvDetail;

    private NoteDatabase db;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🔹 APPLY BAHASA MANUAL
        LanguageHelper.applyLanguage(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        db = NoteDatabase.getDatabase(this);

        btnBack = findViewById(R.id.btn_back);
        btnSave = findViewById(R.id.btn_save);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        tvDetail = findViewById(R.id.tv_detail);

        // 🔹 MODE EDIT
        if (getIntent().hasExtra("note_id")) {
            noteId = getIntent().getIntExtra("note_id", -1);
            etTitle.setText(getIntent().getStringExtra("note_title"));
            etContent.setText(getIntent().getStringExtra("note_content"));
        }

        updateDetailText(etContent.getText().length());

        etContent.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDetailText(s.length());
            }
        });

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveNote());
    }

    private void updateDetailText(int charCount) {
        String time = new SimpleDateFormat(
                "dd MMM yyyy, HH:mm",
                Locale.getDefault()
        ).format(new Date());

        String charText = getString(R.string.characters, charCount);
        tvDetail.setText(time + " | " + charText);
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_note_warning), Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(
                title.isEmpty() ? getString(R.string.untitled) : title,
                content,
                System.currentTimeMillis()
        );

        if (noteId == -1) {
            db.noteDao().insert(note);
            Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
        } else {
            db.noteDao().update(noteId, note.getTitle(), note.getContent(), note.getTimestamp());
            Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
