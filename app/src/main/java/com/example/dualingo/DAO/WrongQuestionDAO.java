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
    void insertOrUpdateWrongQuestion(WrongQuestion wrongQuestion);

    @Query("SELECT * FROM wrong_questions WHERE idWrongQuestion = :idWrongQuestion")
    WrongQuestion getWrongQuestionById(String idWrongQuestion);

    @Query("UPDATE wrong_questions SET idWrongArrangingList = :idWrongArrangingList WHERE idWrongQuestion = :idWrongQuestion")
    void updateWrongArrangingList(String idWrongQuestion, List<String> idWrongArrangingList);

    @Query("SELECT * FROM wrong_questions")
    List<WrongQuestion> getAllWrongQuestions();
}
