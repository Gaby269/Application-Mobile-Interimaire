package com.example.gpgh_interimaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragLoading extends Fragment {

    private TextView text_loading;
    
    // Constructeur
    public FragLoading() {}

    public static FragLoading newInstance(String text) {
        FragLoading fragment = new FragLoading();
        Bundle args = new Bundle();
        args.putString("text", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_loading, container, false);

        text_loading = view.findViewById(R.id.text_loading);

        // Récupère le texte à partir des arguments et l'affiche dans le TextView
        Bundle args = getArguments();
        if (args != null) {
            String text = args.getString("text");
            text_loading.setText(text);
        }

        return view;
    }

}