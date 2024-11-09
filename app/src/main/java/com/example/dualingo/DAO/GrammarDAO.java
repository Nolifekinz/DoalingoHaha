package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.Grammar;

import java.util.List;

@Dao
public interface GrammarDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrammar(Grammar grammar);

    @Query("SELECT * FROM grammar WHERE idGrammar = :id")
    Grammar getGrammarById(String id);

    @Query("SELECT * FROM grammar")
    List<Grammar> getAllGrammars();

    @Delete
    void deleteGrammar(Grammar grammar);
}
