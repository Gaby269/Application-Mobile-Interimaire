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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConnexionActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";
    FirebaseAuth mAuth;

    EditText mailEditText, mdpEditText;

    String typeCompte;

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

        Button loginCandidat = findViewById(R.id.buttonTmp1);
        loginCandidat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailEditText.setText("gaby.toto@gmail.com");
                mdpEditText.setText("123456");
                String email = mailEditText.getText().toString();
                String mdp = mdpEditText.getText().toString();
                if (validateInput(email, mdp)) {
                    signInUser(email, mdp);
                }
            }
        });

        

        Button loginEntreprise = findViewById(R.id.buttonTmp2);
        loginEntreprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailEditText.setText("gatienhaddad@hotmail.fr");
                mdpEditText.setText("123456");
                String email = mailEditText.getText().toString();
                String mdp = mdpEditText.getText().toString();
                if (validateInput(email, mdp)) {
                    signInUser(email, mdp);
                }
            }
        });

        

        Button loginAdmin = findViewById(R.id.buttonTmp3);
        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailEditText.setText("admin@gmail.com");
                mdpEditText.setText("123456");
                String email = mailEditText.getText().toString();
                String mdp = mdpEditText.getText().toString();
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
                        
                        Intent i = new Intent(ConnexionActivity.this, LoadingNavbarActivity.class);
                        startActivity(i);
                    }
                    else {
                        Exception e = task.getException();
                        // Log.w(TAG, "createUserWithEmail:failure", e);
                        if (e instanceof FirebaseAuthException) {
                            gererExceptionsFirebase((FirebaseAuthException) e);
                        } else { // Erreur inconnue
                            Toast.makeText(ConnexionActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void gererExceptionsFirebase(FirebaseAuthException e) {
        String errorCode = e.getErrorCode();
        String errorMessage = e.getMessage();
    
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                // l'email est mal formé
                mailEditText.setError(getString(R.string.email_invalide));
                break;
            case "ERROR_WRONG_PASSWORD":
                // le mot de passe n'est pas bon
                mdpEditText.setError(getString(R.string.mdp_incorrect));
                break;
            case "ERROR_USER_NOT_FOUND":
                // l'utilisateur n'existe pas
                mailEditText.setError(getString(R.string.compte_inexistant));
                break;
            default:
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}