package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AfficherDetailsCandidatureActivity extends AppCompatActivity {

    String TAG = "AfficherDetailsCandidatureActivity";

    FirebaseFirestore db;
    boolean is_favori = false;
    
    String typeCompte, idCandidature, userId, firstName, lastName, description, etat, nomOffre, nomEntreprise;

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_details_candidature);
        
        Intent i = getIntent();
        idCandidature = i.getStringExtra("idCandidature");
        typeCompte = i.getStringExtra("typeCompte");
        
        db = FirebaseFirestore.getInstance();

        setCandidatureInfos();

        TextView candidatureText = findViewById(R.id.candidatureDetailsTextView);
        if (typeCompte.equals("Candidat")) {
            candidatureText.setText("Details de votre candidature");
        }
        else {
            candidatureText.setText("Details de la candidature");
        }


        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (typeCompte.equals("Candidat")) {
                    i = new Intent(AfficherDetailsCandidatureActivity.this, NavbarActivity.class);
                    i.putExtra("fragment", "Candidature");
                    i.putExtra("typeCompte", typeCompte);
                }
                else{
                    i = new Intent(AfficherDetailsCandidatureActivity.this, CandidaturesOffreActivity.class);
                    i.putExtra("typeCompte", typeCompte);
                }
                startActivity(i);
            }
        });

        ImageButton supprimerButton = findViewById(R.id.btn_supp);
        supprimerButton.setOnClickListener(view -> supprimerCandidature(idCandidature));

        ImageView accepterButton = findViewById(R.id.bouton_accepter);
        accepterButton.setOnClickListener(view -> {
            updateCandidature(true);
        });

        ImageView refuserButton = findViewById(R.id.bouton_refuser);
        refuserButton.setOnClickListener(view -> {
            updateCandidature(false);
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

        ImageButton modificationButton = findViewById(R.id.btn_modif);
        modificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfficherDetailsCandidatureActivity.this, ModificationCandidatureActivity.class);
                i.putExtra("typeCompte", typeCompte);
                i.putExtra("is_details", "true");
                startActivity(i);
            }
        });

        // Visibilité des boutons
        modificationButton.setVisibility(View.GONE);
        if (typeCompte.contains("Candidat")) {
            refuserButton.setVisibility(View.GONE);
            accepterButton.setVisibility(View.GONE);
            favoriButton.setVisibility(View.GONE);
        }
        else {
            supprimerButton.setVisibility(View.GONE);
        }

    }


    private void setCandidatureInfos() {

        TextView nomCandidat = findViewById(R.id.nomCandidat);
        TextView offreCandidature = findViewById(R.id.offreCandidature);
        TextView descriptionCandidature = findViewById(R.id.descriptionCandidature);
        TextView cvCandidature = findViewById(R.id.cvCandidature);
        TextView etatCandidature = findViewById(R.id.etatCandidature);

        // récupération du nom de l'offre
        db.collection("candidatures")
            .document(idCandidature)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                String offreId = documentSnapshot.getString("id_offre");
                userId = documentSnapshot.getString("userId");
                description = documentSnapshot.getString("description");
                etat = documentSnapshot.getString("etat");

                descriptionCandidature.setText(description);
                etatCandidature.setText(etat);

                db.collection("offres")
                    .document(offreId)
                    .get()
                    .addOnSuccessListener(documentSnapshotOffre -> {
                        nomOffre = documentSnapshotOffre.getString("titre");
                        nomEntreprise = documentSnapshotOffre.getString("nomEntreprise");

                        if (typeCompte.equals("Candidat")) { 
                            offreCandidature.setText(nomEntreprise); 
                            nomCandidat.setText(nomOffre);
                        }
                        else { 
                            db.collection("users")
                            .document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshotUser -> {
                                firstName = documentSnapshotUser.getString("prenom");
                                lastName = documentSnapshotUser.getString("nom");
                                nomCandidat.setText(firstName + " " + lastName);
                                offreCandidature.setText(nomOffre); 
                            });
                        }
                    });

                // récupération du CV
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference fileRef = storageRef.child("CV/" + userId + "/");

                fileRef.listAll()
                    .addOnSuccessListener(listResult -> {
                        for (StorageReference item : listResult.getItems()) {
                            String nomCV = item.getName();

                            // récupérer l'URL de téléchargement
                            item.getDownloadUrl().addOnSuccessListener(uri -> {
                                String cvUrl = uri.toString();
                                cvCandidature.setText(nomCV);
                                cvCandidature.setTextColor(ContextCompat.getColor(this, R.color.bleu_500));

                                // télécharger le fichier lorsque l'on clique dessus
                                cvCandidature.setOnClickListener(v -> {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(cvUrl), "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    Intent chooser = Intent.createChooser(intent, "Open with");

                                    // Vérifie qu'il existe une application pouvant gérer l'intent
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(chooser);
                                    } else {
                                        Toast.makeText(this, "Please install a PDF reader", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }).addOnFailureListener(exception -> {
                                cvCandidature.setText(R.string.no_cv);
                            });
                            return;
                        }
                        cvCandidature.setText(R.string.no_cv);
                    })
                    .addOnFailureListener(exception -> cvCandidature.setText(R.string.no_cv));
                    
            });
    }


    private void updateCandidature(boolean choix) {
        // update de "etat" dans la base de données
        String etat;
        if (choix) { etat = "Acceptée"; }
        else { etat = "Refusée"; }

        db.collection("candidatures")
            .document(idCandidature)
            .update("etat", etat)
            .addOnSuccessListener(aVoid -> {
                if (choix) {Toast.makeText(AfficherDetailsCandidatureActivity.this,R.string.candidatureAcc,Toast.LENGTH_SHORT).show();}
                else {Toast.makeText(AfficherDetailsCandidatureActivity.this,R.string.candidatureReff,Toast.LENGTH_SHORT).show();}
        
                Intent i1 = new Intent(AfficherDetailsCandidatureActivity.this, CandidaturesOffreActivity.class);
                i1.putExtra("typeCompte", typeCompte);
                startActivity(i1);
                finish();
            });

    }


    private void supprimerCandidature(String candidatureId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AfficherDetailsCandidatureActivity.this);
        builder.setMessage(R.string.suppression_candidature_message)
                .setTitle(R.string.suppression_candidature_titre)
                .setPositiveButton(R.string.oui, (dialog, id) -> {
                    db.collection("candidatures")
                        .document(candidatureId)
                        .delete();
                            Toast.makeText(AfficherDetailsCandidatureActivity.this, R.string.candidatureSupp, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AfficherDetailsCandidatureActivity.this, NavbarActivity.class);
                            i.putExtra("fragment", "Candidature");
                            i.putExtra("typeCompte", typeCompte);
                            startActivity(i);
                })
                .setNegativeButton(R.string.annuler, (dialog, id) -> { dialog.dismiss(); });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}