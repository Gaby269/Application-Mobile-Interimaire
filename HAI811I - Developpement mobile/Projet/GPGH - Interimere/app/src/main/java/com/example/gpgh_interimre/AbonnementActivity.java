package com.example.gpgh_interimre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AbonnementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    List<String> listType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);


        Button inscriptionButton = findViewById(R.id.boutton_choixAbonnement);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AbonnementActivity.this, RecapPaiementActivity.class);
                startActivity(i);
            }
        });


        //Récupération du Spinner déclaré dans le fichier activity_inscription.xml
        Spinner typeAboSpinner = (Spinner) findViewById(R.id.typeAboSpinner);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        listType = new ArrayList<>();
        listType.add("Type d'abonnement");
        listType.add("Ponctuel (un abonnement)");
        listType.add("Mensuel");
        listType.add("Trimestriel");
        listType.add("Semestriel");
        listType.add("Annuel");
        listType.add("Renouvelable");

        // ArrayAdapter pour le spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, R.layout.spinner_item, listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item);
        typeAboSpinner.setAdapter(adapterSpinner); // on passe l'adapter au Spinner
    }


    // Si on appuie sur un endroit
    @Override
    public void onItemSelected(AdapterView<?> arg0,View arg1,int position,long id){
        // Affiche la selection
        Toast.makeText(getApplicationContext(), listType.get(position),Toast.LENGTH_LONG).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}