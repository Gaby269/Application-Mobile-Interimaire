package com.example.gpgh_interimaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private static final String TAG = "ConfirmationTelephoneActivity";

    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseFirestore db;
    TextView usernameTextView;
    EditText verificationCodeEditText;
    String userId, firstName, lastName, phoneNumber, verificationCode, typeCompte;
    FragmentTransaction transaction;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_telephone);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        verificationCodeEditText = findViewById(R.id.codeTelEditText);
        verificationCode = verificationCodeEditText.getText().toString();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        if (userId != null) {
            fetchUserInfo(userId);
        }

        // on skip tant que ça marche pas
        //initCallbacks();
        //sendVerificationCode(phoneNumber);

        Button inscriptionButton = findViewById(R.id.boutton_confirmationTel);
        inscriptionButton.setOnClickListener(view -> {
            displayloadingScreen();
            redirectUserIfSuccessful();

            /*
            String verificationCode1 = verificationCodeEditText.getText().toString().trim();
            String verificationId = ""; // L'ID de vérification reçu dans la méthode onCodeSent

            if (!verificationCode1.isEmpty()) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode1);
                signInWithPhoneAuthCredential(credential);
            }
            else {
                verificationCodeEditText.setError(getString(R.string.code_incorrect));
            }
             */
        });
    }

    private void fetchUserInfo(String userId) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        firstName = documentSnapshot.getString("prenom");
                        lastName = documentSnapshot.getString("nom");
                        phoneNumber = documentSnapshot.getString("telephone");
                        typeCompte = documentSnapshot.getString("typeCompte");

                        usernameTextView = findViewById(R.id.UsernameTextView);
                        usernameTextView.setText("Bonjour " + firstName + " " + lastName);

                    } else {
                        Log.w(TAG, "DB Non trouvée");
                        usernameTextView.setText("Bonjour");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


    private void initCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent: " + verificationId);
            }
        };
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        redirectUserIfSuccessful();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Log.w(TAG, "Invalid verification code");
                        }
                    }
                });
    }

    private void redirectUserIfSuccessful() {
        //afiche un toast avec le type de compte
        // Toast.makeText(ConfirmationTelephoneActivity.this, "Vous êtes connecté en tant que " + typeCompte, Toast.LENGTH_SHORT).show();
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
        FragmentLoading loadingFragment = FragmentLoading.newInstance("Validation du numéro...");
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loadingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void dismissLoadingScreen() {
        getSupportFragmentManager().popBackStack();
    }


}