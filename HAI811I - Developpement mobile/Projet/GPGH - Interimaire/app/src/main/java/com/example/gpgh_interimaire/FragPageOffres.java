package com.example.gpgh_interimaire;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FragPageOffres extends Fragment {

    // Constructeur
    public FragPageOffres() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_offres, container, false);

        ArrayList<class_offre> listeOffres = new ArrayList<>();
        listeOffres.add(new class_offre("Titre offre 1", "Petite description offre 1", "Grande description offre 1", R.drawable.youtube, "Nom entreprise 1", "Adresse entreprise 1", "Complément adresse entreprise 1", "Code postal entreprise 1", "Ville entreprise 1"));
        listeOffres.add(new class_offre("Titre offre 2", "Petite description offre 2", "Grande description offre 2", R.drawable.facebook, "Nom entreprise 2", "Adresse entreprise 2", "Complément adresse entreprise 2", "Code postal entreprise 2", "Ville entreprise 2"));
        // Ajouter d'autres offres si nécessaire
        /*MyAdapterListView adapter = new MyAdapterListView(this, listeOffres);
        ListView listView = view.findViewById(R.id.list_view_offres);
        listView.setAdapter(adapter);*/






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