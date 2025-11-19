package com.example.mindfulwalks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.mindfulwalks.MapFragment;
import com.example.mindfulwalks.AddCheckpointFragment;
import com.example.mindfulwalks.WalksFragment;
import com.example.mindfulwalks.AboutFragment;



public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Default screen → MapFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new MapFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();

            if (id == R.id.nav_map) {
                selected = new MapFragment();
            } else if (id == R.id.nav_add) {
                selected = new AddCheckpointFragment();
            } else if (id == R.id.nav_walks) {
                selected = new WalksFragment();
            } else if (id == R.id.nav_about) {
                selected = new AboutFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, selected)
                    .commit();

            return true;
        });
    }
}
