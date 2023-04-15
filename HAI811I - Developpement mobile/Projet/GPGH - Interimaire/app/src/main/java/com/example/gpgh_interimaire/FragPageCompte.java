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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class FragPageCompte extends Fragment {

    static final String TAG = "FragPageCompte";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView textViewNom, textViewPrenom, textViewEmail, textViewNumero, textViewTypeCompte;
    String firstName, lastName, phoneNumber, email, typeCompte;

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

        textViewNom = view.findViewById(R.id.editNom);
        textViewPrenom = view.findViewById(R.id.editPrenom);
        textViewEmail = view.findViewById(R.id.editEmail);
        textViewNumero = view.findViewById(R.id.editNumero);
        textViewTypeCompte = view.findViewById(R.id.editTypeCompte);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        if (userId != null) {
            email = currentUser.getEmail();
            fetchUserInfo(userId);
        }

        ImageView modifierButton = view.findViewById(R.id.image_modif);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ModificationCompteActivity.class);
                startActivity(i);
            }
        });

        ImageView supprimerButton = view.findViewById(R.id.image_delete);
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
                        typeCompte = documentSnapshot.getString("typeCompte");

                        textViewPrenom.setText(firstName);
                        textViewNom.setText(lastName);
                        textViewEmail.setText(email);
                        textViewNumero.setText(phoneNumber);
                        textViewTypeCompte.setText(typeCompte);

                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


}