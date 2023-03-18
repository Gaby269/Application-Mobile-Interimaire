package com.example.tp3_fragementsservices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class Fragment2 extends Fragment {

    String prenom, nom, anniversaire, telephone, mail;
    ArrayList<String> interests;
    String FILE_NAME = "data.json";

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        prenom = bundle.getString("prenom");
        nom = bundle.getString("nom");
        anniversaire = bundle.getString("anniversaire");
        telephone = bundle.getString("telephone");
        mail = bundle.getString("mail");
        interests = bundle.getStringArrayList("interests");
        boolean sync = bundle.getBoolean("sync");

        TextView nomCompletTextView = view.findViewById(R.id.nomComplet_textview);
        nomCompletTextView.setText("Nom complet : " + prenom + " " + nom);

        TextView anniversaireTextView = view.findViewById(R.id.anniversaire_textview);
        anniversaireTextView.setText("Date d'anniversaire : " + anniversaire);

        TextView telephoneTextView = view.findViewById(R.id.telephone_textview);
        telephoneTextView.setText("Numéro de téléphone : " + telephone);

        TextView mailTextView = view.findViewById(R.id.mail_textview);
        mailTextView.setText("Email : " + mail);

        TextView interetsTextView = view.findViewById(R.id.interets_textview);
        interetsTextView.setText("Centres d'intérêts : " + interests.toString());

        Button validerButton = view.findViewById(R.id.valider_button);
        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveData();
                    Toast.makeText(getActivity(), "Enregistrement des données !", Toast.LENGTH_SHORT).show();
                    downloadData();
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        return view;
    }

    //telechargement des données dans un fichier
    public void saveData() throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prenom", prenom);
        jsonObject.put("nom", nom);
        jsonObject.put("anniversaire", anniversaire);
        jsonObject.put("telephone", telephone);
        jsonObject.put("mail", mail);
        jsonObject.put("interests", interests);

        String userString = jsonObject.toString();
        System.out.println("Contenu du fichier JSON : " + userString);

        FileOutputStream fileOutputStream = null;
        fileOutputStream = getActivity().getApplicationContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fileOutputStream.write(userString.getBytes());
        fileOutputStream.close();
    }


    @SuppressLint("SetTextI18n")
    public void downloadData() throws IOException, JSONException{
        // Lecture du fichier JSON
        FileInputStream fileInputStream = getActivity().openFileInput(FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        fileInputStream.close();
        String jsonString = stringBuilder.toString();
        System.out.println("Contenu du fichier JSON dans le fichier "+ FILE_NAME + ": " + jsonString);

        JSONObject json = new JSONObject(jsonString);
        String prenomString = json.getString("prenom");
        String nomString = json.getString("nom");
        String anniversaireString = json.getString("anniversaire");
        String telephoneString = json.getString("telephone");
        String emailString = json.getString("mail");
        String interetsString = json.getString("interests");

        // Traiter le résultat ici
        Bundle bundle = new Bundle();
        bundle.putString("prenom", prenomString);
        bundle.putString("nom", nomString);
        bundle.putString("anniversaire", anniversaireString);
        bundle.putString("telephone", telephoneString);
        bundle.putString("mail", emailString);
        bundle.putString("interests", interetsString);

        // Creer le fragment 3 et ses arguments
        Fragment4 fragment4 = new Fragment4();
        fragment4.setArguments(bundle);

        // Aller au fragment 2
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment4)
                .addToBackStack(null)
                .commit();


    }
}
