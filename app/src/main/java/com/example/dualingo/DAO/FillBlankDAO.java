package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dualingo.Models.FillBlank;

import java.util.List;

@Dao
public interface FillBlankDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFillBlank(FillBlank fillBlank);

    @Query("SELECT * FROM fill_blank WHERE idFillBlank = :id")
    FillBlank getFillBlankById(String id);

    @Query("SELECT * FROM fill_blank")
    List<FillBlank> getAllFillBlanks();

    @Delete
    void deleteFillBlank(FillBlank fillBlank);
}
