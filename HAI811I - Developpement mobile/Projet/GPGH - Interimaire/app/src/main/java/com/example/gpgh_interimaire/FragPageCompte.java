package com.example.gpgh_interimaire;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class FragPageCompte extends Fragment {

    static final String TAG = "FragPageCompte";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ActivityResultLauncher<String> mGetContent;

    TextView textViewNom, textViewPrenom, textViewEmail, textViewNumero, textViewTypeCompte;
    String firstName, lastName, phoneNumber, email, typeCompte;
    ImageView profilePictureImageView;

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

        profilePictureImageView = view.findViewById(R.id.profilePicture);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        if (userId != null) {
            email = currentUser.getEmail();
            fetchUserInfo(userId);
        }

        Button upload_photo = view.findViewById(R.id.upload_photo);
        upload_photo.setOnClickListener(view1 -> {
            mGetContent.launch("image/*");
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Le résultat de l'activité de sélection d'image est renvoyé ici
                    // Vous pouvez utiliser l'URI de l'image sélectionnée pour télécharger l'image sur Firebase Storage
                    uploadPictureFromGalery(uri);
                });

        ImageView modifierButton = view.findViewById(R.id.image_modif);
        modifierButton.setOnClickListener(view12 -> {
            Intent i = new Intent(getActivity(), ModificationCompteActivity.class);
            startActivity(i);
        });

        ImageView supprimerButton = view.findViewById(R.id.image_delete);
        supprimerButton.setOnClickListener(view13 -> {
            Toast.makeText(getActivity(), R.string.boutton_supprimer,Toast.LENGTH_SHORT).show();
            // affichage d'une boite de dialogue pour confirmer
            logoutUser();
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

                        /*
                         StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                         StorageReference imagesRef = storageRef.child("profils/" + UserId + ".jpg");
                         // Récupérer l'URL de la photo de profil depuis la base de données Firebase
                         String profilePictureURL = documentSnapshot.getString("profilePictureURL");
                         Picasso.get().load(profilePictureURL).into(profilePictureImageView);
                         */

                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


    private void uploadPictureFromGalery(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("profils/" + imageUri.getLastPathSegment());

        UploadTask uploadTask = imagesRef.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Gérer l'échec de l'upload de l'image
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Récupérer l'URL de l'image téléchargée
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Mettre à jour la photo de profil de l'utilisateur dans la base de données Firebase Firestore avec l'URL de l'image téléchargée
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userRef = db.collection("users").document(user.getUid());
                            userRef.update("profilePictureUrl", uri.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Mettre à jour l'UI pour afficher la nouvelle photo de profil de l'utilisateur
                                        }
                                    });
                        }
                    }
                });
            }
        });

    }



}