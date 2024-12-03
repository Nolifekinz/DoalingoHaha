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

    @Query("UPDATE wrong_questions SET idWrongFillBlankList = :idWrongFillBlankList WHERE idWrongQuestion = :idWrongQuestion")
    void updateWrongFillBlankList(String idWrongQuestion, List<String> idWrongFillBlankList);

    @Query("UPDATE wrong_questions SET idWrongListeningList = :idWrongListeningList WHERE idWrongQuestion = :idWrongQuestion")
    void updateWrongListeningList(String idWrongQuestion, List<String> idWrongListeningList);

    @Query("UPDATE wrong_questions SET idWrongSpeakingList = :idWrongSpeakingList WHERE idWrongQuestion = :idWrongQuestion")
    void updateWrongSpeakingList(String idWrongQuestion, List<String> idWrongSpeakingList);

    @Query("UPDATE wrong_questions SET idWrongVocabularyList = :idWrongVocabularyList WHERE idWrongQuestion = :idWrongQuestion")
    void updateWrongVocabularyList(String idWrongQuestion, List<String> idWrongVocabularyList);

    @Query("SELECT * FROM wrong_questions")
    List<WrongQuestion> getAllWrongQuestions();
}
