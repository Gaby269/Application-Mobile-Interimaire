package com.example.tp1_premireapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


class Trajet {
    String depart;
    String arrive;
    String date_horaire;
}

public class TrainActivity extends AppCompatActivity {


    Random rand = new Random();

    @SuppressLint({"ResourceType", "SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        // Valider avec un bouton et action lors d'un click
        Button buttonTrain = findViewById(R.id.boutonRecherche);
        buttonTrain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Création d'une liste de trajet entre deux villes
                String[] villes = {"Montpellier", "Béziers", "Toulouse", "Paris", "Lyon", "Grenoble", "Valence", "Versaille"};
                Trajet[] voyage = new Trajet[80];   //on prevoit 80 voyage en tout

                //Pour chaque trajet
                for (int j = 0; j < 80; j++) {

                    //Choix des villes pour le trajet
                    String villeDepart = villes[rand.nextInt(villes.length)];
                    String villeArrive = villes[rand.nextInt(villes.length)];

                    //Verification que les deux villes ne sont pas identique on utilise un while pour si jamais c'est tjs le cas on continue
                    while (villeDepart.equals(villeArrive)) {
                        villeArrive = villes[rand.nextInt(villes.length)];  //on choisis une autre ville
                    }

                    //Pour la date
                    int annee = 2023;  //on choisis de faire des traget quedansl'année 2023
                    int mois = rand.nextInt(12)+1;  //on veut lesmois de 1 a 12
                    int jour;
                    //pour les jours on doit faire pour chaque mois
                    if (mois == 1 || mois == 3 || mois == 5 || mois == 7 || mois ==  8 || mois == 10 || mois == 12){
                        jour = rand.nextInt(31)+1;  //mois a 31 jours
                    }
                    else if (mois == 4 || mois == 6 || mois == 9 || mois == 11) {
                        jour = rand.nextInt(30)+1;  //mois a 30 jours
                    }
                    else {
                        jour = rand.nextInt(28)+1;  //mois a 28 jours
                    }

                    //Pour les horaires
                    int heure = rand.nextInt(24);
                    int minutes = rand.nextInt(60);

                    //Pour date et horaire
                    String date = jour + "/" + mois+ "/" + annee;
                    String horaire = heure + ":" + minutes;

                    //Ajout au voyage
                    Trajet tj = new Trajet();
                    tj.depart = villeDepart;
                    tj.arrive = villeArrive;
                    tj.date_horaire = date + " - " + horaire;

                    //Ajout a la liste des voyages
                    voyage[j] = tj;

                }

                //Recuperaitiondes emplacement des elements sur lappli
                ListView list_trajet = findViewById(R.id.list_trajet);
                EditText departEdit = findViewById(R.id.editTextDepart);
                EditText arriveEdit = findViewById(R.id.editTextArrive);
                ArrayList<String> list_tj = new ArrayList<>();



                //Bouton pour valider les trains
                Button buttonTrain = findViewById(R.id.boutonRecherche);
                buttonTrain.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Affichage de la liste
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TrainActivity.this, android.R.layout.simple_list_item_1, list_tj);
                        list_trajet.setAdapter(arrayAdapter);


                        // Verification des champs pour être sur qu'ils soient remplit

                        if (departEdit.getText().toString().length() <= 1 || arriveEdit.getText().toString().length() <= 1){
                              Toast.makeText(TrainActivity.this,R.string.avertissement,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //On supprimer les trajet déjà present dans la liste
                            list_tj.removeAll(list_tj);

                            //Pour chaque trajet
                            for (Trajet trajet_i : voyage){

                                //Ajout des trajets dans la liste à afficher si c'est le bon départ et le bon arrivé
                                if ((trajet_i.depart).equals(departEdit.getText().toString()) && (trajet_i.arrive).equals(arriveEdit.getText().toString())) {
                                    list_tj.add(trajet_i.date_horaire + " : " + trajet_i.depart + " - " + trajet_i.arrive);   //pour ajouter dans la liste
                                }

                            }

                            //Si jamais la liste est vide on indique
                            if(list_tj.size() == 0) {
                                list_tj.add("Pas de trajet pour " + departEdit.getText().toString() + " - " + arriveEdit.getText().toString());
                            }

                            arrayAdapter.notifyDataSetChanged(); //pour mettre a jour le adapter

                        }
                    }
                });






            }
        });

        // Retouner avec un bouton et action lors d'un click
        Button buttonAccueil = findViewById(R.id.boutonAccueil);
        buttonAccueil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent i = new Intent(TrainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

}


