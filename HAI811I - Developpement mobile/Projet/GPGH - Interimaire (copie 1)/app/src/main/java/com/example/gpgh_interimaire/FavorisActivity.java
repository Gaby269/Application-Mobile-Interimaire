package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FavorisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        Button retourButton = findViewById(R.id.boutton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FavorisActivity.this, OffresActivity.class);
                startActivity(i);
            }
        });

        LinearLayout offreButton = findViewById(R.id.layout_favorie);
        offreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FavorisActivity.this, AfficherDetailsOffreActivity.class);
                startActivity(i);
            }
        });
    }
}