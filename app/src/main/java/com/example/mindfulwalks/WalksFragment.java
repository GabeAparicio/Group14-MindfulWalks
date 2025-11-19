package com.example.mindfulwalks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class WalksFragment extends Fragment {

    TextView txtList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_walks, container, false);
        txtList = view.findViewById(R.id.txtWalksList);

        updateList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        List<Checkpoint> list = CheckpointStorage.getCheckpoints();

        if (list.isEmpty()) {
            txtList.setText("No checkpoints yet.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Checkpoint c : list) {
            builder.append("• ").append(c.title)
                    .append("\n  Address: ").append(c.address)
                    .append("\n  Prompt: ").append(c.prompt)
                    .append("\n\n");
        }

        txtList.setText(builder.toString());
    }
}
