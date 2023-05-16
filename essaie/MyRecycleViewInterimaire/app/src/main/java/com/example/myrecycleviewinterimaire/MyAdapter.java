package com.example.myrecycleviewinterimaire;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Item> items;

    public MyAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titreView.setText(items.get(position).getTitre());
        holder.imageView.setImageResource(items.get(position).getImage());
        holder.nameEntrepriseView.setText(items.get(position).getNameEntreprise());
        holder.petiteDescriptionView.setText(items.get(position).getPetiteDescription());
        holder.rueView.setText(items.get(position).getRue());
        holder.complementRueView.setText(items.get(position).getComplementRue());
        holder.codePostalView.setText(items.get(position).getCodePostal());
        holder.parkingView.setText(""+items.get(position).getParking()+" places");
        if (items.get(position).isTicket()) {
            holder.ticketView.setText("Ticket Restaurant");
        }
        if (items.get(position).isTeletravail()) {
            holder.teletravailView.setText("Télétravail possible");
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
