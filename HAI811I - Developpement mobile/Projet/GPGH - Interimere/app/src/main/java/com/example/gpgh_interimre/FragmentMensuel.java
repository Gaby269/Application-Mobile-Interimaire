package com.example.gpgh_interimre;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentMensuel extends Fragment {

    // Constructeur
    public FragmentMensuel() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.fragment_mensuel, container, false);

        return view;
    }



}
