package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.Vocabulary;

import java.util.List;

@Dao
public interface VocabularyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVocabulary(Vocabulary vocabulary);

    @Query("SELECT * FROM vocabulary WHERE idVocabulary = :id")
    Vocabulary getVocabularyById(String id);

    @Query("SELECT * FROM vocabulary")
    List<Vocabulary> getAllVocabulary();

    @Query("SELECT vietnameseMeaning FROM vocabulary WHERE englishWord = :word")
    String getMeaningByWord(String word);

    @Query("SELECT * FROM vocabulary WHERE idVocabulary IN (:vocabularyIds)")
    List<Vocabulary> getVocabulariesByIds(List<String> vocabularyIds);

    @Delete
    void deleteVocabulary(Vocabulary vocabulary);
}
