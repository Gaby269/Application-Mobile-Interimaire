package com.example.gpgh_interimaire;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class FragPageMesCandidatures extends Fragment {

        // Constructeur
        public FragPageMesCandidatures() {}

        // Création de la vue pour le fragment 1
        @Override
        @SuppressLint("MissingInflatedId")
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // Associé la vue au layout du fragment 1
            View view = inflater.inflate(R.layout.frag_page_mes_candidatures, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.recycleview);

            List<ItemCandidature> items = new ArrayList<ItemCandidature>();
            items.add(new ItemCandidature("POINTEAU", "Gabrielle", "Travailleur ou pas", "Chouette", "CV1"));
            items.add(new ItemCandidature("Gatien", "HADDAD","Travailleur ou pas","Youtube", "CV1"));
            items.add(new ItemCandidature("Rototo", "CHOCOLAT", "Travailleur ou pas","Youtube", "CV1"));
            items.add(new ItemCandidature("Lily", "GIRON", "Travailleur ou pas","Youtube", "CV1"));
            items.add(new ItemCandidature("Martin", "ROLOTIN","Travailleur ou pas","Youtube", "CV1"));
            items.add(new ItemCandidature("Rolin", "ZOLU","Travailleur ou pas","Youtube", "CV1"));
            items.add(new ItemCandidature("Rudy", "ABON", "Travailleur ou pas","Youtube", "CV1"));
            items.add(new ItemCandidature("Marguerite", "POLIN","Travailleur ou pas","Youtube", "CV1"));
            items.add(new ItemCandidature("Isabelle", "SURT", "Travailleur ou pas","Youtube", "CV1"));

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new MyAdapterCandidature(getActivity(), items));

            return view;
        }
}