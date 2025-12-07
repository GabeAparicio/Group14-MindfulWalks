package com.example.mindfulwalks;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckpointDetailActivity extends AppCompatActivity {

    private TextView txtTitle, txtAddress, txtPrompt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint_detail);

        // Enable back button in the top bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Checkpoint Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtTitle = findViewById(R.id.detailTitle);
        txtAddress = findViewById(R.id.detailAddress);
        txtPrompt = findViewById(R.id.detailPrompt);

        // Read ID from intent
        int id = getIntent().getIntExtra("checkpointId", -1);

        if (id != -1) {

            AppDatabase db = AppDatabase.getInstance(this);
            Checkpoint cp = db.checkpointDao().getCheckpointById(id);

            if (cp != null) {
                txtTitle.setText(cp.title);
                txtAddress.setText(cp.address);
                txtPrompt.setText(cp.prompt);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Back button closes activity
        return true;
    }
}

