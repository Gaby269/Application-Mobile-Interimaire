package com.example.gpgh_interimaire;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FragPageOffres extends Fragment {

    static final String TAG = "FragPageOffres";
    FirebaseFirestore db;

    RecyclerView recyclerView;
    String typeCompte;

    public FragPageOffres() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.frag_page_offres, container, false);
        recyclerView = view.findViewById(R.id.recycleview);
  
        db = FirebaseFirestore.getInstance();

        // Récupérer les arguments du bundle
        assert getArguments() != null;
        typeCompte = getArguments().getString("typeCompte");

        getOffres("");

        ImageView search_button = view.findViewById(R.id.search_button);
        search_button.setOnClickListener(v -> {
            EditText bar_recherche = view.findViewById(R.id.bar_recherche);
            String filtre = bar_recherche.getText().toString();
            getOffres(filtre);
        });

        return view;
    }


    private void getOffres(String filtre) {        
        
        List<ItemOffre> items = new ArrayList<ItemOffre>();

        Log.d(TAG, "Récupération des offres");
        //Récupérer les offres de la base de données
        db.collection("offres")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) { // Vérifier si le QuerySnapshot n'est pas nul
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String documentId = document.getId();
                            //Récupérer les données de l'offre
                            String dateDeb = document.getString("dateDeb");
                            String dateFin = document.getString("dateFin");
                            String nameEntreprise = document.getString("nomEntreprise");
                            String codePostal = document.getString("codePostal");
                            String remuneration = document.getString("remuneration");
                            String titre = document.getString("titre");
                            String type = document.getString("type");
                            String description = document.getString("description");
                            if (filtre.equals("")) {
                                //Ajouter l'offre à la liste des offres
                                items.add(new ItemOffre(documentId, titre, type, remuneration, nameEntreprise, dateDeb, dateFin, codePostal));
                                Log.d(TAG, "offre récupérée");
                            }
                            else {
                                // boucle sur les sous chaine du filtre, et si une sous chaine est contenue dans le titre de l'offre, on l'ajoute à la liste
                                String[] sousChaines = filtre.split(" ");
                                for (String sousChaine : sousChaines) {
                                    if (titre.contains(sousChaine) || type.contains(sousChaine) || nameEntreprise.contains(sousChaine) || codePostal.contains(sousChaine) || description.contains(sousChaine)) {
                                        //Ajouter l'offre à la liste des offres
                                        items.add(new ItemOffre(documentId, titre, type, remuneration, nameEntreprise, dateDeb, dateFin, codePostal));
                                        Log.d(TAG, "offre récupérée");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else { // Le QuerySnapshot est nul
                        Log.d(TAG, "La collection est vide");
                    }
                }
                else {
                    Log.d(TAG, "Erreur lors de la récupération des offres : ", task.getException());
                }
                setupRecyclerView(items);
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "Erreur lors de la récupération des offres : ", e);
                setupRecyclerView(items);
            });
    }


    private void setupRecyclerView(List<ItemOffre> items) {
        if (items.isEmpty()) {
            items.add(new ItemOffre("0", "Aucune offre disponible", "-", "-", "Désolé", "", "", "Rééssayez plus tard"));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyAdapterOffre(getActivity(), items, typeCompte));
    }
    
}