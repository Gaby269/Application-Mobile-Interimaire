package com.example.myrecycleviewinterimaire;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titreView, nameEntrepriseView, petiteDescriptionView, rueView, complementRueView, codePostalView, parkingView, ticketView, teletravailView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        titreView = itemView.findViewById(R.id.titre_offre);
        imageView = itemView.findViewById(R.id.image_entreprise);
        nameEntrepriseView = itemView.findViewById(R.id.entrepriseOffre);
        petiteDescriptionView = itemView.findViewById(R.id.descriptionOffre);
        rueView = itemView.findViewById(R.id.adresseOffre);
        complementRueView = itemView.findViewById(R.id.complementOffre);
        codePostalView = itemView.findViewById(R.id.codeOffre);
        parkingView = itemView.findViewById(R.id.parking);
        ticketView = itemView.findViewById(R.id.ticket_resto);
        teletravailView = itemView.findViewById(R.id.teletravail);
    }
}
