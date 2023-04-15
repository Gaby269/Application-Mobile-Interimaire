package com.example.gpgh_interimaire;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentLoading extends Fragment {    

    private TextView text_loading;
    
    // Constructeur
    public FragmentLoading() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        text_loading = view.findViewById(R.id.text_loading);

        return view;
    }

    public void setTextLoading(String text) {
        if (text_loading != null && isAdded()) {
            text_loading.setText(text);
        }
    }

}