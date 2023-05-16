package com.example.gpgh_interimaire;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public FragPageOffres() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        db = FirebaseFirestore.getInstance();

        // Récupérer les arguments du bundle
        assert getArguments() != null;
        String typeCompte = getArguments().getString("typeCompte");

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_offres, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycleview);

        List<ItemOffre> items = new ArrayList<ItemOffre>();

        items.add(new ItemOffre("0", "[TEST] Développeur Java", "Temps plein", "25", "ABC Entreprise", "01/06/2023", "31/08/2023", "75000 Paris"));
        items.add(new ItemOffre("1", "[TEST] Assistant administratif", "Temps partiel", "15", "XYZ Entreprise", "15/07/2023", "30/09/2023", "69000 Lyon"));
        items.add(new ItemOffre("2", "[TEST] Manutentionnaire", "CDD", "10", "123 Entreprise", "01/06/2023", "30/06/2023", "33000 Bordeaux"));
        items.add(new ItemOffre("3", "[TEST] Infirmier(e)", "CDI", "30", "456 Entreprise", "01/07/2023", "31/12/2023", "13000 Marseille"));
        items.add(new ItemOffre("4", "[TEST] Commercial", "Freelance", "20", "789 Entreprise", "01/06/2023", "31/12/2023", "59000 Lille"));


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
                            //Ajouter l'offre à la liste des offres
                            items.add(new ItemOffre(documentId, titre, type, remuneration, nameEntreprise, dateDeb, dateFin, codePostal));
                            Log.d(TAG, "offre récupérée");
                        }
                    }
                    else { // Le QuerySnapshot est nul
                        Log.d(TAG, "La collection est vide");
                        items.add(new ItemOffre("0", "Pas d'offres disponibles", "", "", "Désolé", "", "", "Rééssayez plus tard"));
                    }
                }
                else {
                    Log.d(TAG, "Erreur lors de la récupération des offres : ", task.getException());
                    items.add(new ItemOffre("0", "Pas d'offres disponibles", "", "", "Désolé", "", "", "Rééssayez plus tard"));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new MyAdapterOffre(getActivity(), items, typeCompte));
            });


        return view;
    }

}