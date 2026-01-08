package com.example.studybuddy.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studybuddy.data.entity.ExamEntity;

import java.util.List;

@Dao
public interface ExamDao {

    @Insert
    void insert(ExamEntity exam);

    @Delete
    void delete(ExamEntity exam);

    // ✅ USER-SPECIFIC DATA
    @Query("SELECT * FROM exams WHERE userId = :userId ORDER BY examDate ASC")
    List<ExamEntity> getAll(int userId);
}