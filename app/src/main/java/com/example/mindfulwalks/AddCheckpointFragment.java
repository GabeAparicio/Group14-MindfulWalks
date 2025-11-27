package com.example.mindfulwalks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddCheckpointFragment extends Fragment {

    EditText editTitle, editAddress, editPrompt;
    Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_checkpoint, container, false);

        editTitle = view.findViewById(R.id.editTitle);
        editAddress = view.findViewById(R.id.editAddress);
        editPrompt = view.findViewById(R.id.editPrompt);
        btnSave = view.findViewById(R.id.btnSaveCheckpoint);

        btnSave.setOnClickListener(v -> saveCheckpoint());

        return view;
    }

    private void saveCheckpoint() {

        String title = editTitle.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String prompt = editPrompt.getText().toString().trim();

        // Improved validation
        if (title.isEmpty()) {
            editTitle.setError("Title is required");
            return;
        }
        if (address.isEmpty()) {
            editAddress.setError("Address is required");
            return;
        }
        if (prompt.isEmpty()) {
            editPrompt.setError("Prompt is required");
            return;
        }

        int newId = CheckpointStorage.getNextId();

        Checkpoint cp = new Checkpoint(newId, title, address, prompt);

        CheckpointStorage.addCheckpoint(cp);

        Toast.makeText(getActivity(),
                "Checkpoint saved successfully!",
                Toast.LENGTH_SHORT).show();

        // Clear input fields
        editTitle.setText("");
        editAddress.setText("");
        editPrompt.setText("");
    }
}

