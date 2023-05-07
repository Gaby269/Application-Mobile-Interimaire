package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoadingNavbarActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    static final String TAG = "LoadingNavbarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_navbar);

        // Récuperer le type de compte à partir de la bdd
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        fetchUserTypeCompte(userId);
    }

    private void fetchUserTypeCompte(String userId) {

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Récupération du type de compte
                        String typeCompte = documentSnapshot.getString("typeCompte");
                        // Toast.makeText(LoadingNavbarActivity.this, typeCompte,Toast.LENGTH_SHORT).show();
                        // Intent pour transmettre le type de compte et le fragment vers lequel on va
                        Intent i = new Intent(LoadingNavbarActivity.this, NavbarActivity.class);
                        i.putExtra("typeCompte", typeCompte);
                        i.putExtra("fragment", "Offre");
                        startActivity(i);
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));

    }

    private String getUserId() {
        // A remplacer par votre fonction pour récupérer l'ID de l'utilisateur actuel
        return "123456";
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