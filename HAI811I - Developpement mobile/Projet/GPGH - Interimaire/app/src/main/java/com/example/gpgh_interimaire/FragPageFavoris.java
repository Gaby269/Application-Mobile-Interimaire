package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FragPageFavoris extends Fragment {

    // Constructeur
    public FragPageFavoris() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_offres, container, false);

        LinearLayout offreButton = view.findViewById(R.id.layout_favorie);
        offreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AfficherDetailsOffreActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

}