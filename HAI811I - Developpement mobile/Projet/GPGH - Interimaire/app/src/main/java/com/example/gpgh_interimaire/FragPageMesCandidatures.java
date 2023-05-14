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
            items.add(new ItemCandidature("0", "1", "John", "Doe", "Expérience en tant que magasinier", "Disponible immédiatement", "cv_john_doe.pdf"));
            items.add(new ItemCandidature("0", "2", "Jane", "Smith", "Compétences en secrétariat et gestion administrative", "Bilingue français-anglais", "cv_jane_smith.pdf"));
            items.add(new ItemCandidature("0", "3", "Michael", "Johnson", "Expérience en tant que chauffeur-livreur", "Permis de conduire valide", "cv_michael_johnson.pdf"));
            items.add(new ItemCandidature("0", "4", "Emily", "Brown", "Compétences en marketing digital", "Maîtrise des réseaux sociaux", "cv_emily_brown.pdf"));
            items.add(new ItemCandidature("0", "5", "Daniel", "Davis", "Expérience en tant que technicien informatique", "Certification Cisco CCNA", "cv_daniel_davis.pdf"));

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new MyAdapterCandidature(getActivity(), items, "Candidat"));

            return view;
        }
}