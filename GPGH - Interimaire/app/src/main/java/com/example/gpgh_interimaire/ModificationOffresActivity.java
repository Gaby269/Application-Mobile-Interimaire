package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ModificationOffresActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_offres);

        Intent i = getIntent();
        String is_details = i.getStringExtra("is_details");

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (is_details.equals("true")) {
                    i = new Intent(ModificationOffresActivity.this, AfficherDetailsOffreActivity.class);
                }
                else{
                    i = new Intent(ModificationOffresActivity.this, NavbarActivity.class);
                    i.putExtra("fragment", "Offre");
                }
                i.putExtra("typeCompte", "Entreprise");
                startActivity(i);
            }
        });
        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificationOffresActivity.this, NavbarActivity.class);
                i.putExtra("typeCompte", "Entreprise");
                i.putExtra("fragment", "Offre");
                startActivity(i);
                Toast.makeText(ModificationOffresActivity.this,R.string.offreModif,Toast.LENGTH_SHORT).show();
            }
        });
    }


}