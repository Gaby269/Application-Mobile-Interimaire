package com.example.gpgh_interimaire;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentLoading extends Fragment {

    FirebaseFirestore db;

    static final String TAG = "LoginActivity";
    FirebaseAuth mAuth;

    // Constructeur
    public FragmentLoading() {}

    public static FragmentLoading newInstance(String text) {
        FragmentLoading fragment = new FragmentLoading();
        Bundle args = new Bundle();
        args.putString("text", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        TextView text_loading = view.findViewById(R.id.text_loading);

        // Récuperer le type de compte
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        fetchUserTypeCompte(userId);


        // Récupère le texte à partir des arguments et l'affiche dans le TextView
        Bundle args = getArguments();
        if (args != null) {
            String text = args.getString("text");
            text_loading.setText(text);
        }

        return view;
    }

    private String getUserId() {
        // A remplacer par votre fonction pour récupérer l'ID de l'utilisateur actuel
        return "123456";
    }


    private void fetchUserTypeCompte(String userId) {

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String typeCompte = documentSnapshot.getString("typeCompte");
                        Toast.makeText(getActivity(), typeCompte,Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), NavbarActivity.class);
                        i.putExtra("typeCompte", getTypeInString(typeCompte));
                        startActivity(i);
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                        Intent i = new Intent(getActivity(), ConnexionActivity.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));

    }

    private String getTypeInString(String s){
        if (s.contains("Entreprise")){
            Log.i(TAG, "Entreprise");
            return "Entreprise";
        }
        else if (s.contains("Candidat")){
            Log.i(TAG, "Candidat");
            return "Candidat";
        }
        else {
            Log.i(TAG, "Agence d'interim");
            return "Agence d'interim";
        }
    }

}