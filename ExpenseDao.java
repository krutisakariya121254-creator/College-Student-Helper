package com.example.studybuddy.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;   // ✅ IMPORTANT

import com.example.studybuddy.data.entity.ExpenseEntity;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(ExpenseEntity expense);

    @Update   // ✅ REQUIRED FOR UPDATE
    void update(ExpenseEntity expense);

    @Delete
    void delete(ExpenseEntity expense);

    // ✅ USER-SPECIFIC DATA
    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY expense_id DESC")
    List<ExpenseEntity> getAll(int userId);
}
