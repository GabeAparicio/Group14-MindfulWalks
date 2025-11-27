package com.example.mindfulwalks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WalksFragment extends Fragment {

    private RecyclerView recyclerView;
    private CheckpointAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_walks, container, false);

        recyclerView = view.findViewById(R.id.recyclerCheckpoints);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {
        List<Checkpoint> list = CheckpointStorage.getCheckpoints();

        adapter = new CheckpointAdapter(list, checkpoint -> {
            // On item click → open detail screen
            Intent i = new Intent(getActivity(), CheckpointDetailActivity.class);
            i.putExtra("checkpointId", checkpoint.id);
            startActivity(i);
        });

        recyclerView.setAdapter(adapter);
    }
}

