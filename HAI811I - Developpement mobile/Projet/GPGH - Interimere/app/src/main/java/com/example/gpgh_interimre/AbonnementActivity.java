package com.example.gpgh_interimre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AbonnementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);


        Button inscriptionButton = findViewById(R.id.boutton_choixAbonnement);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AbonnementActivity.this, RecapPaiementActivity.class);
                startActivity(i);
            }
        });
    }
}