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

public class MyAdapterCandidature extends RecyclerView.Adapter<MyViewHolderCandidature> {

    Context context;
    List<ItemCandidature> itemsCandidature;
    boolean is_favori = false;
    String typeCompte;

    public MyAdapterCandidature(Context context, List<ItemCandidature> itemsCandidature, String typeCompte) {
        this.context = context;
        this.itemsCandidature = itemsCandidature;
        this.typeCompte = typeCompte;
    }

    @NonNull
    @Override
    public MyViewHolderCandidature onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_candidature_view, parent, false);
        return new MyViewHolderCandidature(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCandidature holder, int position) {
        // Si c'est l'offre dans le deffilement
        holder.cvView.setText(itemsCandidature.get(position).getCV());
        holder.nameView.setText(itemsCandidature.get(position).getFirstName()+" "+itemsCandidature.get(position).getLastName());
        holder.descriptionCandidatureView.setText(itemsCandidature.get(position).getDescriptionCandidature());
        holder.complementCandidatureView.setText(itemsCandidature.get(position).getComplementCandidature());

        // Ajouter le OnClickListener à itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour lancer l'Activity2
                Intent intent = new Intent(context, AfficherDetailsCandidatureActivity.class);
                intent.putExtra("titreCandidature", holder.nameView.getText());
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

        // Visibilité du bouton favorie des offres
        if (typeCompte.equals("Candidat")){
            holder.bouton_favori.setVisibility(View.GONE);
        }
        else{
            holder.bouton_favori.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return itemsCandidature.size();
    }

}
