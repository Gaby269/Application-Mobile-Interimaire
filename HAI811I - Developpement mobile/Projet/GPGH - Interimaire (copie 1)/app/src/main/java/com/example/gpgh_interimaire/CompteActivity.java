package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CompteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        Button retourButton = findViewById(R.id.boutton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompteActivity.this, OffresActivity.class);
                startActivity(i);
            }
        });

        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompteActivity.this, ModificationCompteActivity.class);
                startActivity(i);
            }
        });

        Button supprimerButton = findViewById(R.id.boutton_supprimer);
        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompteActivity.this,R.string.boutton_supprimer,Toast.LENGTH_SHORT).show();
                // affichage d'une boite de dialogue pour confirmer
            }
        });
    }
}