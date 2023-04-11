package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EntrepriseActivity extends AppCompatActivity {

    static final String TAG = "InscriptionEntrepriseActivity";
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    CheckBox secondContactCeckbox;
    EditText nomEntrepriseEditText, numSiretEditText, adresseEditText;
    EditText nomCompletEditText, prenomCompletEditText, mailEditText, telephoneEditText;
    EditText nomComplet2EditText, prenomComplet2EditText, mail2EditText, telephone2EditText;
    String nomEntreprise, numSiret, adresse;
    String nomComplet, prenomComplet, mail, telephone;
    String nomComplet2, prenomComplet2, mail2, telephone2;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entreprise);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // on redirige si non connecté
        }
        userId = currentUser.getUid();

        //champs entreprise
        nomEntrepriseEditText = findViewById(R.id.nomEntrepriseEditText);
        numSiretEditText = findViewById(R.id.numSiretEditText);
        adresseEditText = findViewById(R.id.adresseEditText);

        //champs contact1
        nomCompletEditText = findViewById(R.id.nomCompletEditText);
        prenomCompletEditText = findViewById(R.id.prenomCompletEditText);
        mailEditText = findViewById(R.id.mailEditText);
        telephoneEditText = findViewById(R.id.telephoneEditText);

        //champs contact2
        nomComplet2EditText = findViewById(R.id.nomComplet2EditText);
        prenomComplet2EditText = findViewById(R.id.prenomComplet2EditText);
        mail2EditText = findViewById(R.id.mail2EditText);
        telephone2EditText = findViewById(R.id.telephone2EditText);

        secondContactCeckbox = findViewById(R.id.sliderButton);
        LinearLayout secondContactFields = findViewById(R.id.contactSecLayout);

        secondContactCeckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                secondContactFields.setVisibility(View.VISIBLE);
            } else {
                secondContactFields.setVisibility(View.GONE);
            }
        });

        Button inscriptionButton = findViewById(R.id.boutton_creationCompte);
        inscriptionButton.setOnClickListener(view -> {
            addUserToFirestore(userId);
        });

    }

    private void addUserToFirestore(String userId) {
        nomEntreprise = nomEntrepriseEditText.getText().toString();
        numSiret = numSiretEditText.getText().toString();
        adresse = adresseEditText.getText().toString();

        nomComplet = nomCompletEditText.getText().toString();
        prenomComplet = prenomCompletEditText.getText().toString();
        mail = mailEditText.getText().toString();
        telephone = telephoneEditText.getText().toString();

        nomComplet2 = nomComplet2EditText.getText().toString();
        prenomComplet2 = prenomComplet2EditText.getText().toString();
        mail2 = mail2EditText.getText().toString();
        telephone2 = telephone2EditText.getText().toString();

        if (validateInput()) {
            Map<String, Object> entreprise = new HashMap<>();
            entreprise.put("nom", nomEntreprise);
            entreprise.put("siret", numSiret);
            entreprise.put("adresse", adresse);

            db.collection("entreprises")
                    .document(userId)
                    .set(entreprise)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Entreprise ajouté à la BDD"))
                    .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout dans la BDD", e));


            Map<String, Object> contact1 = new HashMap<>();
            contact1.put("nom", nomComplet);
            contact1.put("prenom", prenomComplet);
            contact1.put("mail", mail);
            contact1.put("telephone", telephone);
            contact1.put("num_contact", "1");

            db.collection("contact")
                    .document(userId)
                    .set(contact1)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Contact1 ajouté à la BDD"))
                    .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout dans la BDD", e));

            if (secondContactCeckbox.isChecked()) {
                Map<String, Object> contact2 = new HashMap<>();
                contact2.put("nom", nomComplet2);
                contact2.put("prenom", prenomComplet2);
                contact2.put("mail", mail2);
                contact2.put("telephone", telephone2);
                contact2.put("num_contact", "2");

                db.collection("contact")
                        .document(userId)
                        .set(contact2)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Contact2 ajouté à la BDD"))
                        .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout dans la BDD", e));
            }

            db.collection("users")
                    .document(userId)
                    .update("signup_step", "3")
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "update de signup_step réussie"))
                    .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'update de signup_step", e));

            Intent i = new Intent(EntrepriseActivity.this, AbonnementActivity.class);
            startActivity(i);
        }
    }


    private boolean validateInput() {
        boolean check = true;

        if (nomEntreprise.isEmpty()) {
            nomEntrepriseEditText.setError(getString(R.string.erreurChamp));
            check = false;
        }

        if (numSiret.isEmpty()) {
            numSiretEditText.setError(getString(R.string.erreurChamp));
            check = false;
        }

        if (adresse.isEmpty()) {
            adresseEditText.setError(getString(R.string.erreurChamp));
            check = false;
        }

        if (nomComplet.isEmpty()) {
            nomCompletEditText.setError(getString(R.string.erreurChamp));
            check = false;
        }

        if (prenomComplet.isEmpty()) {
            prenomCompletEditText.setError(getString(R.string.erreurChamp));
            check = false;
        }

        if (mail.isEmpty()) {
            mailEditText.setError(getString(R.string.erreurChamp));
            check = false;
        }

        if (telephone.isEmpty()) {
            telephoneEditText.setError(getString(R.string.erreurChamp));
            check = false;
        }

        if (secondContactCeckbox.isChecked()) {
            if (nomComplet2.isEmpty()) {
                nomComplet2EditText.setError(getString(R.string.erreurChamp));
                check = false;
            }

            if (prenomComplet2.isEmpty()) {
                prenomComplet2EditText.setError(getString(R.string.erreurChamp));
                check = false;
            }

            if (mail2.isEmpty()) {
                mail2EditText.setError(getString(R.string.erreurChamp));
                check = false;
            }

            if (telephone2.isEmpty()) {
                telephone2EditText.setError(getString(R.string.erreurChamp));
                check = false;
            }
        }

        return check;
    }
}