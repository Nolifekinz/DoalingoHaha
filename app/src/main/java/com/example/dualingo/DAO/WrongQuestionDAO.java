package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dualingo.Models.WrongQuestion;

import java.util.List;

@Dao
public interface WrongQuestionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWrongQuestion(WrongQuestion wrongQuestion);

    @Update
    void updateWrongQuestion(WrongQuestion wrongQuestion);

    // Lấy tất cả các bản ghi trong bảng
    @Query("SELECT * FROM wrong_questions")
    List<WrongQuestion> getAllWrongQuestions();

    // Lấy một bản ghi dựa vào ID
    @Query("SELECT * FROM wrong_questions WHERE idWrongQuestion = :id")
    WrongQuestion getWrongQuestionById(String id);

    // Xóa một bản ghi dựa vào ID
    @Query("DELETE FROM wrong_questions WHERE idWrongQuestion = :id")
    void deleteWrongQuestionById(String id);

    // Xóa tất cả các bản ghi
    @Query("DELETE FROM wrong_questions")
    void deleteAllWrongQuestions();
}
