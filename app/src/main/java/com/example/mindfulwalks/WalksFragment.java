package com.example.mindfulwalks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WalksFragment extends Fragment {

    RecyclerView recyclerView;
    CheckpointAdapter adapter;
    SearchView searchView;
    AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_walks, container, false);

        recyclerView = view.findViewById(R.id.recyclerCheckpoints);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.searchView);

        db = AppDatabase.getInstance(requireContext());

        setupSearch();
        loadCheckpoints();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCheckpoints();
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
    }

    private void performSearch(String query) {
        executorService.execute(() -> {
            List<Checkpoint> checkpointList;

            if (query == null || query.trim().isEmpty()) {
                checkpointList = db.checkpointDao().getAllCheckpoints();
            } else {
                checkpointList = db.checkpointDao().searchByNameOrTags(query);
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> updateAdapter(checkpointList));
            }
        });
    }

    private void loadCheckpoints() {
        executorService.execute(() -> {
            List<Checkpoint> checkpointList = db.checkpointDao().getAllCheckpoints();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> updateAdapter(checkpointList));
            }
        });
    }

    private void updateAdapter(List<Checkpoint> checkpointList) {
        adapter = new CheckpointAdapter(checkpointList,
                c -> {
                    Intent intent = new Intent(requireContext(), CheckpointDetailActivity.class);
                    intent.putExtra("checkpointId", c.id);
                    startActivity(intent);
                },
                (c, position) -> {
                    executorService.execute(() -> {
                        db.checkpointDao().deleteById(c.id);

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                adapter.removeItem(position);
                                Toast.makeText(getContext(), "Checkpoint deleted", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                },
                c -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("editCheckpointId", c.id);
                    bundle.putString("editTitle", c.title);
                    bundle.putString("editAddress", c.address);
                    bundle.putString("editPrompt", c.prompt);
                    bundle.putString("editTags", c.tags);

                    AddCheckpointFragment editFragment = new AddCheckpointFragment();
                    editFragment.setArguments(bundle);

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, editFragment)
                            .addToBackStack(null)
                            .commit();
                }
        );

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}

