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

        List<Item> items = new ArrayList<Item>();
        items.add(new Item("John wick", "john@gmail.com",R.drawable.a));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.c));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.d));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.a));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.c));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.d));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.a));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.c));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.d));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.a));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.c));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.d));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.a));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.c));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.d));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.a));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.c));
        items.add(new Item("John wick", "john@gmail.com",R.drawable.d));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));
    }
}