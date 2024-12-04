package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.VocabularyLesson;

import java.util.List;

@Dao
public interface VocabularyLessonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVocabularyLesson(VocabularyLesson vocabularyLesson);

    @Query("SELECT * FROM vocabulary_lesson WHERE idVocabularyLesson = :id")
    VocabularyLesson getVocabularyLessonById(String id);

    @Query("SELECT * FROM vocabulary_lesson")
    List<VocabularyLesson> getAllVocabularyLessons();

    @Query("SELECT * FROM vocabulary_lesson WHERE idLecture = :lectureId ORDER BY RANDOM() LIMIT 3")
    List<VocabularyLesson> getRandomQuestionsByLectureId(String lectureId);


    @Delete
    void deleteVocabularyLesson(VocabularyLesson vocabularyLesson);
}

