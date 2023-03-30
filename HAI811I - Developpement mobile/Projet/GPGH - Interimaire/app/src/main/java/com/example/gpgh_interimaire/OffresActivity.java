package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class OffresActivity extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offres);


        ImageView favorieButton = findViewById(R.id.star_button);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, FavorisActivity.class);
                startActivity(i);
            }
        });

        ImageView compteButton = findViewById(R.id.compte_button);
        compteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CompteActivity.class);
                startActivity(i);
            }
        });

        ImageView messagerieButton = findViewById(R.id.messagerie_button);
        messagerieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, MessagerieActivity.class);
                startActivity(i);
            }
        });

        ImageView candidatureImage = findViewById(R.id.candidature_button);
        candidatureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mesCandidatures si tu est un candidat
                //liste des offres et la candidature a voir si entreprise
                Intent i = new Intent(OffresActivity.this, MesCandidaturesActivity.class);
                startActivity(i);
            }
        });

        ImageView creationOffreImage = findViewById(R.id.create_button);
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