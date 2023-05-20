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

    CheckBox secondContactCheckbox;
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

        secondContactCheckbox = findViewById(R.id.sliderButton);
        LinearLayout secondContactFields = findViewById(R.id.contactSecLayout);

        secondContactCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

        Button TMPButton = findViewById(R.id.boutton_tmp);
        TMPButton.setOnClickListener(view -> {
            //modification des champs avec des valeurs par défaut
            nomEntrepriseEditText.setText("nomEntreprise");
            numSiretEditText.setText("numSiret");
            adresseEditText.setText("adresse");
            
            nomCompletEditText.setText("nomComplet");
            prenomCompletEditText.setText("prenomComplet");
            mailEditText.setText("mail");
            telephoneEditText.setText("telephone");

            nomComplet2EditText.setText("nomComplet2");
            prenomComplet2EditText.setText("prenomComplet2");
            mail2EditText.setText("mail2");
            telephone2EditText.setText("telephone2");
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

            entreprise.put("nom1", nomComplet);
            entreprise.put("prenom1", prenomComplet);
            entreprise.put("mail1", mail);
            entreprise.put("telephone1", telephone);

            if (secondContactCheckbox.isChecked()) {
                entreprise.put("nom2", nomComplet2);
                entreprise.put("prenom2", prenomComplet2);
                entreprise.put("mail2", mail2);
                entreprise.put("telephone2", telephone2);
            }

            db.collection("entreprises")
                    .document(userId)
                    .set(entreprise)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Entreprise ajouté à la BDD"))
                    .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout dans la BDD", e));

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

        if (secondContactCheckbox.isChecked()) {
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