package com.example.gpgh_interimaire;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragPageOffres extends Fragment {

    // Constructeur
    public FragPageOffres() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_offres, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycleview);

        List<ItemOffre> items = new ArrayList<ItemOffre>();
        items.add(new ItemOffre("Titre1", "CDD", "30", "Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre2", "Stage","200","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre3", "CDD", "30","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre4", "CDD", "30","Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre5", "Stage","1000","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre1", "Stage","30","Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre1", "CDD", "30","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre1", "Stage","50","Youtube", R.drawable.facebook, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));
        items.add(new ItemOffre("Titre1", "CDD", "30","Youtube", R.drawable.youtube, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyAdapterOffre(getActivity(), items, null));



        // Récuperer les arguments éventuels
        Bundle args = getArguments();
        String typeCompte = "";
        if (args != null) {
            typeCompte = args.getString("typeCompte");
        }

        /*Button postulerButton = view.findViewById(R.id.boutton_candidature);
        postulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PostulerActivity.class);
                startActivity(i);
            }
        });*/



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
                Intent i = new Intent(getActivity(), CreationOffre1Activity.class);
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