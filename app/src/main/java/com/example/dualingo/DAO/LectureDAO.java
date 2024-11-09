package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.Lecture;

import java.util.List;

@Dao
public interface LectureDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLecture(Lecture lecture);

    @Query("SELECT * FROM lecture WHERE idLecture = :id")
    Lecture getLectureById(String id);

    @Query("SELECT * FROM lecture")
    List<Lecture> getAllLectures();

    @Delete
    void deleteLecture(Lecture lecture);
}
