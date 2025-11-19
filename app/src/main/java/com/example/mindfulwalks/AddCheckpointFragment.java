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

        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString();
            String address = editAddress.getText().toString();
            String prompt = editPrompt.getText().toString();

            // Very simple validation
            if (title.isEmpty() || address.isEmpty() || prompt.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Checkpoint cp = new Checkpoint(title, address, prompt);
            CheckpointStorage.addCheckpoint(cp);

            Toast.makeText(getActivity(), "Checkpoint saved!", Toast.LENGTH_SHORT).show();

            // clear fields
            editTitle.setText("");
            editAddress.setText("");
            editPrompt.setText("");
        });

        return view;
    }
}
