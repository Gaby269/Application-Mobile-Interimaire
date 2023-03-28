package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OffresActivity extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offres);


        Button favorieButton = findViewById(R.id.star_button);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, FavorieActivity.class);
                startActivity(i);
            }
        });

        Button compteButton = findViewById(R.id.compte_button);
        compteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CompteActivity.class);
                startActivity(i);
            }
        });

        Button messagerieButton = findViewById(R.id.messagerie_button);
        messagerieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, MessagerieActivity.class);
                startActivity(i);
            }
        });

        Button candidatureButton = findViewById(R.id.candidature_button);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CandidatureActivity.class);
                startActivity(i);
            }
        });

        Button creationOffreButton = findViewById(R.id.create_button);
        creationOffreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CreationOffre1Activity.class);
                startActivity(i);
            }
        });



        Button offreButton = findViewById(R.id.layout_abo);
        offreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, AfficherDetailsOffreActivity.class);
                startActivity(i);
            }
        });

    }
}