package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmationTelephoneActivity extends AppCompatActivity {
    private static final String TAG = "ConfirmationTelephoneActivity";

    FirebaseFirestore db;
    String userId;
    TextView usernameTextView;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_telephone);
        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");

        if (userId != null) {
            fetchUserInfo(userId);
        }


        Button inscriptionButton = findViewById(R.id.boutton_confirmationTel);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmationTelephoneActivity.this, EntrepriseActivity.class);
                startActivity(i);
            }
        });
    }

    private void fetchUserInfo(String userId) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("prenom");
                        String lastName = documentSnapshot.getString("nom");

                        usernameTextView = findViewById(R.id.UsernameTextView);
                        usernameTextView.setText("Bonjour " + firstName + " " + lastName);

                    }
                    else {
                        Log.w(TAG, "No document found for user");
                        usernameTextView.setText("Bonjour");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


}