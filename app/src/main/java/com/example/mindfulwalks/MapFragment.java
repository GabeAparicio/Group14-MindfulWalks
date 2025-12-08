package com.example.mindfulwalks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        db = AppDatabase.getInstance(requireContext());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
            centerOnUser();

        } else {
            requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1001);
        }

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            loadCheckpointMarkers();
        }, 500);
    }

    private void loadCheckpointMarkers() {
        executorService.execute(() -> {
            List<Checkpoint> checkpoints = db.checkpointDao().getAllCheckpoints();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    for (Checkpoint cp : checkpoints) {
                        if (cp.latitude != 0.0 || cp.longitude != 0.0) {
                            LatLng position = new LatLng(cp.latitude, cp.longitude);

                            mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(cp.title)
                                    .snippet(cp.address));
                        }
                    }

                    mMap.setOnInfoWindowClickListener(marker -> {
                        for (Checkpoint cp : checkpoints) {
                            if (cp.title.equals(marker.getTitle())) {
                                Intent intent = new Intent(requireContext(), CheckpointDetailActivity.class);
                                intent.putExtra("checkpointId", cp.id);
                                startActivity(intent);
                                break;
                            }
                        }
                    });
                });
            }
        });
    }

    private void centerOnUser() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try {
            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (loc != null) {
                LatLng userLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
                return;
            }

        } catch (SecurityException ignored) {}

        LatLng toronto = new LatLng(43.6532, -79.3832);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toronto, 12f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
                centerOnUser();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
            loadCheckpointMarkers();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

}
