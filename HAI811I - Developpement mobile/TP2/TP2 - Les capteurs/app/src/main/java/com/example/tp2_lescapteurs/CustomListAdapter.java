package com.example.tp2_lescapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final ArrayList<String> mItems;
    private final ArrayList<Integer> mImages;
    @SuppressLint("ResourceType")
    // Constructeur
    public CustomListAdapter(Context context, ArrayList<String> items, ArrayList<Integer> images) {
        super(context, R.layout.list_item, items);
        mContext = context;
        mItems = items;
        mImages = images;
    }
    @NonNull
    @Override
    // View qu'on va voir sur le fichier
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"ViewHolder", "ResourceType"}) View rowView = inflater.inflate(R.layout.list_item, parent, false);
        ImageView imageView = rowView.findViewById(R.id.image_view);
        TextView textView = rowView.findViewById(R.id.text_view);
        imageView.setImageResource(mImages.get(position));
        textView.setText(mItems.get(position));
        return rowView;
    }
}
