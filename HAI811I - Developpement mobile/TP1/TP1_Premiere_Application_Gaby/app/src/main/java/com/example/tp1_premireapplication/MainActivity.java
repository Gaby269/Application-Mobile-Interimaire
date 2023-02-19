package com.example.tp1_premireapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
        buttonTrain.setBackgroundColor(R.color.rouge_900);
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
        buttonSaisie.setBackgroundColor(R.color.jaune_900);
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
        buttonCalendrier.setBackgroundColor(R.color.bleu_900);
        buttonCalendrier.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity.this, CalendrierActivity.class);
                startActivity(iCal);
            }
        });


        Button button_language = findViewById(R.id.bouton_langage);
        button_language.setBackgroundColor(R.color.black);
        button_language.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String languageToLoad;
                if (Locale.getDefault().getLanguage() == "en") {
                    languageToLoad= "fr";
                } else {
                    languageToLoad= "en";
                }
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


