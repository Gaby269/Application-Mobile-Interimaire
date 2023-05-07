package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PostulerActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postuler);

        Intent i = getIntent();
        String is_details = i.getStringExtra("is_details");
        String titreOffre = i.getStringExtra("titreOffre");
        String typeCompte = i.getStringExtra("typeCompte");
        String descriptionOffre = i.getStringExtra("description");

        TextView titreOffreTextView = findViewById(R.id.offreTextView);
        titreOffreTextView.setText(titreOffre);

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (is_details.equals("true")){
                    i = new Intent(PostulerActivity.this, AfficherDetailsOffreActivity.class);
                    i.putExtra("typeCompte", typeCompte);
                    i.putExtra("description", descriptionOffre);
                    i.putExtra("titreOffre", titreOffre);
                }
                else{
                    i = new Intent(PostulerActivity.this, NavbarActivity.class);
                    i.putExtra("fragment", "Offre");
                    i.putExtra("typeCompte", typeCompte);
                }
                startActivity(i);
            }
        });

        Button envoyerButton = findViewById(R.id.boutton_envoyer);
        envoyerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostulerActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });

    }
}