package com.example.mindfulwalks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckpointDetailActivity extends AppCompatActivity {

    private EditText editTitle, editAddress, editPrompt;
    private TextView txtTimestamp, txtTags;
    private RatingBar ratingBar;
    private Button btnSave, btnDelete, btnShare, btnDirections, btnFullMap;

    private Checkpoint checkpoint;
    private AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Checkpoint Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);

        int id = getIntent().getIntExtra("checkpointId", -1);

        executorService.execute(() -> {
            checkpoint = db.checkpointDao().getCheckpointById(id);

            runOnUiThread(() -> {
                if (checkpoint == null) {
                    Toast.makeText(this, "Checkpoint not found!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                editTitle = findViewById(R.id.editTitleDetail);
                editAddress = findViewById(R.id.editAddressDetail);
                editPrompt = findViewById(R.id.editPromptDetail);
                txtTimestamp = findViewById(R.id.detailTimestamp);
                txtTags = findViewById(R.id.detailTags);
                ratingBar = findViewById(R.id.ratingBar);
                btnSave = findViewById(R.id.btnSaveEdit);
                btnDelete = findViewById(R.id.btnDelete);
                btnShare = findViewById(R.id.btnShareEmail);
                btnDirections = findViewById(R.id.btnDirections);
                btnFullMap = findViewById(R.id.btnFullMap);

                loadCheckpointData();

                btnSave.setOnClickListener(v -> saveChanges());
                btnDelete.setOnClickListener(v -> deleteCheckpoint());
                btnShare.setOnClickListener(v -> shareViaEmail());
                btnDirections.setOnClickListener(v -> openDirections());
                btnFullMap.setOnClickListener(v -> openFullMap());
            });
        });
    }

    private void loadCheckpointData() {
        editTitle.setText(checkpoint.title);
        editAddress.setText(checkpoint.address);
        editPrompt.setText(checkpoint.prompt);
        txtTags.setText(checkpoint.tags);
        txtTimestamp.setText("Created: " + new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm")
                .format(new java.util.Date(checkpoint.timestamp)));
        ratingBar.setRating(checkpoint.rating);
    }

    private void saveChanges() {
        checkpoint.title = editTitle.getText().toString().trim();
        checkpoint.address = editAddress.getText().toString().trim();
        checkpoint.prompt = editPrompt.getText().toString().trim();
        checkpoint.rating = ratingBar.getRating();

        executorService.execute(() -> {
            db.checkpointDao().updateCheckpoint(checkpoint);

            runOnUiThread(() -> {
                Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void deleteCheckpoint() {
        executorService.execute(() -> {
            db.checkpointDao().deleteById(checkpoint.id);

            runOnUiThread(() -> {
                Toast.makeText(this, "Checkpoint deleted", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void shareViaEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Checkpoint: " + checkpoint.title);
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out this checkpoint!\n\n" +
                        "Title: " + checkpoint.title + "\n" +
                        "Address: " + checkpoint.address + "\n" +
                        "Prompt: " + checkpoint.prompt + "\n" +
                        "Rating: " + checkpoint.rating + " stars");

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Share via Email"));
        }
    }

    private void openDirections() {
        if (checkpoint.latitude == 0.0 && checkpoint.longitude == 0.0) {
            Toast.makeText(this, "No location coordinates available", Toast.LENGTH_SHORT).show();
            return;
        }

        String uri = "google.navigation:q=" + checkpoint.latitude + "," + checkpoint.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            String webUri = "https://www.google.com/maps/dir/?api=1&destination="
                    + checkpoint.latitude + "," + checkpoint.longitude;
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
            startActivity(webIntent);
        }
    }

    private void openFullMap() {
        Intent intent = new Intent(this, FullMapActivity.class);
        intent.putExtra("checkpointId", checkpoint.id);
        intent.putExtra("title", checkpoint.title);
        intent.putExtra("address", checkpoint.address);
        intent.putExtra("latitude", checkpoint.latitude);
        intent.putExtra("longitude", checkpoint.longitude);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}

