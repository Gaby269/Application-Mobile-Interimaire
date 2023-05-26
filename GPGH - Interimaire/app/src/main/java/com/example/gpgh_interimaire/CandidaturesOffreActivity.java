package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CandidaturesOffreActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatures_offre);

        // Récupération de l'intent
        Intent i = getIntent();
        String typeCompte = i.getStringExtra("typeCompte");
        String titreOffre = i.getStringExtra("titreOffre");

        // Affichage du titre et de la description
        TextView titreOffreTextView = findViewById(R.id.titreTextView);
        TextView descriptionOffreTextView = findViewById(R.id.descriptionTextView);
        titreOffreTextView.setText(titreOffre);

        RecyclerView recyclerView = findViewById(R.id.recycleviewEntreprise);

        List<ItemCandidature> items = new ArrayList<ItemCandidature>();
        items.add(new ItemCandidature("0", "1", "John", "Doe", "Expérience en tant que magasinier", "Disponible immédiatement", "cv_john_doe.pdf"));
        items.add(new ItemCandidature("0", "2", "Jane", "Smith", "Compétences en secrétariat et gestion administrative", "Bilingue français-anglais", "cv_jane_smith.pdf"));
        items.add(new ItemCandidature("0", "3", "Michael", "Johnson", "Expérience en tant que chauffeur-livreur", "Permis de conduire valide", "cv_michael_johnson.pdf"));
        items.add(new ItemCandidature("0", "4", "Emily", "Brown", "Compétences en marketing digital", "Maîtrise des réseaux sociaux", "cv_emily_brown.pdf"));
        items.add(new ItemCandidature("0", "5", "Daniel", "Davis", "Expérience en tant que technicien informatique", "Certification Cisco CCNA", "cv_daniel_davis.pdf"));

        Button btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapterCandidature(this, items, typeCompte));

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CandidaturesOffreActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });




    }


    @SuppressLint("MissingInflatedId")
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrer");

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        // Récupérez les références des éléments de filtrage dans la vue
         EditText etMinPrice = dialogView.findViewById(R.id.etMinPrice);
        EditText etMaxPrice = dialogView.findViewById(R.id.etMaxPrice);

        // Ajoutez d'autres références pour les éléments de filtrage supplémentaires

        // Ajoutez les boutons "Appliquer" et "Annuler"
        builder.setPositiveButton("Appliquer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Récupérez les valeurs sélectionnées dans les éléments de filtrage
                String minPrice = etMinPrice.getText().toString();
                String maxPrice = etMaxPrice.getText().toString();

                // Appliquez les filtres avec les valeurs récupérées
                //applyFilters(minPrice, maxPrice);
            }
        });

        SeekBar seekbarPrice = dialogView.findViewById(R.id.seekbarPriceMin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekbarPrice.setMin(0); // Valeur minimale de la plage
        }
        seekbarPrice.setMax(100); // Valeur maximale de la plage

        seekbarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Mettez à jour les valeurs affichées en fonction de la position de la SeekBar
                int minPrice = progress; // Par exemple, valeur minimale en euros
                int maxPrice = seekBar.getMax(); // Par exemple, valeur maximale en euros

                // Affichez les valeurs sélectionnées pour le prix minimum et maximum
                // Vous pouvez les afficher dans des TextView ou toute autre vue de votre choix
                // Pour l'exemple, affichons-les dans la console
                Log.d("Candidature", "Prix minimum : " + minPrice);
                Log.d("Candidature", "Prix maximum : " + maxPrice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Actions à effectuer lorsque l'utilisateur commence à glisser la SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Actions à effectuer lorsque l'utilisateur arrête de glisser la SeekBar
            }
        });

        builder.setNegativeButton("Annuler", null);

        // Affichez la boîte de dialogue
        builder.create().show();
    }


}