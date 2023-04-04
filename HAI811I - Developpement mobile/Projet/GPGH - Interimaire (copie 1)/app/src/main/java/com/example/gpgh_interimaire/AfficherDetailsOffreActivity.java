package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class AfficherDetailsOffreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_offre);

        Button retourButton = findViewById(R.id.boutton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsOffreActivity.this, OffresActivity.class);
                startActivity(i);
            }
        });

        Button postulerButton = findViewById(R.id.boutton_postuler);
        postulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsOffreActivity.this, PostulerActivity.class);
                startActivity(i);
            }
        });

    }
}