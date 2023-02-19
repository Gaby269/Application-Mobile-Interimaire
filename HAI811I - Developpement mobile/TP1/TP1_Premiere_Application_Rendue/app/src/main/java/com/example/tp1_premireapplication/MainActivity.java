package com.example.tp1_premireapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /////////////////////
    // PARTIE POUR XML //
    /////////////////////
    @SuppressLint({"ResourceType", "SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Valider avec un bouton et action lors d'un click
        Button buttonTrain = findViewById(R.id.bouton_train);
        buttonTrain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iTrain = new Intent(MainActivity.this, TrainActivity.class);
                startActivity(iTrain);
            }
        });

        // Valider avec un bouton et action lors d'un click
        Button buttonSaisie = findViewById(R.id.bouton_saisie);
        buttonSaisie.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iSaisie = new Intent(MainActivity.this, SaisieActivity.class);
                startActivity(iSaisie);
            }
        });

        // Valider avec un bouton et action lors d'un click
        Button buttonCalendrier = findViewById(R.id.bouton_agenda);
        buttonCalendrier.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity.this, CalendrierActivity.class);
                startActivity(iCal);
            }
        });


        Button button_language = findViewById(R.id.button_language);
        button_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageToLoad;
                if (Locale.getDefault().getLanguage() == "fr") {
                    languageToLoad= "en";
                } else {
                    languageToLoad= "fr";
                }
                Toast.makeText(MainActivity.this, R.string.changed_language, Toast.LENGTH_SHORT).show();
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                recreate();
            }
        });


    }


}


