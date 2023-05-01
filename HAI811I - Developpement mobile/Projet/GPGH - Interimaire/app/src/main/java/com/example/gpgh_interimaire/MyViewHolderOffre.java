package com.example.gpgh_interimaire;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderOffre extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titreView, nameEntrepriseView, prixView, typeView, rueView, complementRueView, codePostalView, parkingView, ticketView, teletravailView;

    TextView descriptionView;
    public MyViewHolderOffre(@NonNull View itemView) {
        super(itemView);
        titreView = itemView.findViewById(R.id.titre_offre);
        imageView = itemView.findViewById(R.id.image_entreprise);
        nameEntrepriseView = itemView.findViewById(R.id.entrepriseOffre);
        prixView = itemView.findViewById(R.id.prixOffre);
        typeView = itemView.findViewById(R.id.typeOffre);
        descriptionView = itemView.findViewById(R.id.descriptionOffre);
        rueView = itemView.findViewById(R.id.adresseOffre);
        complementRueView = itemView.findViewById(R.id.complementOffre);
        codePostalView = itemView.findViewById(R.id.codeOffre);
        parkingView = itemView.findViewById(R.id.parking);
        ticketView = itemView.findViewById(R.id.ticket_resto);
        teletravailView = itemView.findViewById(R.id.teletravail);

    }


}
