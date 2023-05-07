package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class NavbarActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String typeCompte, pageFragment = "";
    Fragment fragment;

    @Override
    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        // Récupération de l'intent
        Intent i = getIntent();
        pageFragment = i.getStringExtra("fragment"); // Pour savoir quel fragment
        typeCompte = i.getStringExtra("typeCompte");
        // typeCompte = "Candidat";

        // Transmettre le type de compte aux fragments
        Bundle args = new Bundle();
        args.putString("typeCompte", typeCompte);

        // Récupération des éléments de layout
        ImageView messagerieImage = findViewById(R.id.image_message);
        ImageView favorieImage = findViewById(R.id.image_favoris);
        ImageView compteImage = findViewById(R.id.image_compte);
        ImageView offresImage = findViewById(R.id.image_offres);
        ImageView candidatureImage = findViewById(R.id.image_candidature);
        ImageView ajoutImage = findViewById(R.id.image_ajout);

        // Gérer les fragment pour savoir lequel afficher
        if (pageFragment.equals("Compte")) {
            fragment = new FragPageCompte();
            compteImage.setImageResource(R.drawable.icon_compte_bleu);
        }
        else if (pageFragment.equals("Candidature")) {
            fragment = new FragPageMesCandidatures();
            candidatureImage.setImageResource(R.drawable.icon_fichier_bleu);
        }
        else if (pageFragment.equals("Message")){
            fragment = new FragPageMessagerie();
            messagerieImage.setImageResource(R.drawable.icon_message_bleu);
        }
        else if (pageFragment.equals("Favorie")){
            fragment = new FragPageFavoris();
            favorieImage.setImageResource(R.drawable.icon_favori_bleu);
        }
        else {
            fragment = new FragPageOffres();
            offresImage.setImageResource(R.drawable.icon_fichier_bleu);
        }
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_layout, fragment);
        transaction.addToBackStack(null); // ajouter à la pile de retour
        transaction.commit();


        // Bouton favorie
        LinearLayout favorieButton = findViewById(R.id.layout_favoris);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Modification de la messagerie en couleur
                favorieImage.setImageResource(R.drawable.icon_favori_bleu);

                // Remettre tout en noir
                compteImage.setImageResource(R.drawable.icon_compte_black);
                ajoutImage.setImageResource(R.drawable.icon_ajouter_black);
                candidatureImage.setImageResource(R.drawable.icon_formulaire_black);
                offresImage.setImageResource(R.drawable.icon_fichier_black);
                messagerieImage.setImageResource(R.drawable.icon_message_black);

                Fragment fragment = new FragPageFavoris();
                fragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        // Bouton offres
        LinearLayout offresButton = findViewById(R.id.layout_offres);
        offresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Modification de la messagerie en couleur
                offresImage.setImageResource(R.drawable.icon_fichier_bleu);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.icon_favori_black);
                ajoutImage.setImageResource(R.drawable.icon_ajouter_black);
                compteImage.setImageResource(R.drawable.icon_compte_black);
                candidatureImage.setImageResource(R.drawable.icon_formulaire_black);
                messagerieImage.setImageResource(R.drawable.icon_message_black);

                Fragment fragment = new FragPageOffres();
                fragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        // Bouton compte
        LinearLayout compteButton = findViewById(R.id.layout_compte);
        compteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Modification de la compte en couleur
                compteImage.setImageResource(R.drawable.icon_compte_bleu);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.icon_favori_black);
                ajoutImage.setImageResource(R.drawable.icon_ajouter_black);
                offresImage.setImageResource(R.drawable.icon_fichier_black);
                candidatureImage.setImageResource(R.drawable.icon_formulaire_black);
                messagerieImage.setImageResource(R.drawable.icon_message_black);

                Fragment fragment = new FragPageCompte();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        // Bouton messagerie
        LinearLayout messagerieButton = findViewById(R.id.layout_message);
        messagerieButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                // Modification de la messagerie en couleur
                messagerieImage.setImageResource(R.drawable.icon_message_bleu);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.icon_favori_black);
                ajoutImage.setImageResource(R.drawable.icon_ajouter_black);
                compteImage.setImageResource(R.drawable.icon_compte_black);
                offresImage.setImageResource(R.drawable.icon_fichier_black);
                candidatureImage.setImageResource(R.drawable.icon_formulaire_black);

                // Mettre le fragement correspondant
                Fragment fragment = new FragPageMessagerie();
                fragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        // Bouton candidature (Mes candidatures pour un candidat)
        LinearLayout candidatureButton = findViewById(R.id.layout_candidature);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Modification de l'image en couleur
                candidatureImage.setImageResource(R.drawable.icon_formulaire_bleu);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.icon_favori_black);
                ajoutImage.setImageResource(R.drawable.icon_ajouter_black);
                offresImage.setImageResource(R.drawable.icon_fichier_black);
                compteImage.setImageResource(R.drawable.icon_compte_black);
                messagerieImage.setImageResource(R.drawable.icon_message_black);

                Fragment fragment = new FragPageMesCandidatures();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        // Bouton ajout d'une offre
        LinearLayout ajoutButton = findViewById(R.id.layout_ajout);
        ajoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Modification de la messagerie en couleur
                ajoutImage.setImageResource(R.drawable.icon_ajouter_bleu);

                // Remettre tout en noir
                compteImage.setImageResource(R.drawable.icon_compte_black);
                favorieImage.setImageResource(R.drawable.icon_favori_black);
                candidatureImage.setImageResource(R.drawable.icon_formulaire_black);
                offresImage.setImageResource(R.drawable.icon_fichier_black);
                messagerieImage.setImageResource(R.drawable.icon_message_black);

                Intent i = new Intent(NavbarActivity.this, CreationOffreActivity.class);
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });


        // Mettre à jour la barre de navigation en fonction du type de compte de l'utilisateur
        if (typeCompte.contains("Candidat")) {
            favorieButton.setVisibility(View.VISIBLE);
            candidatureButton.setVisibility(View.VISIBLE);
            ajoutButton.setVisibility(View.GONE);
        }
        else if (typeCompte.contains("Entreprise") || typeCompte.contains("Agence")){
            favorieButton.setVisibility(View.GONE);
            candidatureButton.setVisibility(View.GONE);
            ajoutButton.setVisibility(View.VISIBLE);
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