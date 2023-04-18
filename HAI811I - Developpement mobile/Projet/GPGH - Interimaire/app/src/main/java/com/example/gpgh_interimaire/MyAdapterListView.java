package com.example.gpgh_interimaire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapterListView extends BaseAdapter {

    private List<class_offre> mDataList; // la liste des données
    private FragPageOffres mFragment;


    public MyAdapterListView(Fragment fragment, List<class_offre> dataList) {
        mFragment = (FragPageOffres) fragment;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // récupérer l'élément de données correspondant à la position
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mFragment.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_offre, null);
        }

        // remplir la vue avec les données de l'élément
        TextView titre = convertView.findViewById(R.id.titre_offre);
        TextView nomEntreprise = convertView.findViewById(R.id.entrepriseOffre);
        ImageView imageEntreprise = convertView.findViewById(R.id.image_entreprise);
        TextView descriptionOffre = convertView.findViewById(R.id.descriptionOffre);
        TextView adresseOffre = convertView.findViewById(R.id.adresseOffre);
        TextView complementOffre = convertView.findViewById(R.id.complementOffre);
        TextView codeOffre = convertView.findViewById(R.id.codeOffre);

/*
        titre.setText(data.getTitle());
        nomEntreprise.setText(data.getNomEntreprise());
        descriptionOffre.setText(data.getPetiteDescription());
        adresseOffre.setText(data.getRueAdresse());
        complementOffre.setText(data.getComplementAdresse());
        codeOffre.setText(data.getCodePostale());
*/

        return convertView;
    }
}


