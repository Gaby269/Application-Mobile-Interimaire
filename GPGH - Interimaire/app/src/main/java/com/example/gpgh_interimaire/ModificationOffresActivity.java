package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ModificationOffresActivity extends AppCompatActivity {

    String TAG = "ModificationOffresActivity";
    FirebaseFirestore db;

    EditText editTitre, editType, editDescription, editDateDebut, editDateFin, editPrix, editParking;
    String id_offre, is_details;

    private Calendar calendar;
    private Calendar dateCalendarDeb;
    private Calendar minDateCalendarFin, dateCalendarFin;
    private DatePickerDialog datePickerDialogDeb;
    private DatePickerDialog datePickerDialogFin;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_offres);

        Intent i = getIntent();
        is_details = i.getStringExtra("is_details");
        id_offre = i.getStringExtra("idOffre");
        
        db = FirebaseFirestore.getInstance();

        editTitre = findViewById(R.id.editTitre);
        editType = findViewById(R.id.editType);
        editDescription = findViewById(R.id.editDescription);
        editDateDebut = findViewById(R.id.editDateDebut);
        editDateFin = findViewById(R.id.editDateFin);
        editPrix = findViewById(R.id.editPrix);
        editParking = findViewById(R.id.editParking);
        calendar = Calendar.getInstance();

        fetchOffreInfo(id_offre);

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (is_details.equals("true")) {
                    i = new Intent(ModificationOffresActivity.this, AfficherDetailsOffreActivity.class);
                    i.putExtra("idOffre", id_offre);
                }
                else{
                    i = new Intent(ModificationOffresActivity.this, NavbarActivity.class);
                    i.putExtra("fragment", "Offre");
                }
                i.putExtra("typeCompte", "Entreprise");
                startActivity(i);
            }
        });

        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOffreInfo(id_offre);
            }
        });

        ImageView suppressionButton = findViewById(R.id.image_delete);
        suppressionButton.setOnClickListener(view -> supprimerOffre(id_offre));
    }

    // Pour les dates
    public void showDebDatePickerDialog(View view) {
        // Récupérer la date de début par défaut
        String debutDateText = editDateDebut.getText().toString();

        // Convertir la date de début en objet de type Calendar
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date debutDate = sdf.parse(debutDateText);
            Log.d(TAG, String.valueOf(debutDate));

            // Mettre à jour la date minimale du champ de date de fin (date de début + 1 jour)
            Log.d(TAG, String.valueOf(dateCalendarDeb));
            dateCalendarDeb= Calendar.getInstance();
            dateCalendarDeb.setTime(debutDate);

            // Créer le DatePickerDialog avec la date minimale initiale
            datePickerDialogDeb = new DatePickerDialog(this, startDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            // Mettre à jour la date sélectionnée dans le calendrier du DatePickerDialog
            datePickerDialogDeb.updateDate(dateCalendarDeb.get(Calendar.YEAR),
                    dateCalendarDeb.get(Calendar.MONTH),
                    dateCalendarDeb.get(Calendar.DAY_OF_MONTH));

            datePickerDialogDeb.show();
            datePickerDialogDeb.show();
        } catch (ParseException e) {
            editDateDebut.setError(getString(R.string.erreur_date_deb_offre));
        }
    }
    public void showFinDatePickerDialog(View view) throws ParseException {
        // Récupérer la date de début par défaut
        String debutDateText = editDateDebut.getText().toString();
        String finDateText = editDateFin.getText().toString();

        // Convertir la date de début en objet de type Calendar
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date debutDate = sdf.parse(debutDateText);
            Date finDate = sdf.parse(finDateText);

            // Mettre à jour la date minimale du champ de date de fin (date de début + 1 jour)
            Log.d(TAG, String.valueOf(minDateCalendarFin));
            minDateCalendarFin= Calendar.getInstance();
            minDateCalendarFin.setTime(debutDate);
            minDateCalendarFin.add(Calendar.DAY_OF_MONTH, 1);
            dateCalendarFin= Calendar.getInstance();
            dateCalendarFin.setTime(finDate);

            // Créer le DatePickerDialog avec la date minimale initiale
            datePickerDialogFin = new DatePickerDialog(this, endDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            // Mettre à jour la date sélectionnée dans le calendrier du DatePickerDialog
            datePickerDialogFin.updateDate(dateCalendarFin.get(Calendar.YEAR),
                    dateCalendarFin.get(Calendar.MONTH),
                    dateCalendarFin.get(Calendar.DAY_OF_MONTH));
            // Mettre à jour la date minimale du champ de date de fin (date de début + 1 jour)
            datePickerDialogFin.getDatePicker().setMinDate(minDateCalendarFin.getTimeInMillis());
            datePickerDialogFin.show();
        } catch (ParseException e) {
            editDateDebut.setError(getString(R.string.erreur_date_deb_offre));
        }


    }

    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            editDateDebut.setText(selectedDate);
        }
    };

    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            editDateFin.setText(selectedDate);
        }
    };



    private void fetchOffreInfo(String offreId) {

        db.collection("offres")
                .document(offreId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String titre = documentSnapshot.getString("titre");
                        String type = documentSnapshot.getString("type");
                        String description = documentSnapshot.getString("description");
                        String dateDebut = documentSnapshot.getString("dateDeb");
                        String dateFin = documentSnapshot.getString("dateFin");
                        String prix = documentSnapshot.getString("remuneration");
                        String parking = documentSnapshot.getString("parking");

                        editTitre.setText(titre);
                        editType.setText(type);
                        editDescription.setText(description);
                        editDateDebut.setText(dateDebut);
                        editDateFin.setText(dateFin);
                        editPrix.setText(prix);
                        editParking.setText(parking);
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la récupération des infos de l'offre", e));
    }



    private void updateOffreInfo(String offreId) {

        displayLoadingScreen();

        String titre = editTitre.getText().toString();
        String type = editType.getText().toString();
        String description = editDescription.getText().toString();
        String dateDebut = editDateDebut.getText().toString();
        String dateFin = editDateFin.getText().toString();
        String prix = editPrix.getText().toString();
        String parking = editParking.getText().toString();

        // données de l'utilisateur
        db.collection("offres")
                .document(offreId)
                .update("titre", titre,
                        "type", type,
                        "description", description,
                        "dateDebut", dateDebut,
                        "dateFin", dateFin,
                        "prix", prix,
                        "parking", parking)
                .addOnSuccessListener(aVoid -> {
                    dismissLoadingScreen();
                    Toast.makeText(ModificationOffresActivity.this, R.string.compteModif, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ModificationOffresActivity.this, LoadingNavbarActivity.class);
                    i.putExtra("fragment", "Offres");
                    startActivity(i);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de la mise à jour des informations", Toast.LENGTH_SHORT).show());
    }


    private void supprimerOffre(String offreId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModificationOffresActivity.this);
        builder.setMessage(R.string.suppression_offre_message)
                .setTitle(R.string.suppression_offre_titre)
                .setPositiveButton(R.string.oui, (dialog, id) -> {
                    db.collection("offres").document(offreId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Offre supprimée avec succès : " + offreId);
                            Toast.makeText(ModificationOffresActivity.this, "Offre supprimée",Toast.LENGTH_SHORT).show();

                            // suppression des favoris de l'offre
                            db.collection("favoris")
                            .whereEqualTo("offreId", offreId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        // boucler sur tous les favoris de l'offre
                                        for (DocumentSnapshot fav : queryDocumentSnapshots.getDocuments()) {
                                            String id_fav = fav.getId();
                                            db.collection("favoris").document(id_fav)
                                                .delete()
                                                .addOnSuccessListener(aVoid1 -> {
                                                    Log.d(TAG, "Favori supprimé avec succès : " + id_fav);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e(TAG, "Erreur lors de la suppression du favori", e);
                                                });
                                        }
                                    }

                                    Intent i = new Intent(ModificationOffresActivity.this, NavbarActivity.class);
                                    i.putExtra("fragment", "Offre");
                                    i.putExtra("typeCompte", "Entreprise");
                                    startActivity(i);

                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Erreur lors de la récupération des favoris", e);
                                });

                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Erreur lors de la suppression de l'offre", e);
                        });
                })
                .setNegativeButton(R.string.annuler, (dialog, id) -> { dialog.dismiss(); });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void displayLoadingScreen() {
        FragLoading loadingFragment = FragLoading.newInstance("Enregistrement...");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, loadingFragment, "loading_fragment");
        transaction.commit();
    }

    public void dismissLoadingScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragLoading loadingFragment = (FragLoading) fragmentManager.findFragmentByTag("loading_fragment");

        if (loadingFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(loadingFragment);
            transaction.commit();
        }
    }


}