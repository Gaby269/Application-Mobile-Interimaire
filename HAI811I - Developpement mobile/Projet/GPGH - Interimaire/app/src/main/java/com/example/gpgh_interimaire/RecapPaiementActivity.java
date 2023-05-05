package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RecapPaiementActivity extends AppCompatActivity {

    TextView typeAboTextView, prixTextView, totalTextView;
    boolean is_use = false;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress", "SetTextI18n", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_paiement);

        Intent intent = getIntent();
        String typeAbo = intent.getStringExtra("typeAbo");
        int prix = intent.getIntExtra("prix", 0);

        // Rendre invisible les code promo
        LinearLayout code10 = findViewById(R.id.layout_text_reduction10);
        code10.setVisibility(View.GONE);
        LinearLayout code30 = findViewById(R.id.layout_text_reduction30);
        code30.setVisibility(View.GONE);


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


        EditText codePromo = findViewById(R.id.codePromo);
        ImageView boutonCodePromo = findViewById(R.id.bouton_codePromo);
        boutonCodePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codePromo.length() == 0){
                    codePromo.setError("Veuillez remplir le champ");
                }
                else if (codePromo.getText().toString().equals("CODE202310")){
                    // Changement du texte du prix total
                    double prix10 = prix-prix*0.1;
                    totalTextView.setText("Total : "+prix10+" €");
                    // Changement de visibilité du layout
                    LinearLayout code10 = findViewById(R.id.layout_text_reduction10);
                    code10.setVisibility(View.VISIBLE);
                    // Changement de prix
                    TextView prixReduit10 = findViewById(R.id.reductionTextView10);
                    prixReduit10.setText("- "+prix*0.1+" €");
                    // Remise à 0 le code promo
                    codePromo.setText("");
                    is_use = true;

                }
                else if (codePromo.getText().toString().equals("CODE202330")){
                    // Changement du texte du prix total
                    double prix30 = prix-prix*0.3;
                    totalTextView.setText("Total : "+prix30+" €");
                    // Changement de visibilité du layout
                    LinearLayout code30 = findViewById(R.id.layout_text_reduction30);
                    code30.setVisibility(View.VISIBLE);
                    // Changement de prix
                    TextView prixReduit30 = findViewById(R.id.reductionTextView30);
                    prixReduit30.setText("- "+prix*0.3+" €");
                    // Remise à 0 le code promo
                    codePromo.setText("");
                    // Dit qu'on est passé par la
                    is_use = true;
                }
                // Verfication pas de cumulable
                else if (is_use && (codePromo.getText().toString().equals("CODE202330") || codePromo.getText().toString().equals("CODE202310") || codePromo.length() != 0)){
                    codePromo.setError("Les code promo ne sont pas cumulable");
                }
                else {
                    codePromo.setError("Ce code n'est pas reconnu !");
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