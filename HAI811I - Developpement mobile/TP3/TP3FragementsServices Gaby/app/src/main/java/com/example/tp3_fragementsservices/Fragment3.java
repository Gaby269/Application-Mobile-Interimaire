package com.example.tp3_fragementsservices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Fragment3 extends Fragment {

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        // Get user data from arguments
        Bundle bundle = getArguments();
        assert bundle != null;
        String resultat = bundle.getString("fichierJAVA");

        // Set user data in UI
        TextView nomCompletTextView = view.findViewById(R.id.resultat_textView);
        nomCompletTextView.setText("Informations du site : \n"+resultat);

        return view;
    }
}