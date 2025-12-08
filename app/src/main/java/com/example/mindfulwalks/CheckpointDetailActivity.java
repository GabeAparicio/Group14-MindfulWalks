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
    private TextView txtTitle, txtAddress, txtPrompt, txtTags;

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

            if (cp != null) {
                txtTitle.setText(cp.title);
                txtAddress.setText(cp.address);
                txtPrompt.setText(cp.prompt);
                txtTags.setText(cp.tags);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}


