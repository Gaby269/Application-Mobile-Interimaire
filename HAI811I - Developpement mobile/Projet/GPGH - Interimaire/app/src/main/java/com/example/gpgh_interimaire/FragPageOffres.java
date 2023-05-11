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
        items.add(new ItemOffre("Développeur Java Junior", "CDI", "30K€-35K€", "Acme Corp", R.drawable.youtube, "Nous cherchons un développeur Java Junior pour rejoindre notre équipe de développement.", "12 Rue des Lilas", "", "75010 Paris"));
        items.add(new ItemOffre("Chef de Projet IT", "CDI", "40K€-45K€", "Beta Inc.", R.drawable.youtube, "Nous recrutons un Chef de Projet IT pour notre équipe de développement.", "35 Rue de la Pompe", "", "75016 Paris"));
        items.add(new ItemOffre("Ingénieur Systèmes et Réseaux", "CDI", "45K€-50K€", "Gamma SA", R.drawable.youtube, "Nous recherchons un ingénieur systèmes et réseaux pour rejoindre notre équipe d'infrastructure.", "8 Avenue de la Grande Armée", "", "75116 Paris"));
        items.add(new ItemOffre("Développeur Full-Stack", "CDI", "35K€-40K€", "Delta Corp", R.drawable.youtube, "Nous sommes à la recherche d'un développeur Full-Stack pour rejoindre notre équipe de développement.", "4 Rue de la Paix", "", "75002 Paris"));
        items.add(new ItemOffre("Responsable Marketing Digital", "CDI", "50K€-55K€", "Epsilon SAS", R.drawable.youtube, "Nous cherchons un responsable marketing digital pour développer nos activités en ligne.", "17 Avenue des Champs-Élysées", "", "75008 Paris"));
        items.add(new ItemOffre("Administrateur Systèmes Linux", "CDI", "40K€-45K€", "Zeta SA", R.drawable.youtube, "Nous recherchons un administrateur systèmes Linux pour notre équipe d'infrastructure.", "7 Rue des Francs Bourgeois", "", "75004 Paris"));
        items.add(new ItemOffre("Développeur Mobile", "CDI", "35K€-40K€", "Iota Corp", R.drawable.youtube, "Nous cherchons un développeur mobile pour notre équipe de développement.", "42 Rue du Faubourg Saint-Antoine", "", "75012 Paris"));
        items.add(new ItemOffre("Chef de Projet Digital", "CDI", "50K€-55K€", "Kappa Inc.", R.drawable.youtube, "Nous recrutons un Chef de Projet Digital pour notre équipe de développement web.", "23 Rue Saint-Augustin", "", "75002 Paris"));
        items.add(new ItemOffre("Développeur Front-End", "CDI", "35K€-40K€", "Lambda Corp", R.drawable.youtube, "Nous sommes à la recherche d'un développeur Front-End pour rejoindre notre équipe de développement web.", "15 Rue de la Roquette", "", "75011 Paris"));
        
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
                recyclerView.setAdapter(new MyAdapterOffre(getActivity(), items, typeCompte));
            });


        return view;
    }

}