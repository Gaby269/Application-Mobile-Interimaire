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
import android.widget.LinearLayout;

public class FragPageOffres extends Fragment {

    // Constructeur
    public FragPageOffres() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_offres, container, false);

        // Récuperer les arguments éventuels
        Bundle args = getArguments();
        String typeCompte = "";
        if (args != null) {
            typeCompte = args.getString("typeCompte");
        }

        //aparait que si c'ets une entreprise
        Button candidatureButton = view.findViewById(R.id.boutton_candidature);
        candidatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CandidaturesOffreActivity.class);
                startActivity(i);
            }
        });

        //aparait que si c'ets une entreprise
        LinearLayout offreLayout = view.findViewById(R.id.layout_abo);
        offreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AfficherDetailsOffreActivity.class);
                startActivity(i);
            }
        });

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
}