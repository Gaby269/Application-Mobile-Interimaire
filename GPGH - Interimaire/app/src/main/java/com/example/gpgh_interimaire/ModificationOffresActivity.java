package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ModificationOffresActivity extends AppCompatActivity {

    EditText editTitre,editType,editDescription,editDateDebut,editDateFin,editDateLieu,editDatePrix,editDateDetails;

    String id_offre, is_details;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_offres);

        Intent i = getIntent();
        is_details = i.getStringExtra("is_details");
        id_offre = i.getStringExtra("idOffre");

        // TODO récuperer les informations de l'offres d'après son id
        // TODO Changer les edit pour que ce soit associé
        editTitre = findViewById(R.id.editTitre);
        editType = findViewById(R.id.editType);
        editDescription = findViewById(R.id.editDescription);
        editDateDebut = findViewById(R.id.editDateDebut);
        editDateFin = findViewById(R.id.editDateFin);
        editDateLieu = findViewById(R.id.editLieu);
        editDatePrix = findViewById(R.id.editPrix);
        editDateDetails = findViewById(R.id.editDetails);


        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (is_details.equals("true")) {
                    i = new Intent(ModificationOffresActivity.this, AfficherDetailsOffreActivity.class);
                    i.putExtra("idOffre", id_offre);
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
                // TODO Modifier les ifnromations dan sla ase de données et sur l'écran
                Toast.makeText(ModificationOffresActivity.this,R.string.offreModif,Toast.LENGTH_SHORT).show();
            }
        });
    }


}