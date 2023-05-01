package com.example.myrecycleviewdemo;

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
        holder.imageEntreprise.setImageResource(items.get(position).getImage());
        holder.entrepriseOffreView.setText(items.get(position).getEntreprise());
        holder.descriptionOffreView.setText(items.get(position).getPetiteDescription());
        holder.adresseOffreView.setText(items.get(position).getRue());
        holder.complementAdresseOffreView.setText(items.get(position).getComplementRue());
        holder.codeOffreView.setText(items.get(position).getCodePostal());
        holder.parkingView.setText(""+items.get(position).getParking()+ " places");
        if (items.get(position).getTicket()){
            holder.ticket_restoView.setText("Ticket restau okkk");
        }
        if (items.get(position).getTeletravail()){
            holder.teleTravailView.setText("Télétravail2");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
