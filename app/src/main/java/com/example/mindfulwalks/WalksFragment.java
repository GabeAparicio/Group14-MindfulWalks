package com.example.mindfulwalks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WalksFragment extends Fragment {

    RecyclerView recyclerView;
    CheckpointAdapter adapter;

    // ⭐ NEW — register result listener
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                // Anytime user edits OR deletes → refresh list
                loadCheckpoints();
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_walks, container, false);

        recyclerView = view.findViewById(R.id.recyclerCheckpoints);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadCheckpoints();

        return view;
    }

    private void loadCheckpoints() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        List<Checkpoint> checkpointList = db.checkpointDao().getAllCheckpoints();

        adapter = new CheckpointAdapter(checkpointList, checkpoint -> {

            Intent intent = new Intent(requireContext(), CheckpointDetailActivity.class);
            intent.putExtra("checkpointId", checkpoint.id);

            detailLauncher.launch(intent);

        });

        recyclerView.setAdapter(adapter);
    }
}

