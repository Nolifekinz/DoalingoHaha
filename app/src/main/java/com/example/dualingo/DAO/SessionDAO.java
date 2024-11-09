package com.example.dualingo.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.dualingo.Models.Session;

import java.util.List;

@Dao
public interface SessionDAO {
    // Thêm một session mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Session session);

    // Cập nhật một session
    @Update
    void update(Session session);

    // Xóa một session
    @Delete
    void delete(Session session);

    // Lấy tất cả session
    @Query("SELECT * FROM session")
    List<Session> getAllSessions();

    // Lấy một session theo ID
    @Query("SELECT * FROM session WHERE idSession = :id")
    Session getSessionById(String id);
}
