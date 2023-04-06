package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {

    static final String TAG = "InscriptionActivity";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText nomEditText, prenomEditText, telephoneEditText, mailEditText, mdpEditText;
    Spinner typeCompteSpinner;
    String selectedTypeCompte = "Candidat";


    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nomEditText = findViewById(R.id.nomEditText);
        prenomEditText = findViewById(R.id.prenomEditText);
        telephoneEditText = findViewById(R.id.telephoneEditText);
        mailEditText = findViewById(R.id.mailEditText);
        mdpEditText = findViewById(R.id.mdpEditText);
        typeCompteSpinner = findViewById(R.id.typeCompteSpinner);

        // On regarde si on change le spinner
        typeCompteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeCompte = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        Button creaCompteButton = findViewById(R.id.boutton_creationCompte);
        creaCompteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, "Creation du compte");
                String email = mailEditText.getText().toString();
                String mdp = mdpEditText.getText().toString();
                String tel = telephoneEditText.getText().toString();
                if (validateInput(email, mdp, tel)) {
                    Log.w(TAG, "Champs ok");
                    String nom = nomEditText.getText().toString();
                    String prenom = prenomEditText.getText().toString();
                    String type = selectedTypeCompte;
                    createUser(nom, prenom, tel, email, mdp, type);
                }
            }
        });

        TextView goToConnexionButton = findViewById(R.id.boutton_connexion);
        goToConnexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                startActivity(i);
            }
        });

        //Récupération du Spinner déclaré dans le fichier activity_inscription.xml
        Spinner typeCompteSpinner = (Spinner) findViewById(R.id.typeCompteSpinner);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        List<String> listType = new ArrayList<>();
        listType.add("Type de compte");
        listType.add("Candidat");
        listType.add("Entreprise");
        listType.add("Agence d'intérim");

        // ArrayAdapter pour le spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCompteSpinner.setAdapter(adapterSpinner); // on passe l'adapter au Spinner
    }


    private void createUser(String prenom, String nom, String tel, String email, String password, String type) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inscription réussie
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        addUserToFirestore(userId, prenom, nom, email, tel, type);

                        Intent i = new Intent(InscriptionActivity.this, ConfirmationTelephoneActivity.class);
                        i.putExtra("userId", userId);
                        startActivity(i);
                    }
                    else {
                        // Échec de l'inscription
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    }
                });
    }

    private void addUserToFirestore(String userId, String prenom, String nom, String tel, String email, String type) {
        Map<String, Object> user = new HashMap<>();
        if (prenom != null && !prenom.isEmpty()) {
            user.put("prenom", prenom);
        }
        if (nom != null && !nom.isEmpty()) {
            user.put("nom", nom);
        }
        if (tel != null && !tel.isEmpty()) {
            user.put("telephone", tel);
        }
        user.put("email", email);
        user.put("typeCompte", type);
        if (type == "Candidat") {
            user.put("signup_step", "10");
        }
        else {
            user.put("signup_step", "1");
        }

        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Utilisateur ajouté à la BDD"))
                .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout dans la BDD", e));
    }


    private boolean validateInput(String email, String password, String tel) {
        boolean check = true;
        if (email.isEmpty()) {
            mailEditText.setError(getString(R.string.email_vide));
            check = false;
        }

        if (tel.isEmpty()) {
            mailEditText.setError(getString(R.string.tel_vide));
            check = false;
        }

        if (password.isEmpty()) {
            mdpEditText.setError(getString(R.string.mdp_vide));
            check = false;
        }

        if (password.length() < 6) {
            mdpEditText.setError(getString(R.string.mdp_court));
            check = false;
        }
        if (typeCompteSpinner.getSelectedItemPosition() == 0) {
            ((TextView) typeCompteSpinner.getSelectedView()).setError(getString(R.string.choisir_compte));
            check = false;
        }

        return check;
    }


    private void redirect() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Utilisateur déjà connecté, check si connexion effectuée
            String userId = currentUser.getUid();
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int signup_step = Integer.parseInt(documentSnapshot.getString("signup_step"));
                            if (signup_step == 1) {
                                Intent i = new Intent(InscriptionActivity.this, ConfirmationTelephoneActivity.class);
                                startActivity(i);
                            }
                            else if (signup_step == 2) {
                                Intent i = new Intent(InscriptionActivity.this, EntrepriseActivity.class);
                                startActivity(i);
                            }
                            //suite des else
                            else {
                                Intent i = new Intent(InscriptionActivity.this, OffresActivity.class);
                                startActivity(i);
                            }
                        }
                        else {
                            Log.w(TAG, "DB Non trouvée");
                        }
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        redirect();
    }
}