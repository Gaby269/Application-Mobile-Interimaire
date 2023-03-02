package com.example.tp2_lescapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Ex6Activity extends AppCompatActivity implements SensorEventListener {

    private ImageView mImageView;
    private TextView mProximityStatusText;

    private SensorManager mSensorManager;
    private Sensor mProximitySensor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex6);

        mImageView = findViewById(R.id.image_view);
        mProximityStatusText = findViewById(R.id.text_prox);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        // Aller a l'activité suivante
        Button buttonSuivant6 = findViewById(R.id.bouton_suivant_ex6);
        buttonSuivant6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(Ex6Activity.this, Ex7Activity.class);
                startActivity(iCal);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        if (distance < mProximitySensor.getMaximumRange()) {
            mImageView.setImageResource(R.drawable.near_image);
            mProximityStatusText.setText(R.string.proche);
        } else {
            mImageView.setImageResource(R.drawable.far_image);
            mProximityStatusText.setText(R.string.loin);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne rien faire
    }
}

