package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.CompletedLesson;

import java.util.List;

@Dao
public interface CompletedLessonDAO {
    // Chèn mới hoặc cập nhật CompletedLesson
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(CompletedLesson completedLesson);

    // Lấy CompletedLesson dựa vào userId và lectureId
    @Query("SELECT * FROM completed_lesson WHERE userId = :userId AND lectureId = :lectureId LIMIT 1")
    CompletedLesson getCompletedLesson(String userId, String lectureId);

    // Lấy tất cả các CompletedLesson của một user
    @Query("SELECT * FROM completed_lesson WHERE userId = :userId")
    List<CompletedLesson> getAllCompletedLessons(String userId);

    // Xóa CompletedLesson dựa vào userId và lectureId
    @Query("DELETE FROM completed_lesson WHERE userId = :userId AND lectureId = :lectureId")
    void deleteCompletedLesson(String userId, String lectureId);

    // Xóa tất cả CompletedLesson của một user
    @Query("DELETE FROM completed_lesson WHERE userId = :userId")
    void deleteAllCompletedLessons(String userId);
}
