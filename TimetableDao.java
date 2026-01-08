package com.example.studybuddy.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;   // ✅ IMPORTANT IMPORT

import com.example.studybuddy.data.entity.TimetableEntity;

import java.util.List;

@Dao
public interface TimetableDao {

    @Insert
    void insert(TimetableEntity timetable);

    @Query("SELECT * FROM timetable WHERE userId = :userId ORDER BY day, time")
    List<TimetableEntity> getAll(int userId);

    @Query("SELECT * FROM timetable WHERE userId = :userId AND day = :day ORDER BY time")
    List<TimetableEntity> getByDay(int userId, String day);

    @Update
    void update(TimetableEntity timetable);

    @Delete
    void delete(TimetableEntity timetable);
}
