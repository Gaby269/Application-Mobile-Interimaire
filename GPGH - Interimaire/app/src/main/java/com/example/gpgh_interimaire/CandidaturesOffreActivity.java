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

        // Récupération de l'intent
        Intent i = getIntent();
        String typeCompte = i.getStringExtra("typeCompte");
        String titreOffre = i.getStringExtra("titreOffre");

        // Affichage du titre et de la description
        TextView titreOffreTextView = findViewById(R.id.titreTextView);
        TextView descriptionOffreTextView = findViewById(R.id.descriptionTextView);
        titreOffreTextView.setText(titreOffre);

        RecyclerView recyclerView = findViewById(R.id.recycleviewEntreprise);

        List<ItemCandidature> items = new ArrayList<ItemCandidature>();
        items.add(new ItemCandidature("0", "1", "John", "Doe", "Expérience en tant que magasinier", "Disponible immédiatement", "cv_john_doe.pdf"));
        items.add(new ItemCandidature("0", "2", "Jane", "Smith", "Compétences en secrétariat et gestion administrative", "Bilingue français-anglais", "cv_jane_smith.pdf"));
        items.add(new ItemCandidature("0", "3", "Michael", "Johnson", "Expérience en tant que chauffeur-livreur", "Permis de conduire valide", "cv_michael_johnson.pdf"));
        items.add(new ItemCandidature("0", "4", "Emily", "Brown", "Compétences en marketing digital", "Maîtrise des réseaux sociaux", "cv_emily_brown.pdf"));
        items.add(new ItemCandidature("0", "5", "Daniel", "Davis", "Expérience en tant que technicien informatique", "Certification Cisco CCNA", "cv_daniel_davis.pdf"));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapterCandidature(this, items, typeCompte));

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CandidaturesOffreActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });

    }

}