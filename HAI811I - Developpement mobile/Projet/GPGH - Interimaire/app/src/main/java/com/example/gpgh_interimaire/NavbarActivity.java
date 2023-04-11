package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class NavbarActivity extends AppCompatActivity {


    String showLinearLayout = ""; // Définir la variable showLinearLayout

    static final String TAG = "CompteActivity";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String typeCompte;
    private MyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        Intent i = getIntent();
        showLinearLayout = showLinearLayout+i.getStringExtra("typeCompte");

        Fragment fragment = new FragPageOffres();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_layout, fragment);
        transaction.addToBackStack(null); // ajouter à la pile de retour
        transaction.commit();


        // Gérer la nav bar
        LinearLayout favorieButton = findViewById(R.id.layout_favoris);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragPageFavoris();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        LinearLayout compteButton = findViewById(R.id.layout_compte);
        compteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragPageCompte();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        // Pour tout le monde
        LinearLayout messagerieButton = findViewById(R.id.layout_message);
        messagerieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FragPageMessagerie();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        // Bouton candidater dans le linear layout
        LinearLayout candidatureButton = findViewById(R.id.layout_candidature);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mesCandidatures si tu est un candidat
                //liste des offres et la candidature a voir si entreprise
                Intent i = new Intent(NavbarActivity.this, MesCandidaturesActivity.class);
                startActivity(i);
            }
        });

        // Que pour les entreprises et les agences qui peuvent créer les offres
        LinearLayout creationOffreButton = findViewById(R.id.layout_ajout);
        creationOffreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavbarActivity.this, CreationOffre1Activity.class);
                startActivity(i);
            }
        });

        // Mettre à jour la barre de navigation en fonction du type de compte de l'utilisateur
        if (typeCompte == "Candidat") {
        }
        else if (typeCompte == "Entreprise" || typeCompte == "Agence d'interim"){
            favorieButton.setVisibility(View.GONE);
        }
        else{
            Toast.makeText(NavbarActivity.this, ""+typeCompte+"",Toast.LENGTH_SHORT).show();
        }


    }


    // Deconnection
    private void logoutUser() {
        // Créez un AlertDialog pour demander confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(NavbarActivity.this);
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setTitle("Confirmation")
                .setPositiveButton("Oui", (dialog, id) -> {
                    // Si l'user confirme, déco
                    mAuth.signOut();
                    Intent intent = new Intent(NavbarActivity.this, MainActivity.class);
                    startActivity(intent);
                    //finish();
                })
                .setNegativeButton("Annuler", (dialog, id) -> {
                    // Si l'user annule
                    dialog.dismiss();
                });

        // Affichez l'AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

/*
    private void checkIfConnected() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Utilisateur pas connecté, redirect sur la page de connexion
            Intent i = new Intent(NavbarActivity.this, ConnexionActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfConnected();
    }*/
}