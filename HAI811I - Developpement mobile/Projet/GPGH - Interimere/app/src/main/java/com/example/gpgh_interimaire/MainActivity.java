package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    TextView logoTextView, connexionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoTextView = findViewById(R.id.logo);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.logo);
        logoTextView.setTypeface(typeface);


        Button inscriptionButton = findViewById(R.id.boutton_inscription);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InscriptionActivity.class);
                startActivity(i);
            }
        });

        connexionButton = findViewById(R.id.boutton_connexion);
        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ConnexionActivity.class);
                startActivity(i);
            }
        });
    }
}