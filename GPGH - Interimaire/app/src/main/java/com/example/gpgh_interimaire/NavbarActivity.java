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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

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
        if (!(i.hasExtra("typeCompte") && i.hasExtra("fragment"))) {
            Intent IgoToLoading = new Intent(NavbarActivity.this, LoadingNavbarActivity.class);
            startActivity(IgoToLoading);
        }
        pageFragment = i.getStringExtra("fragment"); // Pour savoir sur quel fragment
        typeCompte = i.getStringExtra("typeCompte");

        // Transmettre le type de compte aux fragments
        Bundle args = new Bundle();
        args.putString("typeCompte", typeCompte);



        // Récupération des éléments de layout
        ImageView messagerieImage = findViewById(R.id.image_message);
        ImageView favoriImage = findViewById(R.id.image_favoris);
        ImageView compteImage = findViewById(R.id.image_compte);
        ImageView offresImage = findViewById(R.id.image_offres);
        ImageView candidatureImage = findViewById(R.id.image_candidature);
        ImageView ajoutImage = findViewById(R.id.image_ajout);
        ImageView connexionImage = findViewById(R.id.image_connexion);
        ImageView statistiqueImage = findViewById(R.id.image_statistique);



        // Gérer les fragment pour savoir lequel afficher
        switch (pageFragment) {
            case "Compte":
                fragment = new FragPageCompte();
                compteImage.setImageResource(R.drawable.icon_compte_bleu);
                break;
            case "Candidature":
                fragment = new FragPageMesCandidatures();
                candidatureImage.setImageResource(R.drawable.icon_fichier_bleu);
                break;
            case "Message":
                fragment = new FragPageMessagerie();
                messagerieImage.setImageResource(R.drawable.icon_message_bleu);
                break;
            case "Favoris":
                fragment = new FragPageFavoris();
                favoriImage.setImageResource(R.drawable.icon_favori_bleu);
                break;
            default:
                fragment = new FragPageOffres();
                offresImage.setImageResource(R.drawable.icon_fichier_bleu);
                break;
        }

        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_layout, fragment);
        transaction.addToBackStack(null); // ajouter à la pile de retour
        transaction.commit();


        // Bouton favoris
        LinearLayout favoriButton = findViewById(R.id.layout_favoris);
        favoriButton.setOnClickListener(view -> {
            Fragment fragment = new FragPageFavoris();
            ImageView[] otherImages = {compteImage, ajoutImage, candidatureImage, offresImage, messagerieImage, connexionImage, statistiqueImage};
            int[] otherImagesResources = {R.drawable.icon_compte_black, R.drawable.icon_ajouter_black, R.drawable.icon_formulaire_black, R.drawable.icon_fichier_black, R.drawable.icon_message_black, R.drawable.icon_connexion_black, R.drawable.icon_statistique_black};
            updateUI(args, fragment, favoriImage, R.drawable.icon_favori_bleu, otherImages, otherImagesResources);
        });

        // Bouton offres
        LinearLayout offresButton = findViewById(R.id.layout_offres);
        offresButton.setOnClickListener(view -> {
            Fragment fragment = new FragPageOffres();
            ImageView[] otherImages = {favoriImage, ajoutImage, compteImage, candidatureImage, messagerieImage, connexionImage, statistiqueImage};
            int[] otherImagesResources = {R.drawable.icon_favori_black, R.drawable.icon_ajouter_black, R.drawable.icon_compte_black, R.drawable.icon_formulaire_black, R.drawable.icon_message_black, R.drawable.icon_connexion_black, R.drawable.icon_statistique_black};
            updateUI(args, fragment, offresImage, R.drawable.icon_fichier_bleu, otherImages, otherImagesResources);
        });

        // Bouton compte
        LinearLayout compteButton = findViewById(R.id.layout_compte);
        compteButton.setOnClickListener(view -> {
            Fragment fragment = new FragPageCompte();
            ImageView[] otherImages = {favoriImage, ajoutImage, offresImage, candidatureImage, messagerieImage, connexionImage, statistiqueImage};
            int[] otherImagesResources = {R.drawable.icon_favori_black, R.drawable.icon_ajouter_black, R.drawable.icon_fichier_black, R.drawable.icon_formulaire_black, R.drawable.icon_message_black, R.drawable.icon_connexion_black, R.drawable.icon_statistique_black};
            updateUI(args, fragment, compteImage, R.drawable.icon_compte_bleu, otherImages, otherImagesResources);
        });

        // Bouton messagerie
        LinearLayout messagerieButton = findViewById(R.id.layout_message);
        messagerieButton.setOnClickListener(view -> {
            Fragment fragment = new FragPageMessagerie();
            ImageView[] otherImages = {favoriImage, ajoutImage, offresImage, candidatureImage, compteImage, connexionImage, statistiqueImage};
            int[] otherImagesResources = {R.drawable.icon_favori_black, R.drawable.icon_ajouter_black, R.drawable.icon_fichier_black, R.drawable.icon_formulaire_black, R.drawable.icon_compte_black, R.drawable.icon_connexion_black, R.drawable.icon_statistique_black};
            updateUI(args, fragment, messagerieImage, R.drawable.icon_message_bleu, otherImages, otherImagesResources);
        });

        // Bouton candidature (Mes candidatures pour un candidat)
        LinearLayout candidatureButton = findViewById(R.id.layout_candidature);
        candidatureButton.setOnClickListener(view -> {
            Fragment fragment = new FragPageMesCandidatures();
            ImageView[] otherImages = {favoriImage, ajoutImage, offresImage, compteImage, messagerieImage, connexionImage, statistiqueImage};
            int[] otherImagesResources = {R.drawable.icon_favori_black, R.drawable.icon_ajouter_black, R.drawable.icon_fichier_black, R.drawable.icon_compte_black, R.drawable.icon_message_black, R.drawable.icon_connexion_black, R.drawable.icon_statistique_black};
            updateUI(args, fragment, candidatureImage, R.drawable.icon_formulaire_bleu, otherImages, otherImagesResources);
        });

        // Bouton statistique
        LinearLayout statisqtiqueButton = findViewById(R.id.layout_stat);
        offresButton.setOnClickListener(view -> {
            Fragment fragment = new FragPageOffres();
            ImageView[] otherImages = {favoriImage, ajoutImage, compteImage, candidatureImage, messagerieImage, connexionImage, statistiqueImage};
            int[] otherImagesResources = {R.drawable.icon_favori_black, R.drawable.icon_ajouter_black, R.drawable.icon_compte_black, R.drawable.icon_formulaire_black, R.drawable.icon_message_black, R.drawable.icon_connexion_black, R.drawable.icon_statistique_black};
            updateUI(args, fragment, offresImage, R.drawable.icon_fichier_bleu, otherImages, otherImagesResources);
        });

        // Bouton ajout d'une offre
        LinearLayout ajoutButton = findViewById(R.id.layout_ajout);
        ajoutButton.setOnClickListener(view -> {
            ajoutImage.setImageResource(R.drawable.icon_ajouter_bleu);
            compteImage.setImageResource(R.drawable.icon_compte_black);
            favoriImage.setImageResource(R.drawable.icon_favori_black);
            candidatureImage.setImageResource(R.drawable.icon_formulaire_black);
            offresImage.setImageResource(R.drawable.icon_fichier_black);
            messagerieImage.setImageResource(R.drawable.icon_message_black);
            connexionImage.setImageResource(R.drawable.icon_connexion_black);
            statistiqueImage.setImageResource(R.drawable.icon_statistique_black);

            Intent i1 = new Intent(NavbarActivity.this, CreationOffreActivity.class);
            i1.putExtra("typeCompte", typeCompte);
            startActivity(i1);
        });

        // Bouton connexion pour les invités
        LinearLayout connexionButton = findViewById(R.id.layout_connexion);
        connexionButton.setOnClickListener(view -> {
            ajoutImage.setImageResource(R.drawable.icon_ajouter_black);
            compteImage.setImageResource(R.drawable.icon_compte_black);
            favoriImage.setImageResource(R.drawable.icon_favori_black);
            candidatureImage.setImageResource(R.drawable.icon_formulaire_black);
            offresImage.setImageResource(R.drawable.icon_fichier_black);
            messagerieImage.setImageResource(R.drawable.icon_message_black);
            connexionImage.setImageResource(R.drawable.icon_connexion_bleu);
            statistiqueImage.setImageResource(R.drawable.icon_statistique_black);

            Intent i1 = new Intent(NavbarActivity.this, ConnexionActivity.class);
            startActivity(i1);
        });

        LinearLayout statistiqueButton = findViewById(R.id.layout_stat);
        statistiqueButton.setOnClickListener(view -> {
            Fragment fragment = new FragPageOffres();
            ImageView[] otherImages = {favoriImage, ajoutImage, compteImage, offresImage, candidatureImage, messagerieImage, connexionImage};
            int[] otherImagesResources = {R.drawable.icon_favori_black, R.drawable.icon_ajouter_black, R.drawable.icon_compte_black, R.drawable.icon_fichier_black, R.drawable.icon_formulaire_black, R.drawable.icon_message_black, R.drawable.icon_connexion_black};
            updateUI(args, fragment, statistiqueImage, R.drawable.icon_statistique_bleu, otherImages, otherImagesResources);
        });

        //bouttons tous en gone par défaut (utilisateur invité)
        ajoutButton.setVisibility(View.GONE);
        favoriButton.setVisibility(View.GONE);
        candidatureButton.setVisibility(View.GONE);
        connexionButton.setVisibility(View.GONE);
        statistiqueButton.setVisibility(View.GONE);
        compteButton.setVisibility(View.GONE);
        messagerieButton.setVisibility(View.GONE);

        // Mettre à jour la barre de navigation en fonction du type de compte de l'utilisateur
        if (typeCompte.contains("Candidat")) {
            favoriButton.setVisibility(View.VISIBLE);
            candidatureButton.setVisibility(View.VISIBLE);
            compteButton.setVisibility(View.VISIBLE);
            messagerieButton.setVisibility(View.VISIBLE);
        }
        else if (typeCompte.contains("Entreprise") || typeCompte.contains("Agence")) {
            ajoutButton.setVisibility(View.VISIBLE);
            compteButton.setVisibility(View.VISIBLE);
            messagerieButton.setVisibility(View.VISIBLE);
        }
        else if (typeCompte.contains("Admin")) {
            statistiqueButton.setVisibility(View.VISIBLE);
            compteButton.setVisibility(View.VISIBLE);
        }
        else { // invité
            connexionButton.setVisibility(View.VISIBLE);
        }


    }


    private void updateUI(Bundle args, Fragment fragment, ImageView activeImage, int activeImageResource, ImageView[] otherImages, int[] otherImagesResources) {
        // Mettre l'image active en couleur
        activeImage.setImageResource(activeImageResource);
    
        // Remettre les autres images en noir
        for (int i = 0; i < otherImages.length; i++) {
            otherImages[i].setImageResource(otherImagesResources[i]);
        }
    
        // Afficher le fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(args);
        transaction.replace(R.id.fragment_container_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}