package com.example.myrecycleviewdemo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class MyViewHolder extends ViewHolder {

    ImageView imageEntreprise;
    TextView titreView,entrepriseOffreView, descriptionOffreView, adresseOffreView, complementAdresseOffreView, codeOffreView, parkingView, ticket_restoView, teleTravailView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        titreView = itemView.findViewById(R.id.titre_offre);
        entrepriseOffreView = itemView.findViewById(R.id.entrepriseOffre);
        imageEntreprise = itemView.findViewById(R.id.image_entreprise);
        descriptionOffreView = itemView.findViewById(R.id.descriptionOffre);
        adresseOffreView = itemView.findViewById(R.id.adresseOffre);
        complementAdresseOffreView = itemView.findViewById(R.id.complementOffre);
        codeOffreView = itemView.findViewById(R.id.codeOffre);
        parkingView = itemView.findViewById(R.id.parking);
        ticket_restoView = itemView.findViewById(R.id.ticket_resto);
        teleTravailView = itemView.findViewById(R.id.teletravail);
    }
}
