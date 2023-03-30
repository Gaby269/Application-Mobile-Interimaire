package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        listType.add("Type d'abonnement");
        listType.add("Ponctuel (un abonnement)");
        listType.add("Mensuelle");
        listType.add("Trimestrielle");
        listType.add("Semestrielle");
        listType.add("Annuelle");
        listType.add("Renouvelable");

        // ArrayAdapter pour le spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, R.layout.spinner_item, listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item);
        typeAboSpinner.setAdapter(adapterSpinner); // on passe l'adapter au Spinner

        typeAboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem == "Ponctuel (un abonnement)") {
                    Fragment fragment = new FragmentPonctuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null); // ajouter à la pile de retour
                    transaction.commit();
                }
                else if (selectedItem == "Mensuelle"){
                    Fragment fragment = new FragmentMensuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null); // ajouter à la pile de retour
                    transaction.commit();
                }
                else if (selectedItem == "Trimestrielle"){
                    Fragment fragment = new FragmentTrimestriel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null); // ajouter à la pile de retour
                    transaction.commit();
                }
                else if (selectedItem == "Annuelle"){
                    Fragment fragment = new FragmentAnnuel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null); // ajouter à la pile de retour
                    transaction.commit();
                }
                else if (selectedItem == "Semestrielle"){
                    Fragment fragment = new FragmentSemestriel();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null); // ajouter à la pile de retour
                    transaction.commit();
                }
                else if (selectedItem == "Renouvelable"){
                    Fragment fragment = new FragmentRenouvelable();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_abonnement, fragment);
                    transaction.addToBackStack(null); // ajouter à la pile de retour
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