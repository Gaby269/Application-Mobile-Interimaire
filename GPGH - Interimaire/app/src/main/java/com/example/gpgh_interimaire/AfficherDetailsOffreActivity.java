package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AfficherDetailsOffreActivity extends AppCompatActivity {

    String TAG = "AfficherDetailsOffreActivity";

    FirebaseFirestore db;
    boolean is_favori;
    
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_offre);

        is_favori = false;

        Intent i = getIntent();
        String typeCompte = i.getStringExtra("typeCompte");
        String id_offre = i.getStringExtra("idOffre");

        // Récupération des données de l'offre depuis la base de données
        db = FirebaseFirestore.getInstance();
        getInfoOffre(id_offre);

        Button postulerButton = findViewById(R.id.boutton_postuler);
        postulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeCompte.equals("Candidat")) {
                    Intent i = new Intent(AfficherDetailsOffreActivity.this, PostulerActivity.class);
                    i.putExtra("typeCompte", typeCompte);
                    i.putExtra("idOffre", id_offre);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(AfficherDetailsOffreActivity.this, CandidaturesOffreActivity.class);
                    i.putExtra("typeCompte", typeCompte);
                    i.putExtra("idOffre", id_offre);
                    startActivity(i);
                }

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

        // Visibilité du bouton favori
        if (typeCompte.equals("Candidat")) { favorieButton.setVisibility(View.VISIBLE); }
        else { favorieButton.setVisibility(View.GONE); }

        // Visibilité du bouton modification
        if (typeCompte.equals("Candidat")) { modificationButton.setVisibility(View.GONE); }
        else { modificationButton.setVisibility(View.VISIBLE); }

        // Visibilité du bouton suppression
        if (typeCompte.equals("Candidat")) { suppressionButton.setVisibility(View.INVISIBLE); }
        else { suppressionButton.setVisibility(View.VISIBLE); }

        // Visibilité du bouton postuler
        if (typeCompte.equals("Candidat")) {postulerButton.setText("Postuler");}
        else {postulerButton.setText("Voir les candidatures");}

    }


    private void getInfoOffre(String id_offre) {

        CollectionReference offresRef = db.collection("offres");
        Query query = offresRef.whereEqualTo(FieldPath.documentId(), id_offre);
        
        query.get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "Offre récupérée avec succès : " + id_offre);

                    // Récupération de toutes les données de l'offre
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    String codePostal = documentSnapshot.getString("codePostal");
                    String complementAdresse = documentSnapshot.getString("complement"); // Peut être vide !
                    String createur = documentSnapshot.getString("createur"); // ID de l'entreprise
                    String dateDeb = documentSnapshot.getString("dateDeb");
                    String dateFin = documentSnapshot.getString("dateFin");
                    String description = documentSnapshot.getString("description");
                    String nomEntreprise = documentSnapshot.getString("nomEntreprise");
                    String remuneration = documentSnapshot.getString("remuneration");
                    String rue = documentSnapshot.getString("rue");
                    String titre = documentSnapshot.getString("titre");
                    String type = documentSnapshot.getString("type");
                    String ville = documentSnapshot.getString("ville");                    

                    // "true" ou "false"
                    Boolean parking = documentSnapshot.getBoolean("parking");
                    Boolean teletravail = documentSnapshot.getBoolean("teletravail");
                    Boolean ticketResto = documentSnapshot.getBoolean("ticketResto");

                    // TODO changer la view selon les données récupérées

        
                } 
                else {
                    Log.d(TAG, "Aucune offre trouvée avec l'ID : " + id_offre);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Erreur lors de la récupération de l'offre", e);
            });
        
    }
}