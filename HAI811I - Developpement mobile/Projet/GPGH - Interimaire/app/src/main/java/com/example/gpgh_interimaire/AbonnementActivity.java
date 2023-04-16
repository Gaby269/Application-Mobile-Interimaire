package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AbonnementActivity extends AppCompatActivity {

    List<String> listType;
    Spinner typeAboSpinner;
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
        typeAboSpinner = (Spinner) findViewById(R.id.typeAboSpinner);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        listType = new ArrayList<>();
        listType.add(getString(R.string.type_abonnement));
        listType.add(getString(R.string.ponctuel));
        listType.add(getString(R.string.mensuelle));
        listType.add(getString(R.string.trimestrielle));
        listType.add(getString(R.string.semestrielle));
        listType.add(getString(R.string.annuelle));
        listType.add(getString(R.string.renouvelable));

        // ArrayAdapter pour le spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, R.layout.spinner_item, listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item);
        typeAboSpinner.setAdapter(adapterSpinner); // on passe l'adapter au Spinner

        typeAboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem == "Ponctuel") {
                    Fragment fragment = new FragAboPonctuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if (selectedItem == "Mensuelle"){
                    Fragment fragment = new FragAboMensuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if (selectedItem == "Trimestrielle"){
                    Fragment fragment = new FragAboTrimestriel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if (selectedItem == "Annuelle"){
                    Fragment fragment = new FragAboAnnuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if (selectedItem == "Semestrielle"){
                    Fragment fragment = new FragAboSemestriel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if (selectedItem == "Renouvelable"){
                    Fragment fragment = new FragAboRenouvelable();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne rien faire si aucun élément n'est sélectionné
            }
        });

    }


}