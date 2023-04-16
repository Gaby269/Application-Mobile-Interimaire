package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConnexionActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";
    FirebaseAuth mAuth;

    EditText mailEditText, mdpEditText;

    String typeCompte;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        mAuth = FirebaseAuth.getInstance();

        mailEditText = findViewById(R.id.mailEditText);
        mdpEditText = findViewById(R.id.mdpEditText);


        Button connexion = findViewById(R.id.boutton_connexion);
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mailEditText.getText().toString();
                String mdp = mdpEditText.getText().toString();
                if (validateInput(email, mdp)) {
                    signInUser(email, mdp);
                }
            }
        });

        Button fastLogin = findViewById(R.id.buttonTmp);
        fastLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String email = "gatienhaddad@hotmail.fr";
                //String mdp = "123456";

                String email = "gaby.toto@gmail.com";
                String mdp = "gabyToto";
                if (validateInput(email, mdp)) {
                    signInUser(email, mdp);
                }
            }
        });
    }


    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            mailEditText.setError("L'email ne peut pas être vide");
            return false;
        }

        if (password.isEmpty()) {
            mdpEditText.setError("Le mot de passe ne peut pas être vide");
            return false;
        }

        if (password.length() < 6) {
            mdpEditText.setError("Le mot de passe doit contenir au moins 6 caractères");
            return false;
        }

        return true;
    }


    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) { // Connexion réussie
                        FirebaseUser user = mAuth.getCurrentUser();
                        displayloadingScreen();
                    }
                    else { // Échec de la connexion
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                    }
                });
    }


    public void displayloadingScreen() {

        FragmentLoading loadingFragment = FragmentLoading.newInstance("Connexion...");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loadingFragment);
        //pour pas le fragment soit restauré lorsque l'utilisateur appuie sur le bouton retour
        transaction.addToBackStack(null);
        transaction.commit();
        // loadingFragment.setTextLoading("Création du compte...");
    }
}