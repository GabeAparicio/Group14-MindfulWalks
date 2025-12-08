package com.example.mindfulwalks;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FullMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String title;
    private String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_map);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Checkpoint Location");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        title = getIntent().getStringExtra("title");
        address = getIntent().getStringExtra("address");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fullMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        String apiKey = getString(R.string.google_maps_key);
        Log.d("MAP_KEY_TEST", "Injected key = " + apiKey);

        mMap = googleMap;

        if (latitude != 0.0 || longitude != 0.0) {
            LatLng checkpointLocation = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions()
                    .position(checkpointLocation)
                    .title(title)
                    .snippet(address));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(checkpointLocation, 15f));
        } else {
            LatLng toronto = new LatLng(43.6532, -79.3832);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 12f));
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}