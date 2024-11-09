package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.Speaking;

import java.util.List;

@Dao
public interface SpeakingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpeaking(Speaking speaking);

    @Query("SELECT * FROM speaking WHERE idSpeaking = :id")
    Speaking getSpeakingById(String id);

    @Query("SELECT * FROM speaking")
    List<Speaking> getAllSpeaks();

    @Delete
    void deleteSpeaking(Speaking speaking);
}
