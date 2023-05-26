package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CreationOffreActivity extends AppCompatActivity {

    static final String TAG = "CreationOffre1Activity";
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    Spinner typeOffreSpinner;

    private Calendar calendar;
    private Calendar minDateCalendarDeb;
    private Calendar minDateCalendarFin;
    
    EditText titreOffreEditText, typePosteEditText, descriptionOffreEditText, dateDebEditText, dateFinEditText, remunerationEditText;
    EditText rueEditText, complementEditText, codePostalEditText, villeEditText, placeParkingEditText;
    CheckBox checkBoxTicketResto, checkBoxTeletravail;

    String titre, type, description, dateDeb, dateFin, remuneration, rue, complement, codePostal, ville, parking;
    boolean ticketResto, teletravail;
    String typeCompte, nomEntreprise;

    String selectedTypeOffre = "Stage";


    private DatePickerDialog datePickerDialogDeb;
    private DatePickerDialog datePickerDialogFin;



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
        typeOffreSpinner = findViewById(R.id.typeOffreSpinner);
        descriptionOffreEditText = findViewById(R.id.descriptionOffreEditText);
        remunerationEditText = findViewById(R.id.remunerationEditText);
        rueEditText = findViewById(R.id.rueEditText);
        complementEditText = findViewById(R.id.complementEditText);
        codePostalEditText = findViewById(R.id.codePostalEditText);
        villeEditText = findViewById(R.id.villeEditText);

        dateDebEditText = findViewById(R.id.dateDebEditText);
        dateFinEditText = findViewById(R.id.dateFinEditText);
        calendar = Calendar.getInstance();

        minDateCalendarDeb = Calendar.getInstance();

        datePickerDialogDeb = new DatePickerDialog(this, startDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        placeParkingEditText = findViewById(R.id.parkingEditText);
        checkBoxTicketResto = findViewById(R.id.checkBoxTicketResto);
        checkBoxTeletravail = findViewById(R.id.checkBoxTeletravail);

        // récupération du nom de l'entreprise depuis la BDD
        db.collection("entreprises").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        nomEntreprise = documentSnapshot.getString("nom");
                    } else {
                        Log.d(TAG, "Pas d'entreprise trouvée avec cet ID");
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "Erreur lors de la récupération ", e));

        // On regarde si on change le spinner
        typeOffreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeOffre = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Récupération du Spinner déclaré dans le fichier activity_inscription.xml
        Spinner typeOffreSpinner = (Spinner) findViewById(R.id.typeOffreSpinner);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        List<String> listType = new ArrayList<>();
        listType.add("Sélectionez un type ...");
        listType.add("Stage");
        listType.add("Mission");
        listType.add("CDD");
        // ArrayAdapter pour le spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOffreSpinner.setAdapter(adapterSpinner); // on passe l'adapter au Spinner



        // Bouton TMP à retirer
        Button tmpButton = findViewById(R.id.boutton_tmp);
        tmpButton.setOnClickListener(view -> {
            // On met des données random dans les champs
            String randTitre = Integer.toString(new Random().nextInt(9000)+1000); //entre 1000 et 9999
            String randSalaire = Integer.toString(new Random().nextInt(30)+10); //entre 10 et 40
            String randJour = Integer.toString(new Random().nextInt(19)+10); //entre 10 et 28
            Integer intRandMois = new Random().nextInt(6)+6; //entre 6 et 11
            String randMois1 = Integer.toString(intRandMois);
            String randMois2 = Integer.toString(intRandMois+1);
            // ajouter un 0 avant si mois < 10
            randMois1 = (intRandMois < 10) ? "0"+randMois1 : randMois1;
            randMois2 = (intRandMois+1 < 10) ? "0"+randMois2 : randMois2;


            titreOffreEditText.setText("Offre "+randTitre);
            descriptionOffreEditText.setText("Description Offre");
            remunerationEditText.setText(randSalaire);
            rueEditText.setText("Rue de la rue");
            complementEditText.setText("Complément");
            codePostalEditText.setText("75000");
            villeEditText.setText("Paris");
            dateDebEditText.setText(randJour+"/"+randMois1+"/2023");
            dateFinEditText.setText(randJour+"/"+randMois2+"/2023");
            placeParkingEditText.setText(randTitre);
            //cocher au hasard les checkbox
            checkBoxTicketResto.setChecked(new Random().nextBoolean());
            checkBoxTeletravail.setChecked(new Random().nextBoolean());

            try {
                addOffreToFirestore();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });


        Button creaOffreButton = findViewById(R.id.boutton_creationOffre);
        creaOffreButton.setOnClickListener(view -> {
            try {
                addOffreToFirestore();
            } catch (ParseException e) {
                throw new RuntimeException(e);
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


    private void addOffreToFirestore() throws ParseException {
        if (!validateInput()) {
            return;
        }
        titre = titreOffreEditText.getText().toString();
        type = selectedTypeOffre;
        description = descriptionOffreEditText.getText().toString();
        dateDeb = dateDebEditText.getText().toString();
        dateFin = dateFinEditText.getText().toString();
        remuneration = remunerationEditText.getText().toString();
        rue = rueEditText.getText().toString();
        complement = complementEditText.getText().toString();
        codePostal = codePostalEditText.getText().toString();
        ville = villeEditText.getText().toString();
        parking = placeParkingEditText.getText().toString();
        ticketResto = checkBoxTicketResto.isChecked();
        teletravail = checkBoxTeletravail.isChecked();

        if (remuneration.contains("€/h")){
            remuneration = remuneration.replace("€/h", "");
        }
        else if (remuneration.contains("€")){
            remuneration = remuneration.replace("€", "");
        }
        if (parking.contains("places")){
            parking = parking.replace("places", "");
        }
        else if (parking.contains("place")){
            parking = parking.replace("place", "");
        }

        Map<String, Object> offre = new HashMap<>();
        offre.put("titre", titre);
        offre.put("nomEntreprise", nomEntreprise);
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



    private boolean validateInput() throws ParseException {
        titre = titreOffreEditText.getText().toString();
        type = selectedTypeOffre;
        description = descriptionOffreEditText.getText().toString();
        dateDeb = dateDebEditText.getText().toString();
        dateFin = dateFinEditText.getText().toString();
        remuneration = remunerationEditText.getText().toString();
        rue = rueEditText.getText().toString();
        complement = complementEditText.getText().toString();
        codePostal = codePostalEditText.getText().toString();
        ville = villeEditText.getText().toString();
        parking = placeParkingEditText.getText().toString();

        boolean check = true;
        if (titre.isEmpty()) {
            titreOffreEditText.setError(getString(R.string.erreur_titre_offre));
            check = false;
        }
        Log.d(TAG, type);
        if (typeOffreSpinner.getSelectedItemPosition() == 0) {
            // Premier élément sélectionné, afficher un message d'erreur
            ((TextView) typeOffreSpinner.getSelectedView()).setError(getString(R.string.erreur_type_offre));
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
        if (ville.isEmpty()) {
            villeEditText.setError(getString(R.string.erreur_ville));
            check = false;
        }
        if (codePostal.isEmpty() || codePostal.length() != 5) {
            codePostalEditText.setError(getString(R.string.erreur_code_postal_offre));
            check = false;
        }
        if (parking.isEmpty()) {
            placeParkingEditText.setError(getString(R.string.erreur_parking));
            check = false;
        }


        return check;
    }

    // Pour les dates
    public void showDebDatePickerDialog(View view) {
        // Définir les dates minimale et maximale autorisées
        datePickerDialogDeb.getDatePicker().setMinDate(minDateCalendarDeb.getTimeInMillis());
        datePickerDialogDeb.show();
    }

    public void showFinDatePickerDialog(View view) throws ParseException {
        // Récupérer la date de début par défaut
        String debutDateText = dateDebEditText.getText().toString();

        // Convertir la date de début en objet de type Calendar
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date debutDate = sdf.parse(debutDateText);
            Log.d(TAG, String.valueOf(debutDate));

            // Mettre à jour la date minimale du champ de date de fin (date de début + 1 jour)
            Log.d(TAG, String.valueOf(minDateCalendarFin));
            minDateCalendarFin= Calendar.getInstance();
            minDateCalendarFin.setTime(debutDate);
            minDateCalendarFin.add(Calendar.DAY_OF_MONTH, 1);

            // Créer le DatePickerDialog avec la date minimale initiale
            datePickerDialogFin = new DatePickerDialog(this, endDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            // Mettre à jour la date minimale du champ de date de fin (date de début + 1 jour)
            datePickerDialogFin.getDatePicker().setMinDate(minDateCalendarFin.getTimeInMillis());
            datePickerDialogFin.show();
        } catch (ParseException e) {
            dateDebEditText.setError(getString(R.string.erreur_date_deb_offre));
        }


    }

    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            dateDebEditText.setText(selectedDate);
        }
    };

    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            dateFinEditText.setText(selectedDate);
        }
    };
}