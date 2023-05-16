package com.example.gpgh_interimaire;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderOffre extends RecyclerView.ViewHolder {

    ImageView imageView;
    ImageButton bouton_favori, bouton_modif, bouton_supp;
    TextView titreView, nameEntrepriseView, prixView, dateOffreView, typeView, codePostalView;

    TextView descriptionView;
    public MyViewHolderOffre(@NonNull View itemView) {
        super(itemView);
        titreView = itemView.findViewById(R.id.titre_offre);
        nameEntrepriseView = itemView.findViewById(R.id.entrepriseOffre);
        codePostalView = itemView.findViewById(R.id.codeOffre);
        dateOffreView = itemView.findViewById(R.id.dateOffre);
        typeView = itemView.findViewById(R.id.typeOffre);
        prixView = itemView.findViewById(R.id.prixOffre);
        
        bouton_favori = itemView.findViewById(R.id.btn_heart);
        bouton_modif = itemView.findViewById(R.id.btn_modif);
        bouton_supp = itemView.findViewById(R.id.btn_supp);
    }


}
