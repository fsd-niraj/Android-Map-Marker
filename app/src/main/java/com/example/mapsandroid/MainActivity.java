package com.example.mapsandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;

interface HandleBottomSheetBehaviour {
    public void handleClose();
}
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, HandleBottomSheetBehaviour {

    private GoogleMap googleMap;
    private Marker marker;
    private ArrayList<Marker> markers = new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapClickListener(this);
        this.refreshMarkers();
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation != null) {
                    LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 500), 100, null); // Adjust the zoom level as needed
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (this.marker != null) this.marker.remove();
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        this.marker = googleMap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(500)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 100, null);
        AddLocationFormSheet bottomSheetFragment = new AddLocationFormSheet(latLng.latitude, latLng.longitude, this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Location permission is required to use the map.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refreshMarkers() {
        Iterator<Marker> iterator = this.markers.iterator();
        while (iterator.hasNext()) {
            iterator.next().remove();
        }
        this.markers = new ArrayList<>();
        PreferenceManager preferenceManager = new PreferenceManager(this);
        ArrayList<com.example.mapsandroid.Location> locations = preferenceManager.getTodoList();
        for (com.example.mapsandroid.Location location : locations) {
            Log.d("MAIN", String.format("refreshMarkers: %f, %f", location.getLang(), location.getLat()));
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLang(), location.getLat()))
                    .title(location.getTitle())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            Marker newMarker = this.googleMap.addMarker(markerOptions);
            this.markers.add(newMarker);
        }
    }

    @Override
    public void handleClose() {
        this.refreshMarkers();
    }
}