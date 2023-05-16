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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;


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

        // Récupérer les arguments du bundle
        assert getArguments() != null;
        String typeCompte = getArguments().getString("typeCompte");

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

        ImageView modifierButton = view.findViewById(R.id.image_modif);
        modifierButton.setOnClickListener(view12 -> {
            Intent i = new Intent(getActivity(), ModificationCompteActivity.class);
            i.putExtra("typeCompte", typeCompte);
            startActivity(i);
        });

        ImageView supprimerButton = view.findViewById(R.id.image_delete);
        supprimerButton.setOnClickListener(view13 -> { deleteAccount(); });

        ImageView logoutButton = view.findViewById(R.id.image_logout);
        logoutButton.setOnClickListener(view14 -> logoutUser());



        return view;
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

                        setProfileImage();
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


    private void setProfileImage() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("profils/" + mAuth.getCurrentUser().getUid() + ".jpg");

        final File localFile;
        try {
            localFile = File.createTempFile("profilePic", "jpg");
            localFile.delete(); // à enlever après les tests
            if (localFile.exists()) {
                // Si le fichier existe déjà localement, on charge l'image
                Uri fileUri = Uri.fromFile(localFile);
                String imageUrl = fileUri.toString();
                Picasso.get().load(imageUrl).fit().centerCrop().into(profilePictureImageView);
                Toast.makeText(getActivity(), imageUrl,Toast.LENGTH_SHORT).show();
            } else {
                // Si le fichier n'existe pas localement, on le download de Firebase Storage
                imagesRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Uri fileUri = Uri.fromFile(localFile);
                    String imageUrl = fileUri.toString();
                    Picasso.get().load(imageUrl).fit().centerCrop().into(profilePictureImageView);
                }).addOnFailureListener(e -> {
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.deconnexion_message)
                .setTitle(R.string.deconnexion_titre)
                .setPositiveButton(R.string.oui, (dialog, id) -> {
                    Toast.makeText(getActivity(), R.string.deconnexion_succes, Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.annuler, (dialog, id) -> { dialog.dismiss(); });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.suppression_message)
                .setTitle(R.string.suppression_titre)
                .setPositiveButton(R.string.oui, (dialog, id) -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.compte_supprime_succes, Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else { Toast.makeText(getActivity(), R.string.compte_supprime_echec, Toast.LENGTH_SHORT).show(); }
                            });
                    }
                })
                .setNegativeButton(R.string.annuler, (dialog, id) -> { dialog.dismiss(); });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    


}