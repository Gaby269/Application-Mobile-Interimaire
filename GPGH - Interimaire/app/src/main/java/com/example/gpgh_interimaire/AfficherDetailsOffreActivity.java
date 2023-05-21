package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
    String id_offre, titre, typeCompte;

    TextView titreText, nomEntrepriseText, dateText, typeText, salaireText, descriptionText, rueText, complementAdresseText, codePostalText, villeText, parkingText, ticketText, teletravailText;

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_offre);

        is_favori = false;

        Intent i = getIntent();
        typeCompte = i.getStringExtra("typeCompte");
        id_offre = i.getStringExtra("idOffre");

        // Récupération des données de l'offre depuis la base de données
        db = FirebaseFirestore.getInstance();
        getInfoOffre();

        Button postulerButton = findViewById(R.id.boutton_postuler);
        postulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeCompte.equals("Candidat")) {
                    Intent i = new Intent(AfficherDetailsOffreActivity.this, PostulerActivity.class);
                    i.putExtra("idOffre", id_offre);
                    i.putExtra("titreOffre", titre);
                    i.putExtra("typeCompte", typeCompte);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(AfficherDetailsOffreActivity.this, CandidaturesOffreActivity.class);
                    i.putExtra("idOffre", id_offre);
                    i.putExtra("titreOffre", titre);
                    i.putExtra("typeCompte", typeCompte);
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
                // TODO
                // Intent i = new Intent(AfficherDetailsOffreActivity.this, ModificationOffresActivity.class);
                // i.putExtra("is_details", "true");
                // i.putExtra("typeCompte", typeCompte);
                // startActivity(i);
                Toast.makeText(AfficherDetailsOffreActivity.this, "Offre modifiée",Toast.LENGTH_SHORT).show();
            }
        });

        // Suppresion de l'offre
        ImageButton suppressionButton = findViewById(R.id.btn_supp);
        suppressionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supprimerOffre();
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


        titreText = findViewById(R.id.titre_offre);
        nomEntrepriseText = findViewById(R.id.entrepriseOffre);
        dateText = findViewById(R.id.dateOffre);
        typeText = findViewById(R.id.typeOffre);
        salaireText = findViewById(R.id.prixOffre);
        descriptionText = findViewById(R.id.detailsOffre);
        rueText = findViewById(R.id.adresseOffre);
        complementAdresseText = findViewById(R.id.complementOffre);
        codePostalText = findViewById(R.id.codePostaleOffre);
        parkingText = findViewById(R.id.parking);
        ticketText = findViewById(R.id.ticket_resto);
        teletravailText = findViewById(R.id.teletravail);

    }


    @SuppressLint("SetTextI18n")
    private void getInfoOffre() {

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
                    titre = documentSnapshot.getString("titre");
                    String type = documentSnapshot.getString("type");
                    String ville = documentSnapshot.getString("ville");                    

                    Boolean parking = documentSnapshot.getBoolean("parking");
                    Boolean teletravail = documentSnapshot.getBoolean("teletravail");
                    Boolean ticketResto = documentSnapshot.getBoolean("ticketResto");

                    // TODO changer la view selon les données récupérées
                    titreText.setText(titre);
                    nomEntrepriseText.setText(nomEntreprise);
                    dateText.setText(dateDeb+" - "+dateFin);
                    typeText.setText(type);
                    salaireText.setText(remuneration);
                    descriptionText.setText(description);
                    rueText.setText(rue);
                    if (complementAdresse != "") { // Si le complement d'adresse est vide alors on ne l'affiche aps
                        complementAdresseText.setText(complementAdresse);
                    }
                    else{
                        complementAdresseText.setVisibility(View.GONE);
                    }
                    codePostalText.setText(codePostal+" "+ville);
                    if (parking.equals(true)){
                        parkingText.setText("Parking disponible");
                    }
                    else{
                        parkingText.setText("Pas de place de parking");
                    }
                    if (ticketResto.equals(true)){
                        ticketText.setText("Ticket restaurant possible");
                    }
                    else{
                        ticketText.setText("Pas ticket restaurant");
                    }
                    if (teletravail.equals(true)){
                        teletravailText.setText("Télétravail possible");
                    }
                    else{
                        teletravailText.setText("Pas de télétravail");
                    }
                } 
                else {
                    Log.d(TAG, "Aucune offre trouvée avec l'ID : " + id_offre);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Erreur lors de la récupération de l'offre", e);
            });
        
    }

    
    private void supprimerOffre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AfficherDetailsOffreActivity.this);
        builder.setMessage(R.string.suppression_offre_message)
                .setTitle(R.string.suppression_offre_titre)
                .setPositiveButton(R.string.oui, (dialog, id) -> {
                    db.collection("offres").document(id_offre)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Offre supprimée avec succès : " + id_offre);
                            Toast.makeText(AfficherDetailsOffreActivity.this, "Offre supprimée",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AfficherDetailsOffreActivity.this, NavbarActivity.class);
                            i.putExtra("fragment", "Offre");
                            i.putExtra("typeCompte", "Entreprise");
                            startActivity(i);
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Erreur lors de la suppression de l'offre", e);
                        });
                })
                .setNegativeButton(R.string.annuler, (dialog, id) -> { dialog.dismiss(); });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}