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

        return view;
    }

}