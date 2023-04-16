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

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_candidature);

        TextView candidatureText = findViewById(R.id.candidatureDetailsTextView);
        // si c'est une entreprise : details de la candidature (modifie le texte)
        // si c'est un candidat on laisse votre candidature


        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, NavbarActivity.class);
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
    }
}