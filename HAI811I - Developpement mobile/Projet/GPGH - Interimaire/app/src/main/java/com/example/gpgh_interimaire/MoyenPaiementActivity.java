package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoyenPaiementActivity extends AppCompatActivity {

    static final String TAG = "PaiementActivity";
    String PaypalLink = "https://www.sandbox.paypal.com/checkoutnow?token=7SK00420K5251910F";
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FragmentTransaction transaction;

    CheckBox saveCardCheckbox;
    EditText nomEditText, carteEditText, dateEditText, codeEditText;
    String userId, nom, carte, date, code;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moyen_paiement);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        nomEditText = findViewById(R.id.nomEditText);
        carteEditText = findViewById(R.id.carteEditText);
        dateEditText = findViewById(R.id.dateEditText);
        codeEditText = findViewById(R.id.codeEditText);
        saveCardCheckbox = findViewById(R.id.sliderButton);

        Button inscriptionButton = findViewById(R.id.boutton_payer);
        inscriptionButton.setOnClickListener(view -> {
            displayloadingScreen();
            addPaymentToFirestore();
        });

        Button TMPButton = findViewById(R.id.boutton_tmp);
        TMPButton.setOnClickListener(view -> {
            displayloadingScreen();
            nomEditText.setText("TARTAMPION");
            carteEditText.setText("4111111111111111");
            dateEditText.setText("05/25");
            codeEditText.setText("123");
            addPaymentToFirestore();
        });

        ImageView imageView = findViewById(R.id.imagePaypal);
        imageView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PaypalLink));
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(MoyenPaiementActivity.this, "Paypal webpage not found", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addPaymentToFirestore() {

        if (validateInput()) {

            if (saveCardCheckbox.isChecked()) {
                Map<String, Object> payment = new HashMap<>();
                payment.put("nom", nom);
                payment.put("carte", carte);
                payment.put("date", date);
                payment.put("code", code);
                
                db.collection("paiements")
                    .document(userId)
                    .set(payment)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Carte Bancaire ajoutée à la BDD"))
                    .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout dans la carte", e));
            }

            nomEditText.setError(null);
            carteEditText.setError(null);
            dateEditText.setError(null);
            codeEditText.setError(null);
            dismissLoadingScreen();
            Intent i = new Intent(MoyenPaiementActivity.this, TutoActivity.class);
            startActivity(i);
        }
    }


    private boolean validateInput() {
        nom = nomEditText.getText().toString();
        carte = carteEditText.getText().toString();
        date = dateEditText.getText().toString();
        code = codeEditText.getText().toString();

        boolean check = true;
        if (nom.isEmpty()) {
            nomEditText.setError(getString(R.string.erreur_nom));
            check = false;
        }
        if (carte.isEmpty()) {
            carteEditText.setError(getString(R.string.erreur_vide));
            check = false;
        }
        else if (!isCardNumberValid(carte)) {
            carteEditText.setError(getString(R.string.erreur_carte));
            check = false;
        }
        if (date.isEmpty()) {
            dateEditText.setError(getString(R.string.erreur_date));
            check = false;
        }
        else if (!isExpirationDateValid(date)) {
            dateEditText.setError(getString(R.string.erreur_date));
            check = false;
        }
        if (code.isEmpty()) {
            codeEditText.setError(getString(R.string.erreur_code));
            check = false;
        }
        else if (!isCvvValid(code)) {
            codeEditText.setError(getString(R.string.erreur_code));
            check = false;
        }

        return check;
    }

    private static boolean isCardNumberValid(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static boolean isExpirationDateValid(String expDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
        Date currentDate = new Date();
        Date expirationDate;
        try {expirationDate = dateFormat.parse(expDate);} 
        catch (ParseException e) {return false;}
        return currentDate.compareTo(expirationDate) <= 0;
    }
    

    public static boolean isCvvValid(String cvv) {
        return cvv.matches("\\d{3,4}");
    }


    public void displayloadingScreen() {
        FragmentLoading loadingFragment = FragmentLoading.newInstance("Validation du paiement...");
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loadingFragment);
        //pour pas le fragment soit restauré lorsque l'utilisateur appuie sur le bouton retour
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void dismissLoadingScreen() {
        getSupportFragmentManager().popBackStack();
    }
    
}