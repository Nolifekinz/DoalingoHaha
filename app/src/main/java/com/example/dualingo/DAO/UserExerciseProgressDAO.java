package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.UserExerciseProgress;

import java.util.List;

@Dao
public interface UserExerciseProgressDAO {
    // Thêm trạng thái bài tập
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProgress(UserExerciseProgress progress);

    // Lấy trạng thái bài tập dựa trên userId và idExercise
    @Query("SELECT * FROM user_exercise_progress WHERE userId = :userId AND idExercise = :idExercise")
    UserExerciseProgress getProgress(String userId, String idExercise);

    // Lấy tất cả trạng thái bài tập của một người dùng
    @Query("SELECT * FROM user_exercise_progress WHERE userId = :userId")
    List<UserExerciseProgress> getAllProgressByUser(String userId);
}
