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

public class CheckpointDetailActivity extends AppCompatActivity {

    private EditText editTitle, editAddress, editPrompt;
    private TextView txtTimestamp;
    private RatingBar ratingBar;
    private Button btnSave, btnDelete, btnShare;

    private Checkpoint checkpoint;
    private AppDatabase db;

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
        checkpoint = db.checkpointDao().getCheckpointById(id);

        if (checkpoint == null) {
            Toast.makeText(this, "Checkpoint not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        editTitle = findViewById(R.id.editTitleDetail);
        editAddress = findViewById(R.id.editAddressDetail);
        editPrompt = findViewById(R.id.editPromptDetail);
        txtTimestamp = findViewById(R.id.detailTimestamp);
        ratingBar = findViewById(R.id.ratingBar);
        btnSave = findViewById(R.id.btnSaveEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnShare = findViewById(R.id.btnShareEmail);


        editTitle.setText(checkpoint.title);
        editAddress.setText(checkpoint.address);
        editPrompt.setText(checkpoint.prompt);
        ratingBar.setRating(checkpoint.rating);

        txtTimestamp.setText("Created: " + android.text.format.DateFormat.format("MMM dd, yyyy • h:mm a", checkpoint.timestamp));


        btnSave.setOnClickListener(v -> {
            checkpoint.title = editTitle.getText().toString().trim();
            checkpoint.address = editAddress.getText().toString().trim();
            checkpoint.prompt = editPrompt.getText().toString().trim();

            db.checkpointDao().updateCheckpoint(checkpoint);
            Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });


        btnDelete.setOnClickListener(v -> {
            db.checkpointDao().deleteCheckpoint(checkpoint);
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });


        ratingBar.setOnRatingBarChangeListener((rb, rating, fromUser) -> {
            db.checkpointDao().updateRating(checkpoint.id, rating);
        });


        btnShare.setOnClickListener(v -> shareViaEmail());
    }

    private void shareViaEmail() {

        String subject = "Mindful Walk Checkpoint: " + checkpoint.title;

        String message =
                "Title: " + checkpoint.title + "\n" +
                        "Address: " + checkpoint.address + "\n" +
                        "Prompt: " + checkpoint.prompt + "\n" +
                        "Rating: " + checkpoint.rating + " stars\n\n" +
                        "Created on: " + android.text.format.DateFormat.format("MMM dd, yyyy • h:mm a", checkpoint.timestamp);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(emailIntent, "Share via Email"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}


