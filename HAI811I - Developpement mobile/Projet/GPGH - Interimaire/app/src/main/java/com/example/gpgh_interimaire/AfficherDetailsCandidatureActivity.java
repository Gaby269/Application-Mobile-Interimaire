package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        is_favori = false;

        TextView candidatureText = findViewById(R.id.candidatureDetailsTextView);
        if (typeCompte.equals("Candidat")) {
            candidatureText.setText("Details de votre candidature");
        }
        else {
            candidatureText.setText("Details de la candidature");
        }

        //String typeCompte = "Candidat";

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (typeCompte.equals("Candidat")) {
                    i = new Intent(AfficherDetailsCandidatureActivity.this, NavbarActivity.class);
                    i.putExtra("fragment", "Candidature");
                }
                else{
                    i = new Intent(AfficherDetailsCandidatureActivity.this, CandidaturesOffreActivity.class);
                }
                startActivity(i);
            }
        });

        ImageView modifierButton = findViewById(R.id.bouton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // modifier que si c'est candidat
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, ModificationCandidatureActivity.class);
                startActivity(i);
            }
        });

        ImageView supprimerButton = findViewById(R.id.bouton_supprimer);
        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // supprimer que si c'est candidat
                // boite de dialogue + affichage
                Toast.makeText(AfficherDetailsCandidatureActivity.this,R.string.candidatureSupp,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, NavbarActivity.class);
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
                }
                else{
                    is_favori = true;
                    favorieButton.setImageResource(R.drawable.icon_favori_white);
                }
            }
        });

        if (typeCompte.contains("Candidat")) {
            refuserButton.setVisibility(View.GONE);
            accepterButton.setVisibility(View.GONE);
            supprimerButton.setVisibility(View.VISIBLE);
            modifierButton.setVisibility(View.VISIBLE);
        }
        else if (typeCompte.contains("Entreprise") || typeCompte.contains("Agence")){
            refuserButton.setVisibility(View.VISIBLE);
            accepterButton.setVisibility(View.VISIBLE);
            supprimerButton.setVisibility(View.GONE);
            modifierButton.setVisibility(View.GONE);
        }
    }
}