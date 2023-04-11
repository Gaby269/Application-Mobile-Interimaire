package com.example.gpgh_interimaire;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragAboMensuel extends Fragment {

    // Constructeur
    public FragAboMensuel() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_abo_mensuel, container, false);

        return view;
    }



}
