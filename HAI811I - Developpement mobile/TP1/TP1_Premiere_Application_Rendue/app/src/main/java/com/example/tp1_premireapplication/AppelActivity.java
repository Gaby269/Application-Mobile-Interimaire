package com.example.tp1_premireapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.telecom.Call;

import java.text.BreakIterator;
import java.util.Locale;

public class AppelActivity extends AppCompatActivity {

    //////////////////////////////////
    // AFFICHAGE DE FINI DE VALIDER //
    //////////////////////////////////

    @SuppressLint({"ResourceType", "SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appel);

        // Récuperer les données saisies
        Intent i = getIntent();
        // Récuperation des id des TextView pour l'appel
        TextView affTel = findViewById(R.id.affichageTel);
        // Modifier le texte à partir des Extra pour l'appel
        affTel.setText(i.getStringExtra("telAff"));


        // Bouton pour retourner à la page d'acueil
        Button buttonAppel = findViewById(R.id.appel_button);
        buttonAppel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + affTel.getText().toString());
                Intent i_appel = new Intent(Intent.ACTION_CALL,uri);
                if (ActivityCompat.checkSelfPermission(AppelActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    String [] permissions = {
                            Manifest.permission.CALL_PHONE
                    };
                    requestPermissions(permissions,1000);
                    return;
                }
                startActivity(i_appel);
            }

        });


        // Bouton pour retourner à la page d'acueil
        Button buttonAccueil = findViewById(R.id.accueil_bouton);
        buttonAccueil.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //Réafficher la saisie des données au départ
                Intent i = new Intent(AppelActivity.this, MainActivity.class);
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
