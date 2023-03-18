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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
        prenom = bundle.getString("prenom");
        nom = bundle.getString("nom");
        anniversaire = bundle.getString("anniversaire");
        telephone = bundle.getString("telephone");
        mail = bundle.getString("mail");
        interests = bundle.getStringArrayList("interets");
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(getActivity(), "Validation des données !", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


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
}
