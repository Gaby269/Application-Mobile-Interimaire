package com.example.tp1_premireapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CalendrierActivity extends AppCompatActivity {

    CalendarView calendar;
    ListView events_list_view;
    LinearLayout ll;
    EditText text_view_add;
    Button button_add;
    String selected_date;

    ArrayAdapter<String> adapter;
    HashMap<String, List<String>> events_hashmap;
    List<String> events_list_string;
    Random rand = new Random();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);

        //Recuperation des elements du xml
        calendar = findViewById(R.id.calendar);
        events_list_view = findViewById(R.id.list_events);
        ll = findViewById(R.id.ll);
        text_view_add = findViewById(R.id.text_view_add);
        button_add = findViewById(R.id.button_add);


        //Declaration des données
        events_hashmap = new HashMap<>();
        events_list_string = new ArrayList<>();
        adapter = new ArrayAdapter<>(CalendrierActivity.this, android.R.layout.simple_list_item_1);
        events_list_view.setAdapter(adapter);


        //date courrante
        Calendar today = Calendar.getInstance();
        int yearToday = today.get(Calendar.YEAR);
        int monthToday = today.get(Calendar.MONTH);
        int dayOfMonthToday = today.get(Calendar.DAY_OF_MONTH);
        selected_date = Integer.toString(yearToday)+Integer.toString(monthToday)+Integer.toString(dayOfMonthToday);

        //set de dates aléatoires
        events_list_string = events_hashmap.get(selected_date);
        if (events_list_string == null) {
            events_list_string = new ArrayList<>();
        }
        //Mise à jour du hashmap
        events_list_string.add(getResources().getString(R.string.new_event_title)+" "+String.format("%02d", dayOfMonthToday)+" "+String.format("%02d", monthToday)+" "+yearToday);
        events_hashmap.put(selected_date, events_list_string);

        //Mise à jour de l'affichage
        adapter.addAll(events_list_string); //ajout de la liste des evenemnts a adapter
        adapter.notifyDataSetChanged();


        //////////// AJOUT DES EVENEMENTS //////////////

        // Données de départ
        String[] nomEvent = {"Anniversaire", "Jouer au ballon", "Aller chercher un chat", "Manger avec Toto", "Se promener dans les bois"};
        String[] dateEvent = {"202314", "2023119", "2023113", "202319", "2023023"};


        events_hashmap.put(dateEvent[0], new ArrayList() {{ add(nomEvent[0]); }});
        events_hashmap.put(dateEvent[1], new ArrayList() {{ add(nomEvent[1]); }});
        events_hashmap.put(dateEvent[2], new ArrayList() {{ add(nomEvent[2]); }});
        events_hashmap.put(dateEvent[3], new ArrayList() {{ add(nomEvent[3]); }});
        events_hashmap.put(dateEvent[4], new ArrayList() {{ add(nomEvent[4]); }});


        /////////// CLICK SUR UNE DATE //////////

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selected_date = Integer.toString(year)+Integer.toString(month)+Integer.toString(dayOfMonth);
            //Toast.makeText(CalendrierActivity.this, Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(dayOfMonth), Toast.LENGTH_SHORT).show();
            //Recuperation des données dans le hashmap
            events_list_string = events_hashmap.get(selected_date);  //récupère les éléments à la date sélectionnée
            if (events_list_string == null) {
                events_list_string = new ArrayList<>();
            }
            //mise à jour sur l'affichage
            adapter.clear();
            adapter.addAll(events_list_string);
            adapter.notifyDataSetChanged();
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_msg = text_view_add.getText().toString();
                if (event_msg.length() > 0) {
                    events_list_string = events_hashmap.get(selected_date);
                    if (events_list_string == null) {
                        events_list_string = new ArrayList<>();
                    }
                    events_list_string.add(event_msg);
                    //mettre à jour l'affichage
                    adapter.add(event_msg);
                    adapter.notifyDataSetChanged();
                    //mettre à jour le hashmap
                    events_hashmap.put(selected_date, events_list_string);
                    //supprimer l'insertion sur l'edit text
                    text_view_add.setText("");
                }
            }
        });

        // Modification et suppression des données du calendrier
        events_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'événement correspondant à l'élément sélectionné
                String evenement = adapter.getItem(position);
                String date = selected_date;

                // Ouvrir une boîte de dialogue pour modifier ou supprimer l'événement
                AlertDialog.Builder builder = new AlertDialog.Builder(CalendrierActivity.this);
                builder.setTitle("Supprimer l'événement ?");

               /* // Ajouter des boutons pour modifier ou supprimer l'événement
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });*/
                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Supprimer l'événement de la base de données et de la liste
                        //recupere la liste
                        events_list_string = events_hashmap.get(date);
                        //supprimer la valeur a supprimer
                        events_list_string.remove(evenement);
                        //supprime toute la liste dans hashmap
                        events_hashmap.remove(selected_date);
                        //ajoute a hashmap les vlaeur de list_even
                        events_hashmap.put(date, events_list_string);
                        //mettre a jour l'affichage
                        adapter.remove(evenement);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.show();
            }
        });



        // Bouton pour retourner à la page d'acueil
        Button buttonAccueil = findViewById(R.id.accueil_bouton);
        buttonAccueil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Réafficher la saisie des données au départ
                Intent i = new Intent(CalendrierActivity.this, MainActivity.class);
                startActivity(i);
            }

        });
    }
}