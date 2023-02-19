package com.example.tp1_premireapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;


public class AffichageActivity extends AppCompatActivity {

    ///////////////////////////////
    // AFFICHAGE DES COORDONN2ES //
    ///////////////////////////////

    @SuppressLint({"ResourceType", "SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage);

        // Récuperer les données saisies
        Intent intent = getIntent();

        // Récuperation des id des TextView
        TextView nom_affichage = findViewById(R.id.nom_affichage);
        TextView prenom_affichage = findViewById(R.id.prenom_affichage);
        TextView age_affichage = findViewById(R.id.age_affichage);
        TextView tel_affichage = findViewById(R.id.tel_affichage);
        TextView comp_affichage = findViewById(R.id.comp_affichage);

        // Modifier le texte à partir des Extra
        nom_affichage.setText(intent.getStringExtra("nomAff"));
        prenom_affichage.setText(intent.getStringExtra("prenomAff"));
        age_affichage.setText(intent.getStringExtra("ageAff"));
        tel_affichage.setText(intent.getStringExtra("telAff"));
        comp_affichage.setText(intent.getStringExtra("compAff"));


        // Bouton pour retourner à la page d'acueil
        Button buttonRetour = findViewById(R.id.retour_bouton);
        buttonRetour.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Réafficher la saisie des données au départ
                Intent i = new Intent(AffichageActivity.this, MainActivity.class);
                startActivity(i);
            }

        });

        // Bouton pour dire okk
        Button buttonOk = findViewById(R.id.ok_button);
        buttonOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Réafficher la saisie des données au départ
                Intent i = new Intent(AffichageActivity.this, AppelActivity.class);
                TextView tel_affichage = findViewById(R.id.tel_affichage);
                i.putExtra("telAff", tel_affichage.getText().toString());
                startActivity(i);
            }

        });


        Button button_language = findViewById(R.id.bouton_langage);
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
