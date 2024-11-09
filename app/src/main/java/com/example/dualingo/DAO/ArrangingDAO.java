package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.Arranging;

import java.util.List;

@Dao
public interface ArrangingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArranging(Arranging arranging);

    @Query("SELECT * FROM arranging WHERE idArranging = :id")
    Arranging getArrangingById(String id);

    @Query("SELECT * FROM arranging")
    List<Arranging> getAllArranging();

    @Delete
    void deleteArranging(Arranging arranging);
}
