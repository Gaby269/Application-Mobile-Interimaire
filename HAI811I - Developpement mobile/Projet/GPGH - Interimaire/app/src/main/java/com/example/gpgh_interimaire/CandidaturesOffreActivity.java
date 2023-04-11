package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CandidaturesOffreActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatures_offre);

        Button retourButton = findViewById(R.id.boutton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CandidaturesOffreActivity.this, FragPageOffres.class);
                startActivity(i);
            }
        });

        LinearLayout candidatureButton = findViewById(R.id.layout_candidature);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CandidaturesOffreActivity.this, AfficherDetailsCandidatureActivity.class);
                startActivity(i);
            }
        });
    }

}