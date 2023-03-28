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
                Intent i = new Intent(OffresActivity.this, FavorieActivity.class);
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

        ImageView candidatureButton = findViewById(R.id.candidature_button);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffresActivity.this, CandidatureActivity.class);
                startActivity(i);
            }
        });

        ImageView creationOffreButton = findViewById(R.id.create_button);
        creationOffreButton.setOnClickListener(new View.OnClickListener() {
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

    }
}