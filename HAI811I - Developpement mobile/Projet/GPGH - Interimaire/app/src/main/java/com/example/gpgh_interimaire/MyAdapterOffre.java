package com.example.gpgh_interimaire;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterOffre extends RecyclerView.Adapter<MyViewHolderOffre> {

    Context context;
    List<ItemOffre> itemsOffres;
    List<ItemOffreDetails> itemsOffresDetails;
    String typeCompte;
    boolean is_favori = false;

    public MyAdapterOffre(Context context, List<ItemOffre> itemsOffres, String typeCompte) {
        this.context = context;
        this.itemsOffres = itemsOffres;
        this.typeCompte = typeCompte;
    }

    @NonNull
    @Override
    public MyViewHolderOffre onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (itemsOffres != null) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_offre_view, parent, false);
            return new MyViewHolderOffre(itemView);
        }
        else{
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_offre_details_view, parent, false);
            return new MyViewHolderOffre(itemView);
        }
    }

    @SuppressLint({"SetTextI18n", "RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderOffre holder,  int position) {
        // Si c'est l'offre dans le deffilement
        if (itemsOffres != null) {
            holder.titreView.setText(itemsOffres.get(position).getTitre());
            holder.imageView.setImageResource(itemsOffres.get(position).getImage());
            holder.nameEntrepriseView.setText(itemsOffres.get(position).getNameEntreprise());
            holder.prixView.setText(itemsOffres.get(position).getPrix());
            holder.typeView.setText(itemsOffres.get(position).getType());
            holder.rueView.setText(itemsOffres.get(position).getRue());
            holder.complementRueView.setText(itemsOffres.get(position).getComplementRue());
            holder.codePostalView.setText(itemsOffres.get(position).getCodePostal());
        }
        // Sic'est l'offre en details
        else {
            holder.titreView.setText(itemsOffresDetails.get(position).getTitre());
            holder.imageView.setImageResource(itemsOffresDetails.get(position).getImage());
            holder.nameEntrepriseView.setText(itemsOffresDetails.get(position).getNameEntreprise());
            holder.prixView.setText(itemsOffresDetails.get(position).getPrix());
            holder.descriptionView.setText(itemsOffresDetails.get(position).getDescriptionOffre());
            holder.typeView.setText(itemsOffresDetails.get(position).getType());
            holder.rueView.setText(itemsOffresDetails.get(position).getRue());
            holder.complementRueView.setText(itemsOffresDetails.get(position).getComplementRue());
            holder.codePostalView.setText(itemsOffresDetails.get(position).getCodePostal());
            holder.parkingView.setText("" + itemsOffresDetails.get(position).getParking() + " places");
            if (itemsOffresDetails.get(position).isTicket()) {
                holder.ticketView.setText("Ticket Restaurant");
            }
            if (itemsOffresDetails.get(position).isTeletravail()) {
                holder.teletravailView.setText("Télétravail possible");
            }
        }
        // Ajouter le OnClickListener à itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour lancer l'Activity2
                Intent intent = new Intent(context, AfficherDetailsOffreActivity.class);
                intent.putExtra("titreOffre", itemsOffres.get(position).getTitre());
                intent.putExtra("typeCompte", typeCompte);
                context.startActivity(intent);
            }
        });
        // Bouton favorie
        holder.bouton_favori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_favori){
                    is_favori = false;
                    holder.bouton_favori.setImageResource(R.drawable.icon_favori_white_vide);
                }
                else{
                    is_favori = true;
                    holder.bouton_favori.setImageResource(R.drawable.icon_favori_white);
                }

            }
        });
        // Bouton candidature
        holder.bouton_candidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour lancer l'Activity2
                Intent intent;
                if (typeCompte.equals("Candidat")) {
                    intent = new Intent(context, PostulerActivity.class);
                    intent.putExtra("is_details", "false");
                }
                else{
                    intent = new Intent(context, CandidaturesOffreActivity.class);
                }
                intent.putExtra("typeCompte", typeCompte);
                intent.putExtra("titreOffre", itemsOffres.get(position).getTitre());
                intent.putExtra("description", itemsOffres.get(position).getDescriptionOffre());
                context.startActivity(intent);
            }
        });
        if (typeCompte.equals("Candidat")) {
            holder.bouton_candidater.setText("Postuler");
        }
        else {
            holder.bouton_candidater.setText("Voir les candidatures");
        }

    }
    @Override
    public int getItemCount() {
        return itemsOffres.size();
    }

}
