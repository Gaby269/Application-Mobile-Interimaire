package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreationOffreActivity extends AppCompatActivity {

    static final String TAG = "CreationOffre1Activity";
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    
    EditText titreOffreEditText, typePosteEditText, descriptionOffreEditText, dateDebEditText, dateFinEditText, remunerationEditText;
    EditText rueEditText, complementEditText, codePostalEditText, villeEditText;
    CheckBox checkBoxParking, checkBoxTicketResto, checkBoxTeletravail;

    String titre, type, description, dateDeb, dateFin, remuneration, rue, complement, codePostal, ville;
    boolean parking, ticketResto, teletravail;
    String typeCompte;


    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_offre);

        Intent i = getIntent();
        typeCompte = i.getStringExtra("typeCompte");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        titreOffreEditText = findViewById(R.id.titreOffreEditText);
        typePosteEditText = findViewById(R.id.typePosteEditText);
        descriptionOffreEditText = findViewById(R.id.descriptionOffreEditText);
        dateDebEditText = findViewById(R.id.dateDebEditText);
        dateFinEditText = findViewById(R.id.dateFinEditText);
        remunerationEditText = findViewById(R.id.remunerationEditText);
        rueEditText = findViewById(R.id.rueEditText);
        complementEditText = findViewById(R.id.complementEditText);
        codePostalEditText = findViewById(R.id.codePostalEditText);
        villeEditText = findViewById(R.id.villeEditText);

        checkBoxParking = findViewById(R.id.checkBoxParking);
        checkBoxTicketResto = findViewById(R.id.checkBoxTicketResto);
        checkBoxTeletravail = findViewById(R.id.checkBoxTeletravail);



       Button creaOffreButton = findViewById(R.id.boutton_creationOffre);
        creaOffreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOffreToFirestore();
            }
        });

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreationOffreActivity.this, NavbarActivity.class);
                i.putExtra("typeCompte", typeCompte);
                i.putExtra("fragment", "Offre");
                startActivity(i);
            }
        });
    }


    private void addOffreToFirestore() {
        if (!validateInput()) {
            return;
        }
        titre = titreOffreEditText.getText().toString();
        type = typePosteEditText.getText().toString();
        description = descriptionOffreEditText.getText().toString();
        dateDeb = dateDebEditText.getText().toString();
        dateFin = dateFinEditText.getText().toString();
        remuneration = remunerationEditText.getText().toString();
        rue = rueEditText.getText().toString();
        complement = complementEditText.getText().toString();
        codePostal = codePostalEditText.getText().toString();
        ville = villeEditText.getText().toString();

        parking = checkBoxParking.isChecked();
        ticketResto = checkBoxTicketResto.isChecked();
        teletravail = checkBoxTeletravail.isChecked();

        if (remuneration.contains("€/h")){
            remuneration = remuneration.replace("€/h", "");
        }
        else if (remuneration.contains("€")){
            remuneration = remuneration.replace("€", "");
        }

        Map<String, Object> offre = new HashMap<>();
        offre.put("titre", titre);
        offre.put("type", type);
        offre.put("description", description);
        offre.put("dateDeb", dateDeb);
        offre.put("dateFin", dateFin);
        offre.put("remuneration", remuneration);
        offre.put("rue", rue);
        offre.put("complement", complement);
        offre.put("codePostal", codePostal);
        offre.put("ville", ville);

        offre.put("parking", parking);
        offre.put("ticketResto", ticketResto);
        offre.put("teletravail", teletravail);

        offre.put("createur", mAuth.getCurrentUser().getUid());

        db.collection("offres")
                .add(offre)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Offre ajoutée à la BDD");
                    Toast.makeText(CreationOffreActivity.this,R.string.offreCree,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CreationOffreActivity.this, NavbarActivity.class);
                    i.putExtra("typeCompte", typeCompte);
                    i.putExtra("fragment", "Offre");
                    startActivity(i);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout de l'offre dans la BDD", e));

                
    }


    private boolean validateInput() {
        titre = titreOffreEditText.getText().toString();
        type = typePosteEditText.getText().toString();
        description = descriptionOffreEditText.getText().toString();
        dateDeb = dateDebEditText.getText().toString();
        dateFin = dateFinEditText.getText().toString();
        remuneration = remunerationEditText.getText().toString();
        rue = rueEditText.getText().toString();
        complement = complementEditText.getText().toString();
        codePostal = codePostalEditText.getText().toString();
        ville = villeEditText.getText().toString();

        boolean check = true;
        if (titre.isEmpty()) {
            titreOffreEditText.setError(getString(R.string.erreur_titre_offre));
            check = false;
        }
        if (type.isEmpty()) {
            typePosteEditText.setError(getString(R.string.erreur_type_offre));
            check = false;
        }
        if (description.isEmpty()) {
            descriptionOffreEditText.setError(getString(R.string.erreur_description_offre));
            check = false;
        }
        if (dateDeb.isEmpty()) {
            dateDebEditText.setError(getString(R.string.erreur_date_deb_offre));
            check = false;
        }
        if (dateFin.isEmpty()) {
            dateFinEditText.setError(getString(R.string.erreur_date_fin_offre));
            check = false;
        }
        if (remuneration.isEmpty()) {
            remunerationEditText.setError(getString(R.string.erreur_remuneration_offre));
            check = false;
        }
        if (remuneration.matches("[0-9]+")) {
            int remun = Integer.parseInt(remuneration);
            if (remun < 0) {
                remunerationEditText.setError(getString(R.string.erreur_remuneration_offre));
                check = false;
            }
        }
        else {
            remunerationEditText.setError(getString(R.string.erreur_remuneration_offre));
            check = false;
        }

        if (rue.isEmpty()) {
            rueEditText.setError(getString(R.string.erreur_rue_offre));
            check = false;
        }
        if (codePostal.isEmpty() || codePostal.length() != 5) {
            codePostalEditText.setError(getString(R.string.erreur_code_postal_offre));
            check = false;
        }


        return check;
    }
}