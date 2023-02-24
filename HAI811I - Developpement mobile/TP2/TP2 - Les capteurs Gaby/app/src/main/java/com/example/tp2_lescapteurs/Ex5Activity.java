package com.example.tp2_lescapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ex5Activity extends AppCompatActivity implements SensorEventListener {

    TextView flashValue, xValue, yValue, zValue;
    Sensor accelerometer;
    private boolean flashOn = false;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex5);

        //Récuperation des id des xml
        xValue = findViewById(R.id.tvX);
        yValue = findViewById(R.id.tvY);
        zValue = findViewById(R.id.tvZ);
        flashValue = findViewById(R.id.text_flash);


        //Recuperer les capteurs et notamment celui de l'acceleromettre
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
            }
        }


        // Aller a l'activité suivante avec le bouton suviant
        Button buttonSuivant5 = findViewById(R.id.bouton_suivant_ex5);
        buttonSuivant5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(Ex5Activity.this, Ex6Activity.class);
                startActivity(iCal);
            }
        });


    }

    @Override
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Recuperer les valeurs des capteurs
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Affichage des valeurs de l'accéléromètre sur les TextViews correspondants
        xValue.setText("X : " + String.format("%.3f", x));
        yValue.setText("Y : " + String.format("%.3f", y));
        zValue.setText("Z : " + String.format("%.3f", z));

        // Calculer la force de mouvement
        double gForce = Math.sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH;

        // EN fonction de cette force on change le flash
        if (gForce > 4.0) {
            toggleFlash();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignored for now
    }

    @SuppressLint("SetTextI18n")
    private void toggleFlash() {
        try {
            // Recuperer les données du flash
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];

            // Si Le flash est allumé
            if (flashOn) {
                cameraManager.setTorchMode(cameraId, false);
                flashOn = false;
                flashValue.setText("Off");
            }
            //Si le flash est étteint
            else {
                cameraManager.setTorchMode(cameraId, true);
                flashOn = true;
                flashValue.setText("On");
            }
        }
        //Si le changement ne marche pas
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            // Recuperer les données du flash
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            // Si Le flash est allumé
            if (flashOn) {
                cameraManager.setTorchMode(cameraId, false);
                flashOn = false;
                flashValue.setText("Off");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

