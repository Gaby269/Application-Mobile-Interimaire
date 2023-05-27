package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CandidaturesOffreActivity extends AppCompatActivity {

    String TAG = "CandidaturesOffreActivity";

    FirebaseFirestore db;
    RecyclerView recyclerView;

    String typeCompte, titreOffre, offreId;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatures_offre);

        // Récupération de l'intent
        Intent i = getIntent();
        offreId = i.getStringExtra("idOffre");
        titreOffre = i.getStringExtra("titreOffre");
        typeCompte = i.getStringExtra("typeCompte");

        db = FirebaseFirestore.getInstance();

        // Affichage du titre
        TextView titreOffreTextView = findViewById(R.id.titreTextView);
        titreOffreTextView.setText(titreOffre);

        recyclerView = findViewById(R.id.recycleviewEntreprise);

        // cacher le layout layout_recherche
        LinearLayout layout_recherche = findViewById(R.id.layout_recherche);
        layout_recherche.setVisibility(View.GONE);

        List<ItemCandidature> items = new ArrayList<ItemCandidature>();
        // items.add(new ItemCandidature("0", "1", "John", "Doe", "Expérience en tant que magasinier", "Disponible immédiatement", "cv_john_doe.pdf"));
        // items.add(new ItemCandidature("0", "2", "Jane", "Smith", "Compétences en secrétariat et gestion administrative", "Bilingue français-anglais", "cv_jane_smith.pdf"));
        // items.add(new ItemCandidature("0", "3", "Michael", "Johnson", "Expérience en tant que chauffeur-livreur", "Permis de conduire valide", "cv_michael_johnson.pdf"));
        // items.add(new ItemCandidature("0", "4", "Emily", "Brown", "Compétences en marketing digital", "Maîtrise des réseaux sociaux", "cv_emily_brown.pdf"));
        // items.add(new ItemCandidature("0", "5", "Daniel", "Davis", "Expérience en tant que technicien informatique", "Certification Cisco CCNA", "cv_daniel_davis.pdf"));
        
        //Récupérer les candidatures de la base de données
        db.collection("candidatures")
            .whereEqualTo("id_offre", offreId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    // Vérifier si la QuerySnapshot n'est pas null ou vide
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String documentId = document.getId();
                            //Récupérer les données de l'offre
                            String userId = document.getString("userId");
                            String description = document.getString("description");
                            String etat = document.getString("etat");

                            // Récupérer les données de l'utilisateur à partir de la collection "users"
                            DocumentReference docRef = db.collection("users").document(userId);
                            docRef.get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    DocumentSnapshot document2 = task2.getResult();
                                    if (document2.exists()) {
                                        String nom = document2.getString("nom");
                                        String prenom = document2.getString("prenom");

                                        // récupération du CV
                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                        StorageReference fileRef = storageRef.child("CV/" + userId + "/");

                                        fileRef.listAll()
                                        .addOnSuccessListener(listResult -> {
                                            if (listResult.getItems().size() > 0) {
                                                Log.d(TAG, "CV récupéré");
                                                String nomCV = listResult.getItems().get(0).getName();

                                                items.add(new ItemCandidature(documentId, userId, prenom, nom, description, etat, nomCV));
                                                setupRecyclerView(items);
                                            }
                                            else {
                                                Log.d(TAG, "Aucun CV trouvé");
                                                items.add(new ItemCandidature(documentId, userId, prenom, nom, description, etat, "Pas de CV"));
                                                setupRecyclerView(items);
                                            }
                                        });

                                    }
                                    else {
                                        Log.d(TAG, "Aucun utilisateur ne correspond à cet identifiant");
                                    }

                                }
                                else {
                                    Log.d(TAG, "Erreur lors de la récupération de l'utilisateur : ", task2.getException());
                                }
                            });
                        }
                    }
                    else { // La QuerySnapshot est nul
                        Log.d(TAG, "Aucune candidature trouvée");
                        setupRecyclerView(items);
                    }
                }
                else {
                    Log.d(TAG, "Erreur lors de la récupération des offres : ", task.getException());
                }
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "Erreur lors de la récupération des offres : ", e);
                setupRecyclerView(items);
            });


        Button btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> showFilterDialog());

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


    private void setupRecyclerView(List<ItemCandidature> items) {
        if (items.isEmpty()) {
            items.add(new ItemCandidature("0", "0", "Oh", "oh", "Pas encore de candidature pour ce poste", "", "Réessayez plus tard"));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapterCandidature(this, items, typeCompte));
    }


    @SuppressLint("MissingInflatedId")
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrer");

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        // Récupérez les références des éléments de filtrage dans la vue
        EditText etMinPrice = dialogView.findViewById(R.id.etMinPrice);
        EditText etMaxPrice = dialogView.findViewById(R.id.etMaxPrice);

        // Ajoutez les boutons "Appliquer" et "Annuler"
        builder.setPositiveButton("Appliquer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Récupérez les valeurs sélectionnées dans les éléments de filtrage
                String minPrice = etMinPrice.getText().toString();
                String maxPrice = etMaxPrice.getText().toString();
            }
        });

        SeekBar seekbarPrice = dialogView.findViewById(R.id.seekbarPriceMin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekbarPrice.setMin(0); // Valeur minimale de la plage
        }
        seekbarPrice.setMax(100); // Valeur maximale de la plage

        seekbarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Mettez à jour les valeurs affichées en fonction de la position de la SeekBar
                int minPrice = progress; // Par exemple, valeur minimale en euros
                int maxPrice = seekBar.getMax(); // Par exemple, valeur maximale en euros

                // Affichez les valeurs sélectionnées pour le prix minimum et maximum
                // Vous pouvez les afficher dans des TextView ou toute autre vue de votre choix
                // Pour l'exemple, affichons-les dans la console
                Log.d("Candidature", "Prix minimum : " + minPrice);
                Log.d("Candidature", "Prix maximum : " + maxPrice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Actions à effectuer lorsque l'utilisateur commence à glisser la SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Actions à effectuer lorsque l'utilisateur arrête de glisser la SeekBar
            }
        });

        builder.setNegativeButton("Annuler", null);

        // Affichez la boîte de dialogue
        builder.create().show();
    }


}