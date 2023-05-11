package com.example.gpgh_interimaire;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoadingNavbarActivity extends AppCompatActivity {
    FirebaseFirestore db;
    static final String TAG = "LoadingNavbarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_navbar);

        // Récuperer le type de compte à partir de la bdd
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db = FirebaseFirestore.getInstance();
            fetchUserTypeCompte(currentUser.getUid());
        }
        else {
            Intent i = new Intent(LoadingNavbarActivity.this, NavbarActivity.class);
            i.putExtra("typeCompte", "invite");
            i.putExtra("fragment", "Offre");
            startActivity(i);
            finish();
        }
    }

    private void fetchUserTypeCompte(String userId) {

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String typeCompte = documentSnapshot.getString("typeCompte");
                        Intent i = new Intent(LoadingNavbarActivity.this, NavbarActivity.class);
                        i.putExtra("typeCompte", typeCompte);
                        i.putExtra("fragment", "Offre");
                        startActivity(i);
                        finish();
                    }
                    else {
                        Toast.makeText(LoadingNavbarActivity.this, "Les informations de ce compte ont été supprimées en BDD", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));

    }
}