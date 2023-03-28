package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MoyenPaiementActivity extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moyen_paiement);


        Button inscriptionButton = findViewById(R.id.boutton_payer);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MoyenPaiementActivity.this, TutoActivity.class);
                startActivity(i);
            }
        });
    }
}