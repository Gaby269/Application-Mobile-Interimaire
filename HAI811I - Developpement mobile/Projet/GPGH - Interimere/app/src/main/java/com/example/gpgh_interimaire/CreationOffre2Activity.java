package com.example.gpgh_interimaire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreationOffre2Activity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_offre1);


        Button creaOffreButton = findViewById(R.id.boutton_creationOffre);
        creaOffreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreationOffre2Activity.this, OffresActivity.class);
                startActivity(i);
            }
        });
    }
}