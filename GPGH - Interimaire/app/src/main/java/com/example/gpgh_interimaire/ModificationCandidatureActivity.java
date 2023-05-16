package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ModificationCandidatureActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_candidature);

        Intent i = getIntent();
        String is_details = i.getStringExtra("is_details");

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (is_details.equals("true")) {
                    i = new Intent(ModificationCandidatureActivity.this, AfficherDetailsCandidatureActivity.class);
                }
                else{
                    i = new Intent(ModificationCandidatureActivity.this, NavbarActivity.class);
                    i.putExtra("fragment", "Candidature");
                }
                i.putExtra("typeCompte", "Candidat");
                startActivity(i);
            }
        });
        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificationCandidatureActivity.this, AfficherDetailsCandidatureActivity.class);
                i.putExtra("typeCompte", "Candidat");
                startActivity(i);
                Toast.makeText(ModificationCandidatureActivity.this,R.string.compteModif,Toast.LENGTH_SHORT).show();
            }
        });
    }
}