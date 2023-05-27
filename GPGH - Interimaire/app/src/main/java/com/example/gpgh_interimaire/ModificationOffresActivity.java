package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModificationOffresActivity extends AppCompatActivity {

    String TAG = "ModificationOffresActivity";
    FirebaseFirestore db;

    EditText editTitre, editType, editDescription, editDateDebut, editDateFin, editPrix, editParking;
    String id_offre, is_details;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_offres);

        Intent i = getIntent();
        is_details = i.getStringExtra("is_details");
        id_offre = i.getStringExtra("idOffre");
        
        db = FirebaseFirestore.getInstance();

        editTitre = findViewById(R.id.editTitre);
        editType = findViewById(R.id.editType);
        editDescription = findViewById(R.id.editDescription);
        editDateDebut = findViewById(R.id.editDateDebut);
        editDateFin = findViewById(R.id.editDateFin);
        editPrix = findViewById(R.id.editPrix);
        editParking = findViewById(R.id.editParking);
        
        fetchOffreInfo(id_offre);

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
                updateOffreInfo(id_offre);
            }
        });

        ImageButton suppressionButton = findViewById(R.id.image_delete);
        suppressionButton.setOnClickListener(view -> supprimerOffre(id_offre));
    }



    private void fetchOffreInfo(String offreId) {

        db.collection("offres")
                .document(offreId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String titre = documentSnapshot.getString("titre");
                        String type = documentSnapshot.getString("type");
                        String description = documentSnapshot.getString("description");
                        String dateDebut = documentSnapshot.getString("dateDeb");
                        String dateFin = documentSnapshot.getString("dateFin");
                        String prix = documentSnapshot.getString("remuneration");
                        String parking = documentSnapshot.getString("parking");

                        editTitre.setText(titre);
                        editType.setText(type);
                        editDescription.setText(description);
                        editDateDebut.setText(dateDebut);
                        editDateFin.setText(dateFin);
                        editPrix.setText(prix);
                        editParking.setText(parking);
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la récupération des infos de l'offre", e));
    }



    private void updateOffreInfo(String offreId) {

        displayLoadingScreen();

        String titre = editTitre.getText().toString();
        String type = editType.getText().toString();
        String description = editDescription.getText().toString();
        String dateDebut = editDateDebut.getText().toString();
        String dateFin = editDateFin.getText().toString();
        String prix = editPrix.getText().toString();
        String parking = editParking.getText().toString();

        // données de l'utilisateur
        db.collection("offres")
                .document(offreId)
                .update("titre", titre,
                        "type", type,
                        "description", description,
                        "dateDebut", dateDebut,
                        "dateFin", dateFin,
                        "prix", prix,
                        "parking", parking)
                .addOnSuccessListener(aVoid -> {
                    dismissLoadingScreen();
                    Toast.makeText(ModificationOffresActivity.this, R.string.compteModif, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ModificationOffresActivity.this, LoadingNavbarActivity.class);
                    i.putExtra("fragment", "Offres");
                    startActivity(i);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de la mise à jour des informations", Toast.LENGTH_SHORT).show());
    }


    private void supprimerOffre(String offreId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModificationOffresActivity.this);
        builder.setMessage(R.string.suppression_offre_message)
                .setTitle(R.string.suppression_offre_titre)
                .setPositiveButton(R.string.oui, (dialog, id) -> {
                    db.collection("offres").document(offreId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Offre supprimée avec succès : " + offreId);
                            Toast.makeText(ModificationOffresActivity.this, "Offre supprimée",Toast.LENGTH_SHORT).show();

                            // suppression des favoris de l'offre
                            db.collection("favoris")
                            .whereEqualTo("offreId", offreId)
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

                                    Intent i = new Intent(ModificationOffresActivity.this, NavbarActivity.class);
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


    public void displayLoadingScreen() {
        FragLoading loadingFragment = FragLoading.newInstance("Enregistrement...");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, loadingFragment, "loading_fragment");
        transaction.commit();
    }

    public void dismissLoadingScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragLoading loadingFragment = (FragLoading) fragmentManager.findFragmentByTag("loading_fragment");

        if (loadingFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(loadingFragment);
            transaction.commit();
        }
    }


}