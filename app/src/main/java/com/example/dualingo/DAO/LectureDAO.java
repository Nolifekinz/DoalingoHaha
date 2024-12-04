package com.example.dualingo.DAO;

import androidx.lifecycle.LiveData;
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
    List<Lecture> getAllLectures();  // Phương thức cũ

    @Query("SELECT * FROM lecture")
    LiveData<List<Lecture>> getAllLecturesLive();  // Phương thức mới trả về LiveData

    @Query("SELECT * FROM lecture WHERE idLecture IN (:lectureIds)")
    LiveData<List<Lecture>> getLiveLectureByIds(List<String> lectureIds);

    @Query("SELECT * FROM lecture WHERE idLecture IN (:lectureIds)")
    List<Lecture> getLectureByIds(List<String> lectureIds);

    @Query("SELECT * FROM lecture WHERE idLecture IN (:lectureIds) ORDER BY orderIndex ASC")
    LiveData<List<Lecture>> getLectureByIdsOrdered(List<String> lectureIds);

    @Delete
    void deleteLecture(Lecture lecture);
}
