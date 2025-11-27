package com.example.mindfulwalks;

import java.util.ArrayList;
import java.util.List;

public class CheckpointStorage {

    private static final List<Checkpoint> checkpoints = new ArrayList<>();
    private static int nextId = 1;  // Auto-increment ID counter

    public static void addCheckpoint(Checkpoint cp) {
        checkpoints.add(cp);
    }

    public static List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public static int getNextId() {
        return nextId++;
    }

    public static Checkpoint getById(int id) {
        for (Checkpoint c : checkpoints) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }
}
