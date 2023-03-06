package com.example.tp2_lescapteurs;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class MainActivity2 extends AppCompatActivity  {
    private ListView mListView;
    private CustomListAdapter mAdapter;
    private ArrayList<String> mItems;
    private ArrayList<Integer> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mListView = findViewById(R.id.listViewText);
        mItems = new ArrayList<>();
        mImages = new ArrayList<>();
        // Ajouter des données dans la liste
        mImages.add(R.drawable.capteur_ok);
        mItems.add("Capteurs disponibles");
        mImages.add(R.drawable.capteur_pas);
        mItems.add("Capteurs indisponibles");
        mImages.add(R.drawable.fond);
        mItems.add("Accelerometre");
        mImages.add(R.drawable.direction);
        mItems.add("Direction");
        mImages.add(R.drawable.flash);
        mItems.add("Flash");
        mImages.add(R.drawable.prox);
        mItems.add("Proximité");
        mImages.add(R.drawable.geo);
        mItems.add("Geolocalisation");
        // Créer l'adaptateur
        mAdapter = new CustomListAdapter(this, mItems, mImages);
        // Associer l'adaptateur à la ListView
        mListView.setAdapter(mAdapter);
        // Gérer les clics sur les éléments de la liste
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Sélectionner l'activité à ouvrir en fonction de la position de l'élément cliqué
                switch (position) {
                    case 0:
                        // Ouvrir l'activité pour l'élément 0
                        startActivity(new Intent(MainActivity2.this, Ex1Activity.class));
                        break;
                    case 1:
                        // Ouvrir l'activité pour l'élément 1
                        startActivity(new Intent(MainActivity2.this, Ex2Activity.class));
                        break;
                    case 2:
                        // Ouvrir l'activité pour l'élément 2
                        startActivity(new Intent(MainActivity2.this, Ex3Activity.class));
                        break;
                    case 3:
                        // Ouvrir l'activité pour l'élément 2
                        startActivity(new Intent(MainActivity2.this, Ex4Activity.class));
                        break;
                    case 4:
                        // Ouvrir l'activité pour l'élément 2
                        startActivity(new Intent(MainActivity2.this, Ex5Activity.class));
                        break;
                    case 5:
                        // Ouvrir l'activité pour l'élément 2
                        startActivity(new Intent(MainActivity2.this, Ex6Activity.class));
                        break;
                    case 6:
                        // Ouvrir l'activité pour l'élément 2
                        startActivity(new Intent(MainActivity2.this, Ex7Activity.class));
                        break;
                    // Ajouter d'autres cas ici pour les autres éléments de la liste
                    default:
                        break;
                }
            }
        });
    }
/*
        //boutons pour lancer les 7 activités
        bEx1 = findViewById(R.id.button1);
        bEx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity2.this, Ex1Activity.class);
                startActivity(iCal);
            }
        });
        bEx2 = findViewById(R.id.button2);
        bEx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity2.this, Ex2Activity.class);
                startActivity(iCal);
            }
        });
        bEx3 = findViewById(R.id.button3);
        bEx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity2.this, Ex3Activity.class);
                startActivity(iCal);
            }
        });
        bEx4 = findViewById(R.id.button4);
        bEx4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity2.this, Ex4Activity.class);
                startActivity(iCal);
            }
        });
        bEx5 = findViewById(R.id.button5);
        bEx5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity2.this, Ex5Activity.class);
                startActivity(iCal);
            }
        });
        bEx6 = findViewById(R.id.button6);
        bEx6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity2.this, Ex6Activity.class);
                startActivity(iCal);
            }
        });
        bEx7 = findViewById(R.id.button7);
        bEx7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent iCal = new Intent(MainActivity2.this, Ex7Activity.class);
                startActivity(iCal);
            }
        });
 */
}