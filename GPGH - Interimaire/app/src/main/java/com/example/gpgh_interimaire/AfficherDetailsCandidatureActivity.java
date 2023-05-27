package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AfficherDetailsCandidatureActivity extends AppCompatActivity {

    boolean is_favori;
    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_candidature);

        Intent i = getIntent();
        String typeCompte = i.getStringExtra("typeCompte");
        String idCandidature = i.getStringExtra("idCandidature");
        is_favori = false;

        // TODO Prendre l'id de la candidature et afficher de la base la candidature en entier
        TextView candidatureText = findViewById(R.id.candidatureDetailsTextView);
        if (typeCompte.equals("Candidat")) {
            candidatureText.setText("Details de votre candidature");
        }
        else {
            candidatureText.setText("Details de la candidature");
        }

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (typeCompte.equals("Candidat")) {
                    i = new Intent(AfficherDetailsCandidatureActivity.this, NavbarActivity.class);
                    i.putExtra("fragment", "Candidature");
                    i.putExtra("typeCompte", typeCompte);
                }
                else{
                    i = new Intent(AfficherDetailsCandidatureActivity.this, CandidaturesOffreActivity.class);
                    i.putExtra("typeCompte", typeCompte);
                }
                startActivity(i);
            }
        });

        ImageButton supprimerButton = findViewById(R.id.btn_supp);
        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // supprimer que si c'est candidat
                // boite de dialogue + affichage
                // TODO boite de dialogue pour être sur que on veut supprimer
                // TODO supprimer dna sla base la candidature
                Toast.makeText(AfficherDetailsCandidatureActivity.this,R.string.candidatureSupp,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Candidature");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });

        ImageView accepterButton = findViewById(R.id.bouton_accepter);
        accepterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // boite de dialogue + affichage
                // accepter que si c'est entreprise
                Toast.makeText(AfficherDetailsCandidatureActivity.this,R.string.candidatureAcc,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, CandidaturesOffreActivity.class);
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });

        ImageView refuserButton = findViewById(R.id.bouton_refuser);
        refuserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // refuser que si c'est entreprise
                // boite de dialogue + affichage
                Toast.makeText(AfficherDetailsCandidatureActivity.this,R.string.candidatureReff,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, CandidaturesOffreActivity.class);
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });

        ImageButton favorieButton = findViewById(R.id.btn_heart);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_favori){
                    is_favori = false;
                    favorieButton.setImageResource(R.drawable.icon_favori_white_vide);
                    // TODO enlever la candidature des favories
                }
                else{
                    is_favori = true;
                    favorieButton.setImageResource(R.drawable.icon_favori_white);
                    // TODO ajouter la candidature des favories
                }
            }
        });

        ImageButton modificationButton = findViewById(R.id.btn_modif);
        modificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, ModificationCandidatureActivity.class);
                i.putExtra("typeCompte", typeCompte);
                i.putExtra("is_details", "true");
                startActivity(i);
            }
        });

        // Visilité des boutons
        if (typeCompte.contains("Candidat")) {
            refuserButton.setVisibility(View.GONE);
            accepterButton.setVisibility(View.GONE);
            supprimerButton.setVisibility(View.VISIBLE);
            modificationButton.setVisibility(View.VISIBLE);
            favorieButton.setVisibility(View.GONE);
        }
        else if (typeCompte.contains("Entreprise") || typeCompte.contains("Agence")){
            refuserButton.setVisibility(View.VISIBLE);
            accepterButton.setVisibility(View.VISIBLE);
            supprimerButton.setVisibility(View.INVISIBLE);
            modificationButton.setVisibility(View.GONE);
            favorieButton.setVisibility(View.VISIBLE);
        }


    }




}