package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CandidaturesOffreActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatures_offre);

        // Affichage du titre
        Intent i = getIntent();
        String titreOffre = i.getStringExtra("titreOffre");
        String descriptionOffre = i.getStringExtra("description");
        TextView titreOffreTextView = findViewById(R.id.titreTextView);
        TextView descriptionOffreTextView = findViewById(R.id.descriptionTextView);
        titreOffreTextView.setText(titreOffre);
        descriptionOffreTextView.setText(descriptionOffre);

        RecyclerView recyclerView = findViewById(R.id.recycleviewEntreprise);

        List<ItemCandidature> items = new ArrayList<ItemCandidature>();
        items.add(new ItemCandidature("POINTEAU", "Gabrielle", "30", "Youtube", "CV1"));
        items.add(new ItemCandidature("Gatien", "HADDAD","200","Youtube", "CV1"));
        items.add(new ItemCandidature("Titre3", "CDD", "30","Youtube", "CV1"));
        items.add(new ItemCandidature("Titre4", "CDD", "30","Youtube", "CV1"));
        items.add(new ItemCandidature("Titre5", "Stage","1000","Youtube", "CV1"));
        items.add(new ItemCandidature("Titre1", "Stage","30","Youtube", "CV1"));
        items.add(new ItemCandidature("Titre1", "CDD", "30","Youtube", "CV1"));
        items.add(new ItemCandidature("Titre1", "Stage","50","Youtube", "CV1"));
        items.add(new ItemCandidature("Titre1", "CDD", "30","Youtube", "CV1"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapterCandidature(this, items));

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CandidaturesOffreActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", "Entreprise");
                startActivity(i);
            }
        });

    }

}