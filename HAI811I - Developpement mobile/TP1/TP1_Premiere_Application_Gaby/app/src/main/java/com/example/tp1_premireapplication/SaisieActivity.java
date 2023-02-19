package com.example.tp1_premireapplication;

//import pour le xml
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import pour le java
/*
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Typeface;
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class SaisieActivity extends AppCompatActivity {

    /////////////////////
    // PARTIE POUR XML //
    /////////////////////
    @SuppressLint({"ResourceType", "SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie);

        // Valider avec un bouton et action lors d'un click
        Button buttonValider = findViewById(R.id.bouton_valider);
        buttonValider.setBackgroundColor(R.color.jaune_900);
        buttonValider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajout d'un petit onglet de confirmation pour pouvoir afficher nos coordonnées
                //On apel donc la fonction onConfirm pour éviter de surcharger le code
                onConfirm(v);   //appel a la fonction qui va dire ce qu'on va faire
            }
        });

        // Valider avec un bouton et action lors d'un click
        Button buttonAccueil = findViewById(R.id.bouton_accueil);
        buttonAccueil.setBackgroundColor(R.color.black);
        buttonAccueil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'un intent pour récuperer les informations
                Intent i = new Intent(SaisieActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        Button button_language = findViewById(R.id.bouton_langage);
        button_language.setBackgroundColor(R.color.black);
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

    protected void onConfirm(View v){

        // Recuperation des id de tous les edit text du fichier XML
        EditText prenom = findViewById(R.id.editTextPrenom);
        EditText nom = findViewById(R.id.editTextNom);
        EditText age = findViewById(R.id.editTextAge);
        EditText competence = findViewById(R.id.editTextCompetence);
        EditText tel = findViewById(R.id.editTextTelephone);

        // Verification des champs pour être sur qu'ils soient remplit
        if (prenom.getText().toString().length() <= 1 || nom.getText().toString().length() <= 1 || age.getText().toString().length() <= 1 || competence.getText().toString().length() <= 1 || tel.getText().toString().length() < 10){
            v.setBackgroundColor(ContextCompat.getColor(SaisieActivity.this, R.color.rose_500));
            Toast.makeText(SaisieActivity.this,R.string.avertissement,Toast.LENGTH_SHORT).show();
        }
        else{

            // Ajout d'une boite de dialogue pour les confirmations des données
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.verif_titre);   //titre de la boite
            alertDialogBuilder.setMessage(R.string.verif_msg);   //message de la boite
            alertDialogBuilder.setCancelable(false);              //permettre de ne pas annulé le dialogue

            // Pour la validation positive
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                //Réécriture de la fonction onClick pour la validation
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    // Création d'un intent pour récuperer les informations
                    Intent i = new Intent(SaisieActivity.this, AffichageActivity.class);

                    // Récuperation des ids
                    EditText editTextPrenom = findViewById(R.id.editTextPrenom);
                    EditText editTextNom = findViewById(R.id.editTextNom);
                    EditText editTextAge = findViewById(R.id.editTextAge);
                    EditText editTextCompetence = findViewById(R.id.editTextCompetence);
                    EditText editTextTelephone = findViewById(R.id.editTextTelephone);

                    // Ajouter les données à l'intent
                    i.putExtra("prenomAff", editTextPrenom.getText().toString());
                    i.putExtra("nomAff", editTextNom.getText().toString());
                    i.putExtra("ageAff", editTextAge.getText().toString());
                    i.putExtra("compAff", editTextCompetence.getText().toString());
                    i.putExtra("telAff", editTextTelephone.getText().toString());

                    // Lancer l'activité suivante
                    startActivity(i);
                }
            });

            // Pour la validation negative
            alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Montrer que on a annulé par un Toast
                    Toast.makeText(SaisieActivity.this,R.string.annulation,Toast.LENGTH_SHORT).show();

                    //Réafficher la saisie des données au départ
                    Intent i = new Intent(SaisieActivity.this, SaisieActivity.class);
                    startActivity(i);
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();                                         // afficher la boite
        }
    }





/*      ////////////////////
        // PARTIE EN JAVA //
        ////////////////////

    @SuppressLint({"ResourceType", "SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definir le layout
        LinearLayout l = new LinearLayout(this);     //c'est le layout de base
        l.setOrientation(LinearLayout.VERTICAL);            //on lui dit qu'il faut que ce soit en vertical
        l.setGravity(Gravity.CENTER);                       //on demande a ce que les élément soit au centre
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(850,150);       //positionner les editText en créant des parametres
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(300,100);       //positionner les editText en créant des parametres


        //Text view pour faire le début
        TextView monTextDebut = new TextView(this);
        monTextDebut.setText("Entrez vos coordonnées :");
        monTextDebut.setTextAppearance(android.R.style.TextAppearance_Large);   //mettre en plus gros
        monTextDebut.setTypeface(monTextDebut.getTypeface(), Typeface.BOLD);    //mettre en gras
        monTextDebut.setGravity(Gravity.CENTER);                                //mettre le texte au centre

        //Prénom pour le prénom
        EditText prenom = new EditText(this);
        prenom.setHint("Prénom :");
        prenom.setLayoutParams(params);     //donner les parametres

        //EditText pour le nom
        EditText nom = new EditText(this);
        nom.setHint("Nom :");
        nom.setLayoutParams(params);

        //EditText pour l'age
        EditText age = new EditText(this);
        age.setHint("Age :");
        age.setLayoutParams(params);

        //EditText pour le domaine de compétence
        EditText competence = new EditText(this);
        competence.setHint("Domaine de compétence :");
        competence.setLayoutParams(params);

        //EditTex pour un telephone
        EditText tel = new EditText(this);
        tel.setHint("Numéro de téléphone :");
        tel.setLayoutParams(params);

        //Valider avec un bouton
        Button buttonValider = new Button(this);
        buttonValider.setText("Valider");
        buttonValider.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.purple_500));    //changer la couleur de fond
        buttonValider.setTypeface(buttonValider.getTypeface(), Typeface.BOLD);    //mettre en gras
        buttonValider.setTextColor(Color.WHITE);    //mettre en gras
        buttonValider.setLayoutParams(params2);
        //Action lors d'un click
        buttonValider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.purple_700));
                String message = "        "+R.string.coordonne+"\n\n"+R.string.prenom+" : " + prenom.getText() + "\n"+R.string.nom+" : " + nom.getText() + "\n+R.string.age+ : " + age.getText() + "\n"+R.string.competence+" : " + competence.getText() + "\n"+R.string.numTel+" : " + tel.getText();
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            }
        });

        //Ajout au layout tous les éléments
        l.addView(monTextDebut);            //ajout du texte
        l.addView(new TextView(this), LinearLayout.LayoutParams.MATCH_PARENT, 100);  //ajout d'un espace
        l.addView(prenom);
        l.addView(new TextView(this), LinearLayout.LayoutParams.MATCH_PARENT, 10);
        l.addView(nom);
        l.addView(new TextView(this), LinearLayout.LayoutParams.MATCH_PARENT, 10);
        l.addView(age);
        l.addView(new TextView(this), LinearLayout.LayoutParams.MATCH_PARENT, 10);
        l.addView(competence);
        l.addView(new TextView(this), LinearLayout.LayoutParams.MATCH_PARENT, 10);
        l.addView(tel);
        l.addView(new TextView(this), LinearLayout.LayoutParams.MATCH_PARENT, 100);
        l.addView(buttonValider);

        //Montrer les vues
        setContentView(l);
    }
*/

}
