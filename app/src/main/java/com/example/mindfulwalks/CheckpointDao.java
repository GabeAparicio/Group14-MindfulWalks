package com.example.mindfulwalks;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CheckpointDao {

    @Insert
    void insertCheckpoint(Checkpoint checkpoint);

    @Query("SELECT * FROM checkpoints ORDER BY id DESC")
    List<Checkpoint> getAllCheckpoints();

    @Query("SELECT * FROM checkpoints WHERE id = :id LIMIT 1")
    Checkpoint getCheckpointById(int id);
}
