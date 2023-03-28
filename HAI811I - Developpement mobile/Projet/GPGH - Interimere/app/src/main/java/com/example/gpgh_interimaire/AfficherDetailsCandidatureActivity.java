package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AfficherDetailsCandidatureActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_candidature);

        Button retourButton = findViewById(R.id.boutton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, CandidatureActivity.class);
                startActivity(i);
            }
        });

        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, ModificationCandidatureActivity.class);
                startActivity(i);
            }
        });

        Button supprimerButton = findViewById(R.id.boutton_supprimer);
        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // boite de dialogue + affichage
                Toast.makeText(AfficherDetailsCandidatureActivity.this,R.string.candidatureSupp,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, CandidatureActivity.class);
                startActivity(i);
            }
        });
    }
}