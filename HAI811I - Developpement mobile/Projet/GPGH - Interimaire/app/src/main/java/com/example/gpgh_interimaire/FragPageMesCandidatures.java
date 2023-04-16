package com.example.gpgh_interimaire;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FragPageMesCandidatures extends Fragment {

        // Constructeur
        public FragPageMesCandidatures() {}

        // Création de la vue pour le fragment 1
        @Override
        @SuppressLint("MissingInflatedId")
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // Associé la vue au layout du fragment 1
            View view = inflater.inflate(R.layout.frag_page_mes_candidatures, container, false);

            LinearLayout candidatureButton = view.findViewById(R.id.layout_candidature);
            candidatureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), AfficherDetailsCandidatureActivity.class);
                    startActivity(i);
                }
            });

            return view;
        }
}