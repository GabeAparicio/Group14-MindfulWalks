package com.example.mindfulwalks;

import java.util.ArrayList;
import java.util.List;

public class CheckpointStorage {

    private static final List<Checkpoint> checkpoints = new ArrayList<>();

    public static void addCheckpoint(Checkpoint cp) {
        checkpoints.add(cp);
    }

    public static List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
}
