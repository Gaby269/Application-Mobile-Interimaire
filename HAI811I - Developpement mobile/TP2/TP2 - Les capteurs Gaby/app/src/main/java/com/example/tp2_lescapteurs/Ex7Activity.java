package com.example.tp2_lescapteurs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;


public class Ex7Activity extends AppCompatActivity implements LocationListener {

    private MapView map;
    private LocationManager locationManager;
    private TextView tvLat, tvLong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation de la configuration pour OpenStreetMap
        org.osmdroid.config.Configuration.getInstance().load(getApplicationContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_ex7);

        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLon);


        // Récupération de la vue de la carte
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Récupération du service de géolocalisation
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Vérification des permissions de géolocalisation
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Demande des permissions de géolocalisation si elles ne sont pas accordées
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        else {
            // Démarrage du service de géolocalisation
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            double latitude = 46.2276;
            double longitude = 2.2137;

            // Afficher les coordonnées dans un TextView
            //tvLat.setText(Double.toString(latitude));
            //tvLong.setText(Double.toString(longitude));

            // Affichage des coordonnées géographiques de l'utilisateur sur la carte
            GeoPoint ma_loc = new GeoPoint(latitude, longitude);
            map.getController().setCenter(ma_loc);
            map.getController().setZoom(6.0);
        }

        Button buttonSuivant7 = findViewById(R.id.bouton_suivant_ex7);
        buttonSuivant7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(Ex7Activity.this, MainActivity.class);
                startActivity(iCal);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        // Récupération des coordonnées géographiques de l'utilisateur
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Afficher les coordonnées dans un TextView
        tvLat.setText(Double.toString(latitude));
        tvLong.setText(Double.toString(longitude));

        // Affichage des coordonnées géographiques de l'utilisateur sur la carte
        GeoPoint ma_loc = new GeoPoint(latitude, longitude);
        map.getController().setCenter(ma_loc);
        map.getController().setZoom(17.0);

        ArrayList<OverlayItem> pins = new ArrayList<>();
        OverlayItem les_pins = new OverlayItem("position", "refresh", ma_loc);
        Drawable m = les_pins.getMarker(0);
        pins.add(les_pins);

        ItemizedOverlayWithFocus<OverlayItem> myOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(), pins, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });

        myOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(myOverlay);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Démarrage du service de géolocalisation
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } else {
                // Affichage d'un message d'erreur si les permissions de géolocalisation ne sont pas accordées
                Toast.makeText(this, "Permission de géolocalisation refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Location recupererLocalisation() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            turnOnGPS();
        }

        // Check if any providers are available
        List<String> providers = locationManager.getProviders(true);
        if (providers.isEmpty()) {
            Toast.makeText(this, "No location providers available", Toast.LENGTH_SHORT).show();
            return null;
        }

        // Get last known location from available providers
        Location lastKnownLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(GeolocalizationActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GeolocalizationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null && (lastKnownLocation == null || location.getTime() > lastKnownLocation.getTime())) {
                    lastKnownLocation = location;
                }
            }
        }

        // If no last known location, try to get a new one
        if (lastKnownLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if (bestProvider != null) {
                locationManager.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // Remove location updates to save battery
                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {}

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                });
                lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
            }
        }

        return lastKnownLocation;
    }

    private void turnOnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.wake_geo)).setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        map.onResume();
    }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
