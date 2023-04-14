package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class FragPageCompte extends Fragment {

    static final String TAG = "FragPageCompte";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView textViewNom, textViewPrenom, textViewEmail, textViewNumero;
    String firstName, lastName, phoneNumber, email;

    // Constructeur
    public FragPageCompte() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_compte, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewNom = view.findViewById(R.id.textViewNom);
        textViewPrenom = view.findViewById(R.id.textViewPrenom);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewNumero = view.findViewById(R.id.textViewNumero);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        if (userId != null) {
            email = currentUser.getEmail();
            fetchUserInfo(userId);
        }

        Button modifierButton = view.findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ModificationCompteActivity.class);
                startActivity(i);
            }
        });

        Button supprimerButton = view.findViewById(R.id.boutton_supprimer);
        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.boutton_supprimer,Toast.LENGTH_SHORT).show();
                // affichage d'une boite de dialogue pour confirmer
                logoutUser();
            }
        });

        return view;
    }


    private void logoutUser() {
        // Créez un AlertDialog pour demander confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setTitle("Confirmation")
                .setPositiveButton("Oui", (dialog, id) -> {
                    // Si l'user confirme, déco
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    //finish();
                })
                .setNegativeButton("Annuler", (dialog, id) -> {
                    // Si l'user annule
                    dialog.dismiss();
                });

        // Affichez l'AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
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

                        textViewPrenom.setText(getResources().getString(R.string.nom) + " : " + firstName);
                        textViewNom.setText(getResources().getString(R.string.prenom) + " : " + lastName);
                        textViewEmail.setText(getResources().getString(R.string.mail) + " : " + email);
                        textViewNumero.setText(getResources().getString(R.string.telephone) + " : " + phoneNumber);

                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


}