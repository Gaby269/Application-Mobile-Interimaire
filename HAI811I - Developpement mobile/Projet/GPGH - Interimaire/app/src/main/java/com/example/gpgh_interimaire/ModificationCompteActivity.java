package com.example.gpgh_interimaire;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class ModificationCompteActivity extends AppCompatActivity {

    String TAG = "ModificationCompteActivity";

    ActivityResultLauncher<String> mGetContent;
    FirebaseUser user;
    FirebaseFirestore db;
    ImageView profilePictureImageView;
    EditText editNom, editPrenom, editEmail, editNumero, editTypeCompte;

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast", "CutPasteId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_compte);

        Intent i = getIntent();
        String typeCompte = i.getStringExtra("typeCompte");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editEmail = findViewById(R.id.editEmail);
        editNumero = findViewById(R.id.editNumero);
        editTypeCompte = findViewById(R.id.editTypeCompte);
        profilePictureImageView = findViewById(R.id.profilePicture);
        fetchUserInfo();

        ImageView upload_photo = findViewById(R.id.editButtonPhoto);
        upload_photo.setOnClickListener(view1 -> {
            mGetContent.launch("image/*");
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> uploadPictureFromGalery(uri)
        );

        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(view -> {
            updateAccountInfo();
        });


        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificationCompteActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Compte");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });
    }


    private void uploadPictureFromGalery(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("profils/" + user.getUid() + ".jpg");

        UploadTask uploadTask = imagesRef.putFile(imageUri);
        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(this, R.string.echec_upload,Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this, R.string.success_upload,Toast.LENGTH_SHORT).show();
            setProfileImage();
        });

    }

    private void setProfileImage() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("profils/" + user.getUid() + ".jpg");

        final File localFile;
        try {
            localFile = File.createTempFile("profilePic", "jpg");
            localFile.delete(); // à enlever après les tests
            if (localFile.exists()) {
                // Si le fichier existe déjà localement, on charge l'image
                Uri fileUri = Uri.fromFile(localFile);
                String imageUrl = fileUri.toString();
                Picasso.get().load(imageUrl).fit().centerCrop().into(profilePictureImageView);
                Toast.makeText(this, imageUrl,Toast.LENGTH_SHORT).show();
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
    

    private void fetchUserInfo() {

        if (user != null) {
            String email = user.getEmail();
            editEmail.setText(email);
        }

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("prenom");
                        String lastName = documentSnapshot.getString("nom");
                        String phoneNumber = documentSnapshot.getString("telephone");
                        String typeCompte = documentSnapshot.getString("typeCompte");

                        editNom.setText(lastName);
                        editPrenom.setText(firstName);
                        editNumero.setText(phoneNumber);
                        editTypeCompte.setText(typeCompte);

                        setProfileImage();
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


    private void updateAccountInfo() {
        String firstName = editNom.getText().toString();
        String lastName = editPrenom.getText().toString();
        String phoneNumber = editNumero.getText().toString();
        String email = editEmail.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(email)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Adresse e-mail mise à jour !");
                }
                else {
                    Log.e(TAG, "Erreur lors de la mise à jour de l'adresse e-mail.", task.getException());
                }
            });


        db.collection("users")
                .document(user.getUid())
                .update("prenom", firstName,
                        "nom", lastName,
                        "telephone", phoneNumber)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ModificationCompteActivity.this,R.string.compteModif,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ModificationCompteActivity.this, LoadingNavbarActivity.class);
                    i.putExtra("fragment", "Compte");
                    startActivity(i);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de la mise à jour des informations", Toast.LENGTH_SHORT).show());
    }

}