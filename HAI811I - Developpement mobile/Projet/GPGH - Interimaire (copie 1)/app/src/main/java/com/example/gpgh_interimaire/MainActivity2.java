package com.example.gpgh_interimaire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity2 extends AppCompatActivity {

    TextView logoTextView, connexionButton;

    @Override
    @SuppressLint("MissingInflatedId")
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
                Intent i = new Intent(MainActivity2.this, InscriptionActivity.class);
                startActivity(i);
            }
        });

        Button inviteButton = findViewById(R.id.boutton_invite);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity2.this, OffresActivity.class);
                startActivity(i);
            }
        });

        connexionButton = findViewById(R.id.boutton_connexion);
        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity2.this, ConnexionActivity.class);
                startActivity(i);
            }
        });
    }
}