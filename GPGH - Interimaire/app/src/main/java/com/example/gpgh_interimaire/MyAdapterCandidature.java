package com.example.gpgh_interimaire;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        holder.etatCandidature.setText(itemsCandidature.get(position).getEtat());

        // Ajouter le OnClickListener à itemView
        holder.itemView.setOnClickListener(v -> {
            // Créer une intention pour lancer l'Activity2
            Intent intent = new Intent(context, AfficherDetailsCandidatureActivity.class);
            intent.putExtra("titreCandidature", holder.nameView.getText());
            intent.putExtra("typeCompte", typeCompte);
            context.startActivity(intent);
        });

        // Bouton favori
        holder.bouton_favori.setOnClickListener(v -> {
            if (is_favori) {
                is_favori = false;
                holder.bouton_favori.setImageResource(R.drawable.icon_favori_white_vide);
            }
            else {
                is_favori = true;
                holder.bouton_favori.setImageResource(R.drawable.icon_favori_white);
            }

        });

        holder.bouton_supp.setOnClickListener(v -> {
            Toast.makeText(context, R.string.offreSupp, Toast.LENGTH_SHORT).show();
        });

        // Bouton modification btn_modif
        // holder.bouton_modif.setOnClickListener(v -> {
        //     Intent intent = new Intent(context, ModificationCandidatureActivity.class);
        //     intent.putExtra("typeCompte", typeCompte);
        //     intent.putExtra("titreOffre", holder.nameView.getText());
        //     intent.putExtra("is_details", "false");
        //     context.startActivity(intent);
        // });

        if (itemsCandidature.get(position).getId_candidature() == "0") {
            holder.bouton_favori.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        }

        holder.bouton_modif.setVisibility(View.GONE);

        if (!typeCompte.equals("Candidat")) {
            holder.bouton_supp.setVisibility(View.GONE); // Visibilité du bouton suppresion des candidatures
        }

        if (typeCompte.equals("Candidat") || typeCompte.equals("Admin")){
            holder.bouton_favori.setVisibility(View.GONE); // Visibilité du bouton favori des candidatures
        }
    }

    @Override
    public int getItemCount() {
        return itemsCandidature.size();
    }

}
