package com.example.tp2_lescapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Ex3Activity extends AppCompatActivity implements SensorEventListener {

    private TextView xValue, yValue, zValue;

    private View background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex3);


        //Récuperation des id des xml
        xValue = findViewById(R.id.x_value);
        yValue = findViewById(R.id.y_value);
        zValue = findViewById(R.id.z_value);
        background = findViewById(R.id.background);


        //Recuperer les capteurs et notamment celui de l'acceleromettre
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, getString(R.string.unavailable_acc), Toast.LENGTH_SHORT).show();
            }
        }


        Button buttonSuivant3 = findViewById(R.id.bouton_suivant_ex3);
        buttonSuivant3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(Ex3Activity.this, MainActivity.class);
                startActivity(iCal);
            }
        });
    }



    @Override
    @SuppressLint("DefaultLocale")
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Recuperer les valeurs des capteurs
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Modification des données
        xValue.setText(String.format("X: %.2f", x));
        yValue.setText(String.format("Y: %.2f", y));
        zValue.setText(String.format("Z: %.2f", z));

        // Calculer les couleurs de fond et lui attribuer des valeurs
        int colorX = getColorForValue(x, -10f, 10f);
        int colorY = getColorForValue(y, -10f, 10f);
        int colorZ = getColorForValue(z, 0f, 20f);
        int backgroundColor = getBackgroundColor(colorX, colorY, colorZ);

        // Modifier la couleur du fond
        background.setBackgroundColor(backgroundColor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    // Détermine la catégorie (0 = fuchsia, 1 = vert, 2 = bleu électrique) d'une valeur donnée
    private int getCategoryForValue(float value, float minValue, float maxValue) {
        float thresholdLow = 2.5f;
        float thresholdHigh = 7.5f;
        if (value < (minValue + thresholdLow)) {
            return 0;
        } else if (value > (maxValue - thresholdHigh)) {
            return 2;
        } else {
            return 1;
        }
    }

    // Détermine la couleur à utiliser pour une valeur donnée
    private int getColorForValue(float value, float minValue, float maxValue) {
        int category = getCategoryForValue(value, minValue, maxValue);

        if (category == 1) {
            return Color.rgb(90, 0, 75);
        } else if (category == 0) {
            return Color.rgb(0, 105, 14);
        } else {
            return Color.rgb(0, 39, 181); //bleu electrique
        }
    }

    // Détermine la couleur de fond à utiliser en fonction des couleurs des axes X, Y et Z
    private int getBackgroundColor(int colorX, int colorY, int colorZ) {
        int red = Math.max(colorX, Math.max(colorY, colorZ));
        int green = Math.min(colorX, Math.min(colorY, colorZ));
        int blue = (colorX + colorY + colorZ) - red - green;

        return Color.rgb(red, green, blue);
    }

}





