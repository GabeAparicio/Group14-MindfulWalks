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

    private boolean isEditMode = false;
    private int editCheckpointId = -1;

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

        checkIfEditMode();

        btnSave.setOnClickListener(v -> saveCheckpoint());

        return view;
    }

    private void checkIfEditMode() {
        Bundle args = getArguments();
        if (args != null && args.containsKey("editCheckpointId")) {
            isEditMode = true;
            editCheckpointId = args.getInt("editCheckpointId");

            editTitle.setText(args.getString("editTitle"));
            editAddress.setText(args.getString("editAddress"));
            editPrompt.setText(args.getString("editPrompt"));

            btnSave.setText("Update Checkpoint");
        }
    }

    private void saveCheckpoint() {

        String title = editTitle.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String prompt = editPrompt.getText().toString().trim();

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

        if (title.length() > 100) {
            editTitle.setError("Title is too long (max 100 characters)");
            return;
        }

        if (address.length() > 200) {
            editAddress.setError("Address is too long (max 200 characters)");
            return;
        }

        if (prompt.length() > 500) {
            editPrompt.setError("Prompt is too long (max 500 characters)");
            return;
        }

        double lat = 0.0;
        double lng = 0.0;

        AppDatabase db = AppDatabase.getInstance(requireContext());

        if (isEditMode) {
            Checkpoint cp = db.checkpointDao().getCheckpointById(editCheckpointId);
            cp.title = title;
            cp.address = address;
            cp.prompt = prompt;
            cp.latitude = lat;
            cp.longitude = lng;

            db.checkpointDao().updateCheckpoint(cp);

            Toast.makeText(getActivity(),
                    "✓ Checkpoint updated successfully!",
                    Toast.LENGTH_SHORT).show();

            getParentFragmentManager().popBackStack();

        } else {
            Checkpoint cp = new Checkpoint(title, address, prompt, lat, lng);
            db.checkpointDao().insertCheckpoint(cp);

            Toast.makeText(getActivity(),
                    "✓ Checkpoint \"" + title + "\" saved successfully!",
                    Toast.LENGTH_LONG).show();

            editTitle.setText("");
            editAddress.setText("");
            editPrompt.setText("");
        }
    }

}

