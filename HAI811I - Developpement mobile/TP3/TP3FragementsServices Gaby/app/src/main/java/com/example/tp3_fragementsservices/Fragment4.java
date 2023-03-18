package com.example.tp3_fragementsservices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;

public class Fragment4 extends Fragment {

    public Fragment4() {
        // Required empty public constructor
    }

    @Override
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_4, container, false);

        // Get user data from arguments
        Bundle bundle = getArguments();
        assert bundle != null;
        String prenomString = bundle.getString("prenom");
        String nomString = bundle.getString("nom");
        String anniversaireString = bundle.getString("anniversaire");
        String telephoneString = bundle.getString("telephone");
        String emailString = bundle.getString("mail");
        String interetsString = bundle.getString("interests");

        // afficher les données dans les TextView correspondants
        TextView nameTextView = view.findViewById(R.id.nomComplet);
        nameTextView.setText("Nom complet : " + prenomString + nomString);
        TextView annivTextView = view.findViewById(R.id.anniversaire);
        annivTextView.setText("Date d'anniversaire : " +anniversaireString);
        TextView telephoneTextView = view.findViewById(R.id.telephone);
        telephoneTextView.setText("Numéro de téléphone : " + telephoneString);
        TextView emailTextView = view.findViewById(R.id.mail);
        emailTextView.setText("Email : " + emailString);
        TextView interetsTextView = view.findViewById(R.id.interets);
        interetsTextView.setText("Centres d'intérêts : " + interetsString);

        return view;
    }
}