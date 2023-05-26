package com.example.gpgh_interimaire;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterOffre extends RecyclerView.Adapter<MyViewHolderOffre> {

    String TAG = "MyAdapterOffre";

    Context context;
    List<ItemOffre> itemsOffres;
    String typeCompte;

    String userId;

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

        if (itemsOffres != null) {
            String offreId = itemsOffres.get(position).getId_offre();

            holder.titreView.setText(itemsOffres.get(position).getTitre());
            holder.nameEntrepriseView.setText(itemsOffres.get(position).getNameEntreprise());
            holder.codePostalView.setText(itemsOffres.get(position).getCodePostal());
            holder.typeView.setText(itemsOffres.get(position).getType());
            holder.prixView.setText(itemsOffres.get(position).getPrix());

            String date_debut = itemsOffres.get(position).getDate_debut();
            String date_fin = itemsOffres.get(position).getDate_fin();
            String date_debut_fin = date_debut + " - " + date_fin;
            holder.dateOffreView.setText(date_debut_fin);

            // récupérer l'id de l'user s'il est connecté
            if (!typeCompte.equals("Invite")) {
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                getFavori(userId, offreId, holder);
            }

            
            // Ajouter le OnClickListener à itemView
            holder.itemView.setOnClickListener(v -> {
                // Créer une intention pour lancer l'ActivityDetailsOffre
            Intent intent = new Intent(context, AfficherDetailsOffreActivity.class);
            intent.putExtra("idOffre", itemsOffres.get(position).getId_offre());
            intent.putExtra("typeCompte", typeCompte);
            context.startActivity(intent);
            });
        
            // Bouton modification btn_modif
            holder.bouton_modif.setOnClickListener(v -> {
                Intent intent = new Intent(context, ModificationOffresActivity.class);
                intent.putExtra("typeCompte", typeCompte);
                intent.putExtra("titreOffre", itemsOffres.get(position).getTitre());
                intent.putExtra("idOffre", itemsOffres.get(position).getId_offre());
                intent.putExtra("is_details", "false");
                context.startActivity(intent);
            });
        
            if (typeCompte.equals("Candidat") || typeCompte.equals("Invite")) {
                holder.bouton_modif.setVisibility(View.GONE); // Visibilité du bouton modifier
            }
            else if (!typeCompte.equals("Candidat")) {
                holder.bouton_favori.setVisibility(View.GONE); // Visibilité du bouton favori
            }
            
            // Bouton favori
            holder.bouton_favori.setOnClickListener(v -> {
                setFavori(userId, offreId, holder);
            });
        }
        
    }
    @Override
    public int getItemCount() {
        return itemsOffres.size();
    }


    public void setFavori(String userId, String offreId, MyViewHolderOffre holder) {
        holder.bouton_favori.setEnabled(false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // on check d'abord si l'offre est déjà en favori ou pas
        db.collection("favoris")
            .whereEqualTo("userId", userId)
            .whereEqualTo("offreId", offreId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    // Si l'offre est dans les favoris, on la supprime
                    if (!task.getResult().isEmpty()) {
                        db.collection("favoris")
                            .whereEqualTo("userId", userId)
                            .whereEqualTo("offreId", offreId)
                            .get()
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    // Pour chaque document trouvé (il devrait y en avoir seulement un), supprimer le document
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        db.collection("favoris").document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(aVoid -> {
                                                holder.bouton_favori.setImageResource(R.drawable.icon_favori_white_vide);
                                            })
                                            .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la suppression de l'offre des favoris", e));
                                    }
                                } else {
                                    Log.d(TAG, "Erreur lors de la récupération des favoris", task.getException());
                                }
                            });
                    }

                    // Si l'offre n'est pas dans les favoris, on l'ajoute
                    else {
                        Map<String, Object> favori = new HashMap<>();
                        favori.put("userId", userId);
                        favori.put("offreId", offreId);
            
                        db.collection("favoris")
                            .add(favori)
                            .addOnSuccessListener(documentReference -> {
                                holder.bouton_favori.setImageResource(R.drawable.icon_favori_white);
                            })
                            .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout de l'offre aux favoris", e));
                    }


                }
                else {
                    // Erreur lors de la vérification
                    Log.d(TAG, "Erreur lors de la vérification des favoris : ", task.getException());
                }
            });

        holder.bouton_favori.setEnabled(true);
    }


    public void getFavori(String userId, String offreId, MyViewHolderOffre holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); 
        db.collection("favoris")
            .whereEqualTo("userId", userId)
            .whereEqualTo("offreId", offreId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        holder.bouton_favori.setImageResource(R.drawable.icon_favori_white);
                    }
                }
                else {
                    // Erreur lors de la vérification
                    Log.d(TAG, "Erreur lors de la vérification des favoris : ", task.getException());
                }
            });
    }
}
