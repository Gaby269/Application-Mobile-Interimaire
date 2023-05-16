package com.example.gpgh_interimaire;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.gpgh_interimaire.FragmentLoading;

public class InscriptionActivity extends AppCompatActivity {

    static final String TAG = "InscriptionActivity";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText nomEditText, prenomEditText, telephoneEditText, mailEditText, mdpEditText;
    Spinner typeCompteSpinner;
    String selectedTypeCompte = "Candidat";

    FragmentTransaction transaction;


    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nomEditText = findViewById(R.id.nomEditText);
        prenomEditText = findViewById(R.id.prenomEditText);
        telephoneEditText = findViewById(R.id.telephoneEditText);
        mailEditText = findViewById(R.id.mailEditText);
        mdpEditText = findViewById(R.id.mdpEditText);
        typeCompteSpinner = findViewById(R.id.typeCompteSpinner);

        // On regarde si on change le spinner
        typeCompteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeCompte = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        Button creaCompteButton = findViewById(R.id.boutton_creationCompte);
        creaCompteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    displayLoadingScreen();
                    createUser();
                }
            }
        });

        //Récupération du Spinner déclaré dans le fichier activity_inscription.xml
        Spinner typeCompteSpinner = (Spinner) findViewById(R.id.typeCompteSpinner);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        List<String> listType = new ArrayList<>();
        listType.add("Type de compte");
        listType.add("Candidat");
        listType.add("Entreprise");
        listType.add("Agence d'intérim");
        // ArrayAdapter pour le spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,listType);
        // On definit une présentation du spinner quand il est déroulé
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCompteSpinner.setAdapter(adapterSpinner); // on passe l'adapter au Spinner

        //signup tmp
        Button tmpSignup = findViewById(R.id.button);
        tmpSignup.setOnClickListener(view -> {
            Random rand = new Random();
            String randInt = Integer.toString(rand.nextInt(9000)+1000); //entre 1000 et 9999
            mailEditText.setText(randInt+"@gmail.com");
            mdpEditText.setText("123456");
            telephoneEditText.setText("0782235495");
            nomEditText.setText("Jean"+randInt);
            prenomEditText.setText("Test"+randInt);

            Log.w(TAG, "Creation du compte "+randInt);
            if (validateInput()) {
                displayLoadingScreen();
                createUser();
            }
        });

        TextView goToConnexionButton = findViewById(R.id.boutton_connexion);
        goToConnexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                startActivity(i);
            }
        });
    }


    private void createUser() {
        String prenom = prenomEditText.getText().toString();
        String nom = nomEditText.getText().toString();
        String tel = telephoneEditText.getText().toString();
        String email = mailEditText.getText().toString();
        String password = mdpEditText.getText().toString();
        String type = selectedTypeCompte;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inscription réussie
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        addUserToFirestore(userId, prenom, nom, email, tel, type);

                        dismissLoadingScreen();
                        Intent i = new Intent(InscriptionActivity.this, ConfirmationTelephoneActivity.class);
                        i.putExtra("phoneNumber", tel);
                        startActivity(i);
                        finish();
                    }
                    else {
                        dismissLoadingScreen();
                        Exception e = task.getException();
                        // Log.w(TAG, "createUserWithEmail:failure", e);
                        if (e instanceof FirebaseAuthException) {
                            gererExceptionsFirebase((FirebaseAuthException) e);
                        } else { // Erreur inconnue
                            Toast.makeText(InscriptionActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToFirestore(String userId, String prenom, String nom, String email, String tel, String type) {
        Map<String, Object> user = new HashMap<>();
        user.put("prenom", prenom);
        user.put("nom", nom);
        user.put("telephone", tel);
        user.put("email", email);
        user.put("typeCompte", type);
        user.put("typeAbo", "0");
        user.put("signup_step", "1");

        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Utilisateur ajouté à la BDD"))
                .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de l'ajout dans la BDD", e));
    }


    private boolean validateInput() {
        String email = mailEditText.getText().toString();
        String password = mdpEditText.getText().toString();
        String tel = telephoneEditText.getText().toString();
        String nom = nomEditText.getText().toString();
        String prenom = prenomEditText.getText().toString();
        boolean check = true;
        if (email.isEmpty()) {
            mailEditText.setError(getString(R.string.email_vide));
            check = false;
        }
        else if (!isEmailValid(email)) {
            mailEditText.setError(getString(R.string.email_invalide));
            check = false;
        }

        if (nom.isEmpty()) {
            nomEditText.setError(getString(R.string.nom_vide));
            check = false;
        }

        if (prenom.isEmpty()) {
            prenomEditText.setError(getString(R.string.prenom_vide));
            check = false;
        }

        if (tel.length() < 10) {
            telephoneEditText.setError(getString(R.string.tel_vide));
            check = false;
        }

        if (password.isEmpty()) {
            mdpEditText.setError(getString(R.string.mdp_vide));
            check = false;
        }
        else if (password.length() < 6) {
            mdpEditText.setError(getString(R.string.mdp_court));
            check = false;
        }

        if (typeCompteSpinner.getSelectedItemPosition() == 0) {
            ((TextView) typeCompteSpinner.getSelectedView()).setError(getString(R.string.choisir_compte));
            check = false;
        }

        return check;
    }

    public static boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }    


    private void redirect() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Utilisateur déjà connecté, check si connexion effectuée
            String userId = currentUser.getUid();
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int signup_step = Integer.parseInt(documentSnapshot.getString("signup_step"));
                            if (signup_step == 1) {
                                Intent i = new Intent(InscriptionActivity.this, ConfirmationTelephoneActivity.class);
                                startActivity(i);
                            }
                            else if (signup_step == 2) {
                                Intent i = new Intent(InscriptionActivity.this, EntrepriseActivity.class);
                                startActivity(i);
                            }
                            //suite des else
                            else {
                                Intent i = new Intent(InscriptionActivity.this, LoadingNavbarActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        else {
                            Log.w(TAG, "DB Non trouvée");
                        }
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
        }
    }


    private void gererExceptionsFirebase(FirebaseAuthException e) {
        String errorCode = e.getErrorCode();
        String errorMessage = e.getMessage();
    
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                // l'email est mal formé
                mailEditText.setError(getString(R.string.email_invalide));
                break;
            case "ERROR_WEAK_PASSWORD":
                // le mot de passe est trop faible
                mdpEditText.setError(getString(R.string.mdp_court));
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                // l'email est déjà utilisé par un autre utilisateur
                mailEditText.setError(getString(R.string.email_deja_utilise));
                break;
            default:
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }



    public void displayLoadingScreen() {
        FragmentLoading loadingFragment = FragmentLoading.newInstance("Chargement...");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, loadingFragment, "loading_fragment");
        transaction.commit();
    }

    public void dismissLoadingScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentLoading loadingFragment = (FragmentLoading) fragmentManager.findFragmentByTag("loading_fragment");

        if (loadingFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(loadingFragment);
            transaction.commit();
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        redirect();
    }
}