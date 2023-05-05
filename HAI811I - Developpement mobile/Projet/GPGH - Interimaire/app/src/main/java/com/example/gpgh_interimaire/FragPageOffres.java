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

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_offres, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycleview);

        List<ItemOffre> items = new ArrayList<ItemOffre>();
        // items.add(new ItemOffre("Titre1", "CDD", "30", "Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre2", "Stage","200","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre3", "CDD", "30","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre4", "CDD", "30","Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre5", "Stage","1000","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre1", "Stage","30","Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre1", "CDD", "30","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre1", "Stage","50","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        // items.add(new ItemOffre("Titre1", "CDD", "30","Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));

        //Récupérer les offres de la base de données
        db.collection("offres")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) { // Vérifier si le QuerySnapshot n'est pas nul
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            //Récupérer les données de l'offre
                            String dateDeb = document.getString("dateDeb");
                            String dateFin = document.getString("dateFin");
                            String description = document.getString("description");
                            String lieu = document.getString("lieu");
                            String remuneration = document.getString("remuneration");
                            String titre = document.getString("titre");
                            String type = document.getString("type");
                            //Ajouter l'offre à la liste des offres
                            items.add(new ItemOffre(titre, type, remuneration, "Entreprise", R.drawable.youtube, description, lieu, "ville", "codePostal"));
                            Log.d(TAG, "offre récupérée");
                        }
                    }
                    else { // Le QuerySnapshot est nul
                        Log.d(TAG, "La collection est vide");
                        items.add(new ItemOffre("Offre cool", "CDD", "30", "Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
                    }
                }
                else {
                    Log.d(TAG, "Erreur lors de la récupération des offres : ", task.getException());
                    items.add(new ItemOffre("Super offre", "CDD", "30", "Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new MyAdapterOffre(getActivity(), items, null));
            });







        // Récuperer les arguments éventuels
        Bundle args = getArguments();
        String typeCompte = "";
        if (args != null) {
            typeCompte = args.getString("typeCompte");
        }



        //aparait que si c'ets une entreprise
        ImageView ajoutButton = view.findViewById(R.id.boutton_ajout);
        if (typeCompte.contains("Candidat")){
            ajoutButton.setVisibility(View.GONE);
        }
        else{
            ajoutButton.setVisibility(View.VISIBLE);
        }
        ajoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CreationOffreActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
/*
    public Object getSystemService(String service) {
        if (service.equals(Context.LAYOUT_INFLATER_SERVICE)) {
            return LayoutInflater.from(mContext);
        } else if (service.equals(Context.CONNECTIVITY_SERVICE)) {
            return mConnectivityManager;
        } else if (service.equals(Context.LOCATION_SERVICE)) {
            return mLocationManager;
        } else {
            return null;
        }
    }*/

}