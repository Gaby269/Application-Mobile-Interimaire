package com.example.myrecycleviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycleview);

        List<Item> items = new ArrayList<>();
        items.add(new Item("Titre1", "Entreprise1", R.drawable.youtube, "Description1", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 100, true, true));
        items.add(new Item("Titre2", "Entreprise2", R.drawable.youtube, "Description2", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 30, true, true));
        items.add(new Item("Titre3", "Entreprise3", R.drawable.youtube, "Description3", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 100, false, true));
        items.add(new Item("Titre4", "Entreprise4", R.drawable.youtube, "Description4", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 100, true, true));
        items.add(new Item("Titre5", "Entreprise5", R.drawable.youtube, "Description5", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 100, true, true));
        items.add(new Item("Titre6", "Entreprise6", R.drawable.youtube, "Description6", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 100, true, true));
        items.add(new Item("Titre7", "Entreprise7", R.drawable.youtube, "Description7", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 15, true, true));
        items.add(new Item("Titre8", "Entreprise8", R.drawable.youtube, "Description8", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 100, true, true));
        items.add(new Item("Titre9", "Entreprise9", R.drawable.youtube, "Description9", "9 rue de Bon", "Résidence le Chardon", "23000 Rondin", 100, true, true));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));
    }
}