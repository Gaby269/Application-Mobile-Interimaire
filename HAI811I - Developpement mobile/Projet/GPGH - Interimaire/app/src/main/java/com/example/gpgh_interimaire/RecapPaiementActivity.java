package com.example.gpgh_interimaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class RecapPaiementActivity extends AppCompatActivity {

    String TAG = "RecapPaiementActivity";

    FirebaseUser user;
    FirebaseFirestore db;

    TextView typeAboTextView, multiplicateurTextViewTextView, totalTextView;
    boolean is_use = false;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress", "SetTextI18n", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_paiement);

        Intent intent = getIntent();
        String typeAbo = intent.getStringExtra("typeAbo");
        int prix = intent.getIntExtra("prix", 0);
        int multiplicateur = intent.getIntExtra("multiplicateur", 1);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Rendre invisible les code promo
        LinearLayout code10 = findViewById(R.id.layout_text_reduction10);
        code10.setVisibility(View.GONE);
        LinearLayout code30 = findViewById(R.id.layout_text_reduction30);
        code30.setVisibility(View.GONE);


        //modif du texte du paiment de la typeAboTextView et prixTextView
        typeAboTextView = findViewById(R.id.typeAboTextView);
        if (typeAbo.contains("Ponctuel")) { typeAbo = "Ponctuel"; }

        typeAboTextView.setText("Abonnement " + typeAbo + " : ");

        multiplicateurTextViewTextView = findViewById(R.id.multiplicateurTextView);
        multiplicateurTextViewTextView.setText(decimalFormat.format(prix) + "€ x" + multiplicateur);
        totalTextView = findViewById(R.id.totalTextView);
        prix *= multiplicateur;
        totalTextView.setText("Total : " + decimalFormat.format(prix) + " €");


        EditText codePromo = findViewById(R.id.codePromo);
        ImageView boutonCodePromo = findViewById(R.id.bouton_codePromo);
        int finalPrix = prix;
        boutonCodePromo.setOnClickListener(view -> {
            if (codePromo.length() == 0) {
                codePromo.setError(getString(R.string.erreur_vide));
            }
            // Verfication code pas cumulable
            else if (is_use && codePromo.length() != 0) {
                codePromo.setError(getString(R.string.code_promo_non_cumulabe));
            }
            else if (codePromo.getText().toString().equals("GPGH10")) {
                double nouveau_prix = finalPrix *0.9;
                totalTextView.setText("Total : " + decimalFormat.format(nouveau_prix) + " €");

                // Changement de visibilité du layout
                LinearLayout code101 = findViewById(R.id.layout_text_reduction10);
                code101.setVisibility(View.VISIBLE);

                TextView prixReduit10 = findViewById(R.id.reductionTextView10);
                prixReduit10.setText("- "+ decimalFormat.format(finalPrix *0.1) +" €");

                codePromo.setText("");
                is_use = true;

            }
            else if (codePromo.getText().toString().equals("GPGH30")) {
                double nouveau_prix = finalPrix *0.7;
                totalTextView.setText("Total : " + decimalFormat.format(nouveau_prix) + " €");

                // Changement de visibilité du layout
                LinearLayout code301 = findViewById(R.id.layout_text_reduction30);
                code301.setVisibility(View.VISIBLE);

                TextView prixReduit30 = findViewById(R.id.reductionTextView30);
                prixReduit30.setText("- "+ decimalFormat.format(finalPrix *0.3) +" €");

                codePromo.setText("");
                is_use = true;
            }
            else {
                codePromo.setError(getString(R.string.code_promo_incorrect));
            }
        });


        Button inscriptionButton = findViewById(R.id.boutton_confirmer);
        String finalTypeAbo = typeAbo;
        inscriptionButton.setOnClickListener(view -> addPaymentToFirestore(finalTypeAbo));
    }


    private void addPaymentToFirestore(String type_abonnement) {
        String userId = user.getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.update("abonnement", type_abonnement)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Champ de paiement mis à jour");
                Intent i = new Intent(RecapPaiementActivity.this, MoyenPaiementActivity.class);
                startActivity(i);
            })
            .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la mise à jour du paiement", e));

    }
}