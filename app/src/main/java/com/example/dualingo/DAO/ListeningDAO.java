package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.Listening;

import java.util.List;

@Dao
public interface ListeningDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListening(Listening listening);

    @Query("SELECT * FROM listening WHERE idListening = :id")
    Listening getListeningById(String id);

    @Query("SELECT * FROM listening")
    List<Listening> getAllListenings();

    @Delete
    void deleteListening(Listening listening);
}
