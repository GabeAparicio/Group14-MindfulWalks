package com.example.mindfulwalks;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checkpoints")
public class Checkpoint {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String address;
    public String prompt;

    public double latitude;
    public double longitude;

    public long timestamp;

    public Checkpoint(String title, String address, String prompt, double latitude, double longitude) {
        this.title = title;
        this.address = address;
        this.prompt = prompt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = System.currentTimeMillis();
    }
}
