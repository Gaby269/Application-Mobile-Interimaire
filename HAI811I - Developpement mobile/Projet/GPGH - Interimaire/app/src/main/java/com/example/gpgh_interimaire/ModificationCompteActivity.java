package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ModificationCompteActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_compte);

        Button retourButton = findViewById(R.id.boutton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificationCompteActivity.this, FragPageCompte.class);
                startActivity(i);
            }
        });
        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificationCompteActivity.this, FragPageCompte.class);
                startActivity(i);
                Toast.makeText(ModificationCompteActivity.this,R.string.compteModif,Toast.LENGTH_SHORT).show();
            }
        });
    }
}