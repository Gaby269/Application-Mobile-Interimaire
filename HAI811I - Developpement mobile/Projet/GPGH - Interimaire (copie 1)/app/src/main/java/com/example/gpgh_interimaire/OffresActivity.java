package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout;

public class OffresActivity extends AppCompatActivity {


    private String showLinearLayout; // Définir la variable showLinearLayout

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offres);


        showLinearLayout = getIntent().getStringExtra("showLinearLayout");


        LinearLayout favorieButton = findViewById(R.id.layout_favoris);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, FavorisActivity.class);
                startActivity(i);
            }
        });

        LinearLayout compteButton = findViewById(R.id.layout_compte);
        compteButton.setVisibility(showLinearLayout=="true" ? View.VISIBLE : View.GONE);
        compteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CompteActivity.class);
                startActivity(i);
            }
        });

        LinearLayout messagerieButton = findViewById(R.id.layout_message);
        messagerieButton.setVisibility(showLinearLayout=="true" ? View.VISIBLE : View.GONE);
        messagerieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, MessagerieActivity.class);
                startActivity(i);
            }
        });

        LinearLayout candidatureImage = findViewById(R.id.layout_candidature);
        candidatureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mesCandidatures si tu est un candidat
                //liste des offres et la candidature a voir si entreprise
                Intent i = new Intent(OffresActivity.this, MesCandidaturesActivity.class);
                startActivity(i);
            }
        });

        LinearLayout creationOffreImage = findViewById(R.id.layout_ajout);
        creationOffreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CreationOffre1Activity.class);
                startActivity(i);
            }
        });



        LinearLayout offreButton = findViewById(R.id.layout_abo);
        offreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, AfficherDetailsOffreActivity.class);
                startActivity(i);
            }
        });

        //aparait que si c'ets une entreprise
        Button candidatureButton = findViewById(R.id.boutton_candidature);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CandidaturesOffreActivity.class);
                startActivity(i);
            }
        });

    }
}