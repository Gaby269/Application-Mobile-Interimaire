package com.example.myrecycleviewinterimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


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

        List<Item> items = new ArrayList<Item>();
        items.add(new Item("Titre1", "Youtube", R.drawable.a, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 100, true, true));
        items.add(new Item("Titre2", "Youtube", R.drawable.c, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 100, true, true));
        items.add(new Item("Titre3", "Youtube", R.drawable.a, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 49, false, true));
        items.add(new Item("Titre4", "Youtube", R.drawable.c, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 100, true, true));
        items.add(new Item("Titre5", "Youtube", R.drawable.a, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 30, true, false));
        items.add(new Item("Titre1", "Youtube", R.drawable.d, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 100, true, true));
        items.add(new Item("Titre1", "Youtube", R.drawable.a, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 100, true, true));
        items.add(new Item("Titre1", "Youtube", R.drawable.a, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 100, false, true));
        items.add(new Item("Titre1", "Youtube", R.drawable.a, "petite description", "1 rue du Bidon", "Résidence chaud", "24000  Rouge", 100, true, true));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));
    }
}