package com.example.tp3_fragementsservices;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment2 extends Fragment {

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        // Get user data from arguments
        Bundle bundle = getArguments();
        String prenom = bundle.getString("prenom");
        String nom = bundle.getString("nom");
        String anniversaire = bundle.getString("anniversaire");
        String telephone = bundle.getString("telephone");
        String mail = bundle.getString("mail");
        ArrayList<String> interests = bundle.getStringArrayList("interets");
        boolean sync = bundle.getBoolean("sync");

        // Set user data in UI
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
                Toast.makeText(getActivity(), "Validation des données !", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
