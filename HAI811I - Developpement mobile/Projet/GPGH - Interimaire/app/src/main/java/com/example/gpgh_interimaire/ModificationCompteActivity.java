package com.example.gpgh_interimaire;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class ModificationCompteActivity extends AppCompatActivity {


    ActivityResultLauncher<String> mGetContent;
    ImageView profilePictureImageView;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast", "CutPasteId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_compte);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificationCompteActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Compte");
                startActivity(i);
            }
        });

        // Ajouter laphotode profil dans la modification
        setProfileImage();
        // Récuperation de la photo
        profilePictureImageView = findViewById(R.id.profilePicture);
        // SI on appuie dessus
        ImageView upload_photo = findViewById(R.id.profilePicture);
        upload_photo.setOnClickListener(view1 -> {
            mGetContent.launch("image/*");
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> uploadPictureFromGalery(uri)
        );


        Button modifierButton = findViewById(R.id.boutton_modifier);
        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificationCompteActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Compte");
                startActivity(i);
                Toast.makeText(ModificationCompteActivity.this,R.string.compteModif,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadPictureFromGalery(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("profils/" + mAuth.getCurrentUser().getUid() + ".jpg");

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
                Toast.makeText(this, imageUrl,Toast.LENGTH_SHORT).show();
            } else {
                // Si le fichier n'existe pas localement, on le download de Firebase Storage
                imagesRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Uri fileUri = Uri.fromFile(localFile);
                    String imageUrl = fileUri.toString();
                    Picasso.get().load(imageUrl).fit().centerCrop().into(profilePictureImageView);
                    Toast.makeText(this, "Image téléchargée",Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}