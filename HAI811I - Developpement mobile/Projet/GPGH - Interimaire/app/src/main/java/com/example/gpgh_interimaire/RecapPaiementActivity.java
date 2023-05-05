package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RecapPaiementActivity extends AppCompatActivity {

    TextView typeAboTextView, prixTextView, totalTextView;
    String typeAbo;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress", "SetTextI18n", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_paiement);

        Intent intent = getIntent();
        String typeAbo = intent.getStringExtra("typeAbo");
        int prix = intent.getIntExtra("prix", 0);


        //modif du texte du paiment de la typeAboTextView et prixTextView
        typeAboTextView = findViewById(R.id.typeAboTextView);
        if (typeAbo.contains("Ponctuel")){
            typeAboTextView.setText("Abonnement Ponctuel : ");
        }
        else{
            typeAboTextView.setText("Abonnement " + typeAbo + " : ");
        }
        prixTextView = findViewById(R.id.prixTextView);
        prixTextView.setText(prix+" €");
        totalTextView = findViewById(R.id.totalTextView);
        totalTextView.setText("Total : "+prix+" €");


        ImageView boutonCodePromo = findViewById(R.id.bouton_codePromo);
        EditText codePromo = findViewById(R.id.codePromo);
        boutonCodePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codePromo.length() == 0){
                    codePromo.setError("Veuillez remplir le champ");
                }
                // Ajouter au prix le code promo
                // le fait que ya - quelque chose
                // reclacul du total
            }
        });


        Button inscriptionButton = findViewById(R.id.boutton_confirmer);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecapPaiementActivity.this, MoyenPaiementActivity.class);
                startActivity(i);
            }
        });
    }
}