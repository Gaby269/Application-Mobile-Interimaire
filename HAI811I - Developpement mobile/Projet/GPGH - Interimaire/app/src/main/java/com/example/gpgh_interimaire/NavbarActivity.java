package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    String typeCompte;

    @Override
    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        Intent i = getIntent();
        typeCompte = i.getStringExtra("typeCompte");

        Bundle args = new Bundle();
        args.putString("typeCompte", typeCompte);

        Fragment fragment = new FragPageOffres();
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_layout, fragment);
        transaction.addToBackStack(null); // ajouter à la pile de retour
        transaction.commit();

        ImageView messagerieImage = findViewById(R.id.image_message);
        ImageView favorieImage = findViewById(R.id.image_favoris);
        ImageView compteImage = findViewById(R.id.image_compte);
        ImageView offresImage = findViewById(R.id.image_offres);
        ImageView candidatureImage = findViewById(R.id.image_candidature);

        offresImage.setImageResource(R.drawable.description_black_24dp_black);


        // Gérer la nav bar
        LinearLayout favorieButton = findViewById(R.id.layout_favoris);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Modification de la messagerie en couleur
                favorieImage.setImageResource(R.drawable.favorite_black_24dp);

                // Remettre tout en noir
                compteImage.setImageResource(R.drawable.account_circle_black_24dp);
                candidatureImage.setImageResource(R.drawable.content_paste_black_24dp);
                offresImage.setImageResource(R.drawable.description_black_24dp);
                messagerieImage.setImageResource(R.drawable.textsms_black_24dp);

                Fragment fragment = new FragPageFavoris();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });

        LinearLayout offresButton = findViewById(R.id.layout_offres);
        offresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Modification de la messagerie en couleur
                offresImage.setImageResource(R.drawable.description_black_24dp_black);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.favorite_border_black_24dp);
                compteImage.setImageResource(R.drawable.account_circle_black_24dp);
                candidatureImage.setImageResource(R.drawable.content_paste_black_24dp);
                messagerieImage.setImageResource(R.drawable.textsms_black_24dp);


                Bundle args = new Bundle();
                args.putString("typeCompte", typeCompte);

                Fragment fragment = new FragPageOffres();
                fragment.setArguments(args);
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

                // Modification de la compte en couleur
                compteImage.setImageResource(R.drawable.account_circle_black_24dp_black);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.favorite_border_black_24dp);
                offresImage.setImageResource(R.drawable.description_black_24dp);
                candidatureImage.setImageResource(R.drawable.content_paste_black_24dp);
                messagerieImage.setImageResource(R.drawable.textsms_black_24dp);

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
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                // Modification de la messagerie en couleur
                messagerieImage.setImageResource(R.drawable.textsms_black_24dp_black);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.favorite_border_black_24dp);
                compteImage.setImageResource(R.drawable.account_circle_black_24dp);
                offresImage.setImageResource(R.drawable.description_black_24dp);
                candidatureImage.setImageResource(R.drawable.content_paste_black_24dp);

                // Mettre le fragement correspondant
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
                // Modification de la compte en couleur
                candidatureImage.setImageResource(R.drawable.content_paste_search_black_24dp_black);

                // Remettre tout en noir
                favorieImage.setImageResource(R.drawable.favorite_border_black_24dp);
                offresImage.setImageResource(R.drawable.description_black_24dp);
                compteImage.setImageResource(R.drawable.account_circle_black_24dp);
                messagerieImage.setImageResource(R.drawable.textsms_black_24dp);

                Fragment fragment = new FragPageMesCandidatures();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_layout, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }
        });


        // Mettre à jour la barre de navigation en fonction du type de compte de l'utilisateur
        if (typeCompte.contains("Candidat")) {
            favorieButton.setVisibility(View.VISIBLE);
            candidatureButton.setVisibility(View.VISIBLE);
        }
        else if (typeCompte.contains("Entreprise") || typeCompte.contains("Agence")){
            favorieButton.setVisibility(View.GONE);
            candidatureButton.setVisibility(View.GONE);
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