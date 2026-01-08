package com.example.studybuddy.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studybuddy.data.entity.AssignmentEntity;

import java.util.List;

@Dao
public interface AssignmentDao {

    @Insert
    void insert(AssignmentEntity assignment);

    @Update
    void update(AssignmentEntity assignment);

    @Delete
    void delete(AssignmentEntity assignment);

    @Query("SELECT * FROM assignments WHERE userId = :userId ORDER BY dueDate ASC")
    List<AssignmentEntity> getAll(int userId);
}
