package com.example.catatan;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM notes_table ORDER BY timestamp DESC")
    List<Note> getAllNotes();

    @Query("DELETE FROM notes_table WHERE id = :id")
    void deleteById(int id);

    @Query("UPDATE notes_table SET title=:title, content=:content, timestamp=:timestamp WHERE id=:id")
    void update(int id, String title, String content, long timestamp);
}
