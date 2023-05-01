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

    public MyAdapterOffre(Context context, List<ItemOffre> itemsOffres, List<ItemOffreDetails> itemsOffresDetails) {
        this.context = context;
        if (itemsOffres != null) {

            this.itemsOffres = itemsOffres;
        }
        if (itemsOffresDetails != null) {
            this.itemsOffresDetails = itemsOffresDetails;
        }
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderOffre holder, int position) {
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
                // passer la possiblité de garder ce quil est
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemsOffres.size();
    }

}
