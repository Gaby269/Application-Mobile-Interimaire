package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ModificationCandidatureActivity extends AppCompatActivity {

    EditText editNom, editPrenom, editDescription;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_candidature);

        Intent i = getIntent();
        String is_details = i.getStringExtra("is_details");

        // PAS DE MODIFICATION DE CANDIDATURE

        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editDescription = findViewById(R.id.editDescription);

        editNom.setText("Nom du candidat");
        editPrenom.setText("Prénom du candidat");
        editDescription.setText("Description du candidat");

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(view -> {
            Intent i1;
            if (is_details.equals("true")) {
                i1 = new Intent(ModificationCandidatureActivity.this, AfficherDetailsCandidatureActivity.class);
            }
            else{
                i1 = new Intent(ModificationCandidatureActivity.this, NavbarActivity.class);
                i1.putExtra("fragment", "Candidature");
            }
            i1.putExtra("typeCompte", "Candidat");
            startActivity(i1);
        });
        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(ModificationCandidatureActivity.this,R.string.compteModif,Toast.LENGTH_SHORT).show();
            }
        });
    }
}