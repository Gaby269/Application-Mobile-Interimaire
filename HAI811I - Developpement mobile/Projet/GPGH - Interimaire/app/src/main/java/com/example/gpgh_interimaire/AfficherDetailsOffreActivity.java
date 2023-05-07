package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AfficherDetailsOffreActivity extends AppCompatActivity {
    boolean is_favori;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_offre);

        is_favori = false;

        Intent i = getIntent();
        String typeCompte = i.getStringExtra("typeCompte");
        String titreOffre = i.getStringExtra("titreOffre");
        String descriptionOffre = i.getStringExtra("descriptionOffre");


        Button postulerButton = findViewById(R.id.boutton_postuler);
        postulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsOffreActivity.this, PostulerActivity.class);
                i.putExtra("is_details", "true");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsOffreActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });
        ImageButton favorieButton = findViewById(R.id.btn_heart);
        favorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_favori){
                    is_favori = false;
                    favorieButton.setImageResource(R.drawable.icon_favori_white_vide);
                }
                else{
                    is_favori = true;
                    favorieButton.setImageResource(R.drawable.icon_favori_white);
                }
            }
        });
        // Visibilité du bouton favorie
        if (typeCompte.equals("Candidat")){
            favorieButton.setVisibility(View.VISIBLE);
        }
        else{
            favorieButton.setVisibility(View.GONE);
        }

        // Modification de l'offre
        ImageButton modificationButton = findViewById(R.id.btn_modif);
        modificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsOffreActivity.this, ModificationOffresActivity.class);
                i.putExtra("is_details", "true");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });
        // Visibilité du bouton favorie
        if (typeCompte.equals("Candidat")){
            modificationButton.setVisibility(View.GONE);
        }
        else{
            modificationButton.setVisibility(View.VISIBLE);
        }

        // Suppresion de l'offre
        ImageButton suppressionButton = findViewById(R.id.btn_supp);
        suppressionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AfficherDetailsOffreActivity.this,R.string.offreSupp,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AfficherDetailsOffreActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });
        // Visibilité du bouton favorie
        if (typeCompte.equals("Candidat")){
            suppressionButton.setVisibility(View.INVISIBLE);
        }
        else{
            suppressionButton.setVisibility(View.VISIBLE);
        }

    }
}