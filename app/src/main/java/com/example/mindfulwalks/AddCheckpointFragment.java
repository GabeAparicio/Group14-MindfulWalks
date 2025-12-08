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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCheckpointFragment extends Fragment {

    EditText editTitle, editAddress, editPrompt, editTags;
    Button btnSave;

    private boolean isEditMode = false;
    private int editCheckpointId = -1;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_checkpoint, container, false);

        editTitle = view.findViewById(R.id.editTitle);
        editAddress = view.findViewById(R.id.editAddress);
        editPrompt = view.findViewById(R.id.editPrompt);
        editTags = view.findViewById(R.id.editTags);
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
            editTags.setText(args.getString("editTags"));

            btnSave.setText("Update Checkpoint");
        }
    }

    private void saveCheckpoint() {

        String title = editTitle.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String prompt = editPrompt.getText().toString().trim();
        String tags = editTags.getText().toString().trim();

        // Validation
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

        // Geocode the address to get coordinates
        double lat = 0.0;
        double lng = 0.0;

        try {
            android.location.Geocoder geocoder = new android.location.Geocoder(requireContext());
            java.util.List<android.location.Address> addresses = geocoder.getFromLocationName(address, 1);

            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address location = addresses.get(0);
                lat = location.getLatitude();
                lng = location.getLongitude();
            } else {
                Toast.makeText(getActivity(),
                        "⚠ Address not found. Checkpoint saved without location.",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(),
                    "⚠ Could not geocode address. Checkpoint saved without location.",
                    Toast.LENGTH_LONG).show();
        }

        AppDatabase db = AppDatabase.getInstance(requireContext());

        final double finalLat = lat;
        final double finalLng = lng;

        executorService.execute(() -> {
            if (isEditMode) {
                Checkpoint cp = db.checkpointDao().getCheckpointById(editCheckpointId);

                cp.title = title;
                cp.address = address;
                cp.prompt = prompt;
                cp.tags = tags;
                cp.latitude = finalLat;
                cp.longitude = finalLng;

                db.checkpointDao().updateCheckpoint(cp);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(),
                                "✓ Checkpoint updated successfully!",
                                Toast.LENGTH_SHORT).show();

                        getParentFragmentManager().popBackStack();
                    });
                }

            } else {
                Checkpoint cp = new Checkpoint(title, address, prompt, tags, finalLat, finalLng);
                db.checkpointDao().insertCheckpoint(cp);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        String message = (finalLat != 0.0 && finalLng != 0.0)
                                ? "✓ Checkpoint \"" + title + "\" saved with location!"
                                : "✓ Checkpoint \"" + title + "\" saved!";

                        Toast.makeText(getActivity(),
                                message,
                                Toast.LENGTH_LONG).show();

                        editTitle.setText("");
                        editAddress.setText("");
                        editPrompt.setText("");
                        editTags.setText("");
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}