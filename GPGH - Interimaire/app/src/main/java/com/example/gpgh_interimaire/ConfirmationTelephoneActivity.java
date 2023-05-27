package com.example.gpgh_interimaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class ConfirmationTelephoneActivity extends AppCompatActivity {
    
    String TAG = "ConfirmationTelephoneActivity";
    FragmentTransaction transaction;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseFirestore db;
    
    EditText codeTelEditText;
    String userId, phoneNumber, typeCompte;
    String randomCode = "0000";

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_telephone);

        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        // on récupère le numéro de téléphone
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        typeCompte = intent.getStringExtra("typeCompte");

        codeTelEditText = findViewById(R.id.codeTelEditText);

        TextView confirmationButton = findViewById(R.id.codeNonRecuTextView);
        confirmationButton.setOnClickListener(view -> {
            randomCode = String.valueOf((int) (Math.random() * (9999 - 1000 + 1) + 1000));
            sendVerificationCode(phoneNumber, randomCode);
        });

        Button inscriptionButton = findViewById(R.id.boutton_confirmationTel);
        inscriptionButton.setOnClickListener(view -> {
            checkIfNumberIsValid();
        });
    }


    private void sendVerificationCode(String phoneNumber, String randomCode) {
        // ouvre l'application de SMS avec écrit le nombre aléatoire
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", "Code : " + randomCode);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } 
        else {
            Toast.makeText(this, "Application de messagerie non disponible", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkIfNumberIsValid() {

        String codeTel = codeTelEditText.getText().toString();

        if (codeTel.isEmpty()) {
            codeTelEditText.setError(getString(R.string.code_vide));
            codeTelEditText.requestFocus();
            dismissLoadingScreen();
            return;
        }
        else if (!codeTel.equals(randomCode)) {
            codeTelEditText.setError(getString(R.string.code_erreur));
            codeTelEditText.requestFocus();
            dismissLoadingScreen();
            return;
        }
        else {
            displayloadingScreen();
            redirectUserIfSuccessful();
        }
    }


    private void redirectUserIfSuccessful() {
        if (typeCompte.equals("Candidat")) {
            // mettre à jour signup_step dans la BDD 
            db.collection("users")
                    .document(userId)
                    .update("signup_step", "10")
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "signup_step mis à jour !"))
                    .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la MaJ de signup_step", e));

            dismissLoadingScreen();
            Intent i = new Intent(ConfirmationTelephoneActivity.this, LoadingNavbarActivity.class);
            startActivity(i);
        }
        else {
            db.collection("users")
                    .document(userId)
                    .update("signup_step", "2")
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "signup_step mis à jour !"))
                    .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la MaJ de signup_step", e));

            dismissLoadingScreen();
            Intent i = new Intent(ConfirmationTelephoneActivity.this, EntrepriseActivity.class);
            startActivity(i);
        }
    }


    public void displayloadingScreen() {
        FragLoading loadingFragment = FragLoading.newInstance("Validation du numéro...");
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loadingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void dismissLoadingScreen() {
        getSupportFragmentManager().popBackStack();
    }


}