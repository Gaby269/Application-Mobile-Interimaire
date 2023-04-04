package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class CompteActivity extends AppCompatActivity {

    static final String TAG = "CompteActivity";

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button retourButton = findViewById(R.id.boutton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompteActivity.this, OffresActivity.class);
                startActivity(i);
            }
        });

        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompteActivity.this, ModificationCompteActivity.class);
                startActivity(i);
            }
        });

        Button supprimerButton = findViewById(R.id.boutton_supprimer);
        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompteActivity.this,R.string.boutton_supprimer,Toast.LENGTH_SHORT).show();
                // affichage d'une boite de dialogue pour confirmer
                logoutUser();
            }
        });
    }


    private void logoutUser() {
        // Créez un AlertDialog pour demander confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(CompteActivity.this);
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setTitle("Confirmation")
                .setPositiveButton("Oui", (dialog, id) -> {
                    // Si l'user confirme, déco
                    mAuth.signOut();
                    Intent intent = new Intent(CompteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Annuler", (dialog, id) -> {
                    // Si l'user annule
                    dialog.dismiss();
                });

        // Affichez l'AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void redirect() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Utilisateur pas connecté, redirect sur la page de connexion
            Intent i = new Intent(CompteActivity.this, ConnexionActivity.class);
            startActivity(i);
                            
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        redirect();
    }
}