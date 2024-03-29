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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AbonnementActivity extends AppCompatActivity {

    List<String> listType;
    Spinner typeAboSpinner;
    int prix;
    int multiplicateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);

        typeAboSpinner = (Spinner) findViewById(R.id.typeAboSpinner);
        
        
        listType = new ArrayList<>();
        listType.add(getString(R.string.ponctuel));
        listType.add(getString(R.string.mensuel));
        listType.add(getString(R.string.trimestriel));
        listType.add(getString(R.string.semestriel));
        listType.add(getString(R.string.annuel));
        listType.add(getString(R.string.renouvelable));

        Fragment fragment = new FragAboPonctuel();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_abonnement, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        prix = 10;
        multiplicateur = 1;


        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, R.layout.spinner_item, listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item);
        typeAboSpinner.setAdapter(adapterSpinner);

        typeAboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setTag(position);
                int selectedPosition = (int) view.getTag();

                if (selectedPosition == 0) {
                    Fragment fragment = new FragAboPonctuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    prix = 10;
                    multiplicateur = 1;
                }
                else if (selectedPosition == 1) {
                    Fragment fragment = new FragAboMensuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    prix = 200;
                    multiplicateur = 1;
                }
                else if (selectedPosition == 2) {
                    Fragment fragment = new FragAboTrimestriel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    prix = 150;
                    multiplicateur = 3;
                }
                else if (selectedPosition == 3) {
                    Fragment fragment = new FragAboSemestriel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    prix = 120;
                    multiplicateur = 6;
                }
                else if (selectedPosition == 4) {
                    Fragment fragment = new FragAboAnnuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    prix = 1200;
                    multiplicateur = 1;
                }
                else if (selectedPosition == 5) {
                    Fragment fragment = new FragAboRenouvelable();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    prix = 1000;
                    multiplicateur = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        Button inscriptionButton = findViewById(R.id.boutton_choixAbonnement);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String typeAbo = typeAboSpinner.getSelectedItem().toString();
                Intent i = new Intent(AbonnementActivity.this, RecapPaiementActivity.class);
                i.putExtra("typeAbo", typeAbo);
                i.putExtra("prix", prix);
                i.putExtra("multiplicateur", multiplicateur);
                startActivity(i);
            }
        });
        
    }
}
        