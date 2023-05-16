package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
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

public class FragPageFavoris extends Fragment {

    // Constructeur
    public FragPageFavoris() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_favoris, container, false);

        // Récupérer les arguments du bundle
        assert getArguments() != null;
        String typeCompte = getArguments().getString("typeCompte");

        RecyclerView recyclerView = view.findViewById(R.id.recycleview);

        List<ItemOffre> items = new ArrayList<ItemOffre>();
        items.add(new ItemOffre("0", "[TEST] Développeur Java", "Temps plein", "25", "ABC Entreprise", "01/06/2023", "31/08/2023", "75000 Paris"));
        items.add(new ItemOffre("0", "[TEST] Assistant administratif", "Temps partiel", "15", "XYZ Entreprise", "15/07/2023", "30/09/2023", "69000 Lyon"));
        items.add(new ItemOffre("0", "[TEST] Manutentionnaire", "CDD", "10", "123 Entreprise", "01/06/2023", "30/06/2023", "33000 Bordeaux"));
        items.add(new ItemOffre("0", "[TEST] Infirmier(e)", "CDI", "30", "456 Entreprise", "01/07/2023", "31/12/2023", "13000 Marseille"));
        items.add(new ItemOffre("0", "[TEST] Commercial", "Freelance", "20", "789 Entreprise", "01/06/2023", "31/12/2023", "59000 Lille"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyAdapterOffre(getActivity(), items, typeCompte));

        return view;
    }

}