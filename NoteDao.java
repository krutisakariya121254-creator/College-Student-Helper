package com.example.studybuddy.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studybuddy.data.entity.NoteEntity;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(NoteEntity note);

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY createdAt DESC")
    List<NoteEntity> getAllNotes(int userId);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);
}
