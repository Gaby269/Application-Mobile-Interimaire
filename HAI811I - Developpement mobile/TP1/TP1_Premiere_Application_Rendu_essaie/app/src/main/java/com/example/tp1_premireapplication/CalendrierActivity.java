package com.example.tp1_premireapplication;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class Evenement {
    String nomEvenement;
    int annee;
    int mois;
    int jour;
}


public class CalendrierActivity extends AppCompatActivity {

    /////////////////////////////
    // AFFICHAGE DU CALENDRIER //
    /////////////////////////////

    ArrayAdapter arrayAdapter;
    ArrayList<Evenement> evenements = new ArrayList<Evenement>();
    //Création d'une liste d'évenement avec une date
    String[] nomEven = {"Anniversaire", "Jouer au ballon", "Aller chercher un chat", "Manger avec Toto"};
    Evenement newEvenement = new Evenement();
    ArrayList<Evenement> NewEvenements = new ArrayList<Evenement>();
    Evenement evenement;

    public void construireEvent(){
        //Ajout de mon anniversaire à l'évènement 0 il faudrait mettre pour chaqque annee que c'est le même evenement
        Evenement ev0 = new Evenement();
        ev0.nomEvenement = nomEven[0];
        ev0.mois = 7;
        ev0.jour = 5;
        evenements.add(ev0);

        //Ajout de jouer au ballon le 4 février 2023, le 19 mars, et le 13 novembre
        Evenement ev1 = new Evenement();
        ev1.nomEvenement = nomEven[1];
        ev1.annee = 2023;
        ev1.mois = 1;   //fevrier car commence a 0
        ev1.jour = 4;
        evenements.add(ev1);

        Evenement ev2 = new Evenement();
        ev2.nomEvenement = nomEven[1];
        ev2.annee = 2023;
        ev2.mois = 2;
        ev2.jour = 19;
        evenements.add(ev2);

        Evenement ev3 = new Evenement();
        ev3.nomEvenement = nomEven[1];
        ev3.annee = 2023;
        ev3.mois = 10;
        ev3.jour = 13;
        evenements.add(ev3);

        Evenement ev6 = new Evenement();
        ev6.nomEvenement = nomEven[1];
        ev6.annee = 2023;
        ev6.mois = 1;
        ev6.jour = 9;
        evenements.add(ev6);

        //Ajout d'aller chercher un chat le 9 février 2023
        Evenement ev4 = new Evenement();
        ev4.nomEvenement = nomEven[2];
        ev4.annee = 2023;
        ev4.mois = 1;
        ev4.jour = 9;
        evenements.add(ev4);

        //Ajout de manger avec toto le 23 février 2023
        Evenement ev5 = new Evenement();
        ev5.nomEvenement = nomEven[3];
        ev5.annee = 2023;
        ev5.mois = 1;
        ev5.jour = 23;
        evenements.add(ev5);


    }



    @SuppressLint({"ResourceType", "SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);
        construireEvent();

        //Modification du calendrier
        CalendarView calendrier = findViewById(R.id.calendarView);


        //Récuperation de l'id de la liste des evenements
        ListView list_even = findViewById(R.id.list_even);
        ArrayList<String> list_evenement = new ArrayList<>();
        //Lier la liste créer a celle a afficher
        arrayAdapter = new ArrayAdapter<>(CalendrierActivity.this, android.R.layout.simple_list_item_1, list_evenement);
        list_even.setAdapter(arrayAdapter);


        //Lorsqu'on change de date
        calendrier.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //On supprimer les elements deja present dans la liste
                list_evenement.removeAll(list_evenement);

                //Ajout de la date
                list_evenement.add(dayOfMonth + "/" + (month+1) + "/" + year);    //ajout del'evenement dans laliste a afficher


                //Recuperaion de l'evenement
                if (!NewEvenements.isEmpty()) {
                    for (Evenement newEv : NewEvenements) {
                        //Toast.makeText(CalendrierActivity.this, "" + newEv.jour + "", Toast.LENGTH_SHORT).show();
                        //Ajout de l'évènement dansletableau
                        evenements.add(newEv);
                        Toast.makeText(CalendrierActivity.this, "" + (evenements.get(evenements.size()-1)).mois + "", Toast.LENGTH_SHORT).show();
                        if ((newEv.jour == dayOfMonth) && (newEv.mois == month) && (newEv.annee == year)) {     //si c'est le bon jour
                            list_evenement.add(newEv.nomEvenement);    //ajout del'evenement dans laliste a afficher
                        }
                        NewEvenements.add(newEv);
                    }
                }
                /*if (newEvenement != null) {
                    //Une fois les verifications faites, on regarde si levenement du jour est ajd ? et on l'affiche si c'est le cas
                    if ((newEvenement.jour == dayOfMonth) && (newEvenement.mois == month) && (newEvenement.annee == year)) {     //si c'est le bon jour
                        //on remet tout a la normal
                        Toast.makeText(CalendrierActivity.this, newEvenement.nomEvenement, Toast.LENGTH_SHORT).show();
                        list_evenement.add(newEvenement.nomEvenement);    //ajout del'evenement dans laliste a afficher
                    }
                }*/



                //Affichage des évènements du jour
                for (Evenement ev : evenements){    //pour chaque evenement

                    //Si le champ jour pour indiquer tous els jour est vide alors on met le jour d'ajd comme on a eliminer les autres
                    if (ev.jour == 0) {
                        ev.jour = dayOfMonth;
                    }//de meme pour les autres
                    if (ev.mois == -1) {
                        ev.mois = month;
                    }
                    if (ev.annee == 0) {
                        ev.annee = year;
                    }
                    //Une fois les verifications faites, on regarde si levenement du jour est ajd ? et on l'affiche si c'est le cas
                    if ((ev.jour == dayOfMonth) && (ev.mois == month) && (ev.annee == year)){     //si c'est le bon jour
                        Toast.makeText(CalendrierActivity.this,ev.nomEvenement,Toast.LENGTH_SHORT).show();
                        //on remet tout a la normal
                        list_evenement.add(ev.nomEvenement);    //ajout del'evenement dans laliste a afficher
                    }
                }

                //Si jamais la liste est vide on indique
                if (list_evenement.size() == 1) {
                    String pas = "Pas d'évènements pour ce jour";
                    list_evenement.add(pas);
                }

                //Mise à jour
                arrayAdapter.notifyDataSetChanged();
            }
        });


        //Quand on click ?
        EditText newEven = findViewById(R.id.ajout_ev);
        Button button_ajout = findViewById(R.id.boutonAjout);
        button_ajout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //Recuperation de la date selectionner de l'évenement
                long dateActuel = calendrier.getDate();
                //Transformation en date reel
                @SuppressLint("SimpleDateFormat") String dateFormatJour = new SimpleDateFormat("dd").format(new Date(dateActuel));
                @SuppressLint("SimpleDateFormat") String dateFormatMois = new SimpleDateFormat("MM").format(new Date(dateActuel));
                @SuppressLint("SimpleDateFormat") String dateFormatAnnee = new SimpleDateFormat("yyyy").format(new Date(dateActuel));

                //Récuperation du texte dans l'edit

                String evenementAjout = newEven.getText().toString();
                if (evenementAjout.isEmpty()){
                    Toast.makeText(CalendrierActivity.this,R.string.avertissement,Toast.LENGTH_SHORT).show();
                }
                else {

                    newEvenement.nomEvenement = evenementAjout;
                    newEvenement.mois = Integer.parseInt(dateFormatMois);
                    newEvenement.jour = Integer.parseInt(dateFormatJour);
                    newEvenement.annee = Integer.parseInt(dateFormatAnnee);

                    //Ajout d'un element dans un taleau plus grand
                    Toast.makeText(CalendrierActivity.this, R.string.ajout_even, Toast.LENGTH_SHORT).show();

                    //Si il n'y a pas d'évenement la taille de la liste est 2 et on supprime la ligne comme quoi il n'y a pas d'évènement
                    if (list_evenement.size() == 2){
                        list_evenement.remove("Pas d'évènements pour ce jour");
                    }
                    list_evenement.add(newEvenement.nomEvenement);

                    //on rend vide le edit Text
                    newEven.setText("");

                    //ajout de l'evenement
                    NewEvenements.add(newEvenement);
                    evenements.add(newEvenement);

                    //Mise à jour
                    arrayAdapter.notifyDataSetChanged();


                }
            }
        });






        Button button_language = findViewById(R.id.bouton_langage);
        button_language.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String languageToLoad;
                if (Locale.getDefault().getLanguage() == "en") {
                    languageToLoad= "fr";
                } else {
                    languageToLoad= "en";
                }
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                recreate();
            }
        });


    }



}

