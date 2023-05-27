package com.example.gpgh_interimaire;


import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderCandidature extends RecyclerView.ViewHolder {

    TextView cvView, nameView, descriptionCandidatureView, etatCandidature;
    ImageButton bouton_favori, bouton_modif, bouton_supp;

    public MyViewHolderCandidature(@NonNull View itemView) {
        super(itemView);
        cvView = itemView.findViewById(R.id.cvCandidature);
        nameView = itemView.findViewById(R.id.name_Candidature);
        descriptionCandidatureView = itemView.findViewById(R.id.descriptionCandidature);
        bouton_favori = itemView.findViewById(R.id.btn_heart);
        bouton_modif = itemView.findViewById(R.id.btn_modif);
        bouton_supp = itemView.findViewById(R.id.btn_supp);
        etatCandidature = itemView.findViewById(R.id.etatCandidature);
    }


}
