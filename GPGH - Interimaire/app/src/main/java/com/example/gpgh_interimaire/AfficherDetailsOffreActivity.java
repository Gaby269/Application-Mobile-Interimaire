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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AfficherDetailsOffreActivity extends AppCompatActivity {

    String TAG = "AfficherDetailsOffreActivity";

    FirebaseFirestore db;
    String userId;

    boolean is_favori;
    boolean is_postule = false;
    String id_offre, titre, typeCompte, id_candidature;

    TextView titreText, nomEntrepriseText, dateText, typeText, salaireText, descriptionText, rueText, complementAdresseText, codePostalText, villeText, parkingText, ticketText, teletravailText;

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_offre);

        is_favori = false;

        Intent i = getIntent();
        typeCompte = i.getStringExtra("typeCompte");
        id_offre = i.getStringExtra("idOffre");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        
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
        
        getInfoOffre();
        
        Button postulerButton = findViewById(R.id.boutton_postuler);
        postulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeCompte.equals("Candidat")) {
                    if (is_postule) {
                        Intent i = new Intent(AfficherDetailsOffreActivity.this, AfficherDetailsCandidatureActivity.class);
                        i.putExtra("idCandidature", id_candidature);
                        i.putExtra("typeCompte", typeCompte);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(AfficherDetailsOffreActivity.this, PostulerActivity.class);
                        i.putExtra("idOffre", id_offre);
                        i.putExtra("titreOffre", titre);
                        i.putExtra("typeCompte", typeCompte);
                        startActivity(i);
                    }
                }
                else {
                    Intent i = new Intent(AfficherDetailsOffreActivity.this, CandidaturesOffreActivity.class);
                    i.putExtra("idOffre", id_offre);
                    i.putExtra("titreOffre", titre);
                    i.putExtra("typeCompte", typeCompte);
                    startActivity(i);
                }

            }
        });

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(view -> {
            Intent i1 = new Intent(AfficherDetailsOffreActivity.this, NavbarActivity.class);
            i1.putExtra("fragment", "Offre");
            i1.putExtra("typeCompte", typeCompte);
            startActivity(i1);
        });

        ImageButton favoriButton = findViewById(R.id.btn_heart);
        favoriButton.setOnClickListener(view -> {
            if (is_favori){
                is_favori = false;
                favoriButton.setImageResource(R.drawable.icon_favori_white_vide);
            }
            else{
                is_favori = true;
                favoriButton.setImageResource(R.drawable.icon_favori_white);
            }
        });

        // Modification de l'offre
        ImageButton modificationButton = findViewById(R.id.btn_modif);
        modificationButton.setOnClickListener(view -> {
            Intent i12 = new Intent(AfficherDetailsOffreActivity.this, ModificationOffresActivity.class);
            i12.putExtra("is_details", "true");
            i12.putExtra("typeCompte", typeCompte);
            i12.putExtra("idOffre", id_offre);
            startActivity(i12);
        });

        // Suppresion de l'offre
        ImageButton suppressionButton = findViewById(R.id.btn_supp);
        suppressionButton.setOnClickListener(view -> supprimerOffre());

        
        modificationButton.setVisibility(View.GONE); // Visibilité du bouton modification
        suppressionButton.setVisibility(View.GONE);  // Visibilité du bouton suppression

        // Texte du bouton postuler
        if (typeCompte.equals("Candidat")) { 
            checkIfAlreadyPostule(userId, id_offre, postulerButton);
        }
        else if (typeCompte.equals("Invite")) { 
            favoriButton.setVisibility(View.GONE);
            postulerButton.setVisibility(View.GONE); 
        }
        else { 
            favoriButton.setVisibility(View.GONE);
            postulerButton.setText("Voir les candidatures"); 
            checkIfOffreIsMine(userId, id_offre, modificationButton, suppressionButton);
        }

    }


    @SuppressLint("SetTextI18n")
    private void checkIfAlreadyPostule(String userId, String id_offre, Button postulerButton) {
        CollectionReference candidaturesRef = db.collection("candidatures");
        Query query = candidaturesRef.whereEqualTo("userId", userId).whereEqualTo("id_offre", id_offre);

        query.get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    id_candidature = queryDocumentSnapshots.getDocuments().get(0).getId();
                    is_postule = true;

                    postulerButton.setText("Voir ma candidature");
                }
                else {
                    postulerButton.setText("Postuler"); 
                }
            })
            .addOnFailureListener(e -> Log.d(TAG, "Erreur lors de la récupération de la candidature : " + id_offre));
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

                    String parking = documentSnapshot.getString("parking");
                    Boolean teletravail = documentSnapshot.getBoolean("teletravail");
                    Boolean ticketResto = documentSnapshot.getBoolean("ticketResto");

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
                    if (parking.equals("0") || parking.equals("") || parking.equals("0 place") || parking.equals("0 places")){
                        parkingText.setText("Parking non disponible");
                    }
                    else{
                        parkingText.setText(parking+" places");
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

                            // suppression des favoris de l'offre
                            db.collection("favoris")
                            .whereEqualTo("offreId", id_offre)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        // boucler sur tous les favoris de l'offre
                                        for (DocumentSnapshot fav : queryDocumentSnapshots.getDocuments()) {
                                            String id_fav = fav.getId();
                                            db.collection("favoris").document(id_fav)
                                                .delete()
                                                .addOnSuccessListener(aVoid1 -> {
                                                    Log.d(TAG, "Favori supprimé avec succès : " + id_fav);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e(TAG, "Erreur lors de la suppression du favori", e);
                                                });
                                        }
                                    }

                                    Intent i = new Intent(AfficherDetailsOffreActivity.this, NavbarActivity.class);
                                    i.putExtra("fragment", "Offre");
                                    i.putExtra("typeCompte", "Entreprise");
                                    startActivity(i);

                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Erreur lors de la récupération des favoris", e);
                                });

                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Erreur lors de la suppression de l'offre", e);
                        });
                })
                .setNegativeButton(R.string.annuler, (dialog, id) -> { dialog.dismiss(); });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void checkIfOffreIsMine(String userId, String offreId, ImageButton modificationButton, ImageButton suppressionButton) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("offres")
            .document(offreId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String createur = document.getString("createur");
                        if (createur.equals(userId)) {        
                            modificationButton.setVisibility(View.VISIBLE); // Visibilité du bouton modification
                            suppressionButton.setVisibility(View.VISIBLE); // Visibilité du bouton suppression
                        }
                    }
                }
                else {
                    // Erreur lors de la vérification
                    Log.d(TAG, "Erreur lors de la vérification du créateur de l'offre : ", task.getException());
                }
            });

    }
}