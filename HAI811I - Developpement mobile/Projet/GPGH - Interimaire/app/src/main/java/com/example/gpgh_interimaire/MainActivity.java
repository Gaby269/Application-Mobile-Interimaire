package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    TextView logoTextView, connexionButton;
    FirebaseAuth mAuth;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logoTextView = findViewById(R.id.logo);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.logo);
        logoTextView.setTypeface(typeface);

        Button inscriptionButton = findViewById(R.id.boutton_inscription);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InscriptionActivity.class);
                i.putExtra("showLinearLayout", true);
                i.putExtra("invite", false);
                startActivity(i);
            }
        });

        Button inviteButton = findViewById(R.id.boutton_invite);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OffresActivity.class);
                i.putExtra("showLinearLayout", false);
                i.putExtra("invite", true);
                startActivity(i);
            }
        });

        connexionButton = findViewById(R.id.boutton_connexion);
        connexionButton.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, ConnexionActivity.class);
            i.putExtra("showLinearLayout", true);
            i.putExtra("invite", false);
            startActivity(i);
        });


        Button decoButton = findViewById(R.id.buttonDeco);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            if (userId != null) {
                decoButton.setText("Connecté");
            }
        }



        decoButton.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Déconnecté",Toast.LENGTH_SHORT).show();
            logoutUser();
        });


    }

    private void logoutUser() {
        // Créez un AlertDialog pour demander confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setTitle("Confirmation")
                .setPositiveButton("Oui", (dialog, id) -> {
                    // Si l'user confirme, déco
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
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
}