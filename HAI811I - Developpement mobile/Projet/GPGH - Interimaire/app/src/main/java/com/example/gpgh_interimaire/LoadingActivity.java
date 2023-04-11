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

public class LoadingActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private ViewModel mViewModel;
    static final String TAG = "LoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Initialiser ViewModel
        mViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Récuperer le type de compte à partir de la bdd
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        fetchUserTypeCompte(userId, (MyViewModel) mViewModel);
    }

    private void fetchUserTypeCompte(String userId, MyViewModel mViewModel) {

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String typeCompte = documentSnapshot.getString("typeCompte");
                        mViewModel.setUserTypeCompte(typeCompte);
                        Toast.makeText(LoadingActivity.this, ""+typeCompte+"",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoadingActivity.this, NavbarActivity.class);
                        i.putExtra("typeCompte", ""+typeCompte+"");
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
}
