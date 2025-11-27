package com.example.mindfulwalks;

public class Checkpoint {

    public int id;            // Unique ID for opening detail screen
    public String title;
    public String address;
    public String prompt;
    public long timestamp;    // Optional: for sorting, saving time created

    public Checkpoint(int id, String title, String address, String prompt) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.prompt = prompt;
        this.timestamp = System.currentTimeMillis();
    }
}

