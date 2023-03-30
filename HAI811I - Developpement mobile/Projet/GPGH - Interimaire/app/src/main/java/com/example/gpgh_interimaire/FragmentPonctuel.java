package com.example.gpgh_interimaire;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentPonctuel extends Fragment {

    // Constructeur
    public FragmentPonctuel() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint({"MissingInflatedId", "ResourceType"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.fragment_ponctuel, container, false);

        return view;
    }



}
