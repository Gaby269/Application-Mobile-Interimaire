package com.example.gpgh_interimaire;


import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderCandidature extends RecyclerView.ViewHolder {

    TextView cvView, nameView, descriptionCandidatureView, complementCandidatureView;
    ImageButton bouton_favori;

    public MyViewHolderCandidature(@NonNull View itemView) {
        super(itemView);
        cvView = itemView.findViewById(R.id.cvCandidature);
        nameView = itemView.findViewById(R.id.name_Candidature);
        descriptionCandidatureView = itemView.findViewById(R.id.descriptionCandidature);
        complementCandidatureView = itemView.findViewById(R.id.complementCandidature);
        bouton_favori = itemView.findViewById(R.id.btn_heart);

    }


}
