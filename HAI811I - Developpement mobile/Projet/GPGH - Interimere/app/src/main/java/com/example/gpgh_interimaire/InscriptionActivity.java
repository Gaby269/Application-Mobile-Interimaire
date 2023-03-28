package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);


        Button creaCompteButton = findViewById(R.id.boutton_creationCompte);
        creaCompteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InscriptionActivity.this, ConfirmationTelephoneActivity.class);
                startActivity(i);
            }
        });

        Button connexionButton = findViewById(R.id.boutton_connexion);
        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                startActivity(i);
            }
        });

        //Récupération du Spinner déclaré dans le fichier activity_inscription.xml
        Spinner typeCompteSpinner = (Spinner) findViewById(R.id.typeCompteSpinner);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        List<String> listType = new ArrayList<>();
        listType.add("Type de compte");
        listType.add("Entreprise");
        listType.add("Candidat");

        // ArrayAdapter pour le spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCompteSpinner.setAdapter(adapterSpinner); // on passe l'adapter au Spinner
    }
}