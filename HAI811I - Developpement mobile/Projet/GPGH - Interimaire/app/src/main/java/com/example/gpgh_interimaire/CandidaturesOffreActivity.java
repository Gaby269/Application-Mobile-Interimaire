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

import java.util.ArrayList;
import java.util.List;

public class CandidaturesOffreActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatures_offre);

        RecyclerView recyclerView = findViewById(R.id.recycleview);

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
                startActivity(i);
            }
        });

        LinearLayout candidatureButton = findViewById(R.id.layout_candidature);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CandidaturesOffreActivity.this, AfficherDetailsCandidatureActivity.class);
                startActivity(i);
            }
        });

    }

}