package com.example.gpgh_interimaire;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final int PICK_CV_FILE = 2;

    ActivityResultLauncher<String> mGetContent;
    FirebaseUser user;
    FirebaseFirestore db;
    Uri cvFileUri;
    String cvFileName;

    EditText editNom, editPrenom, editEmail, editNumero, editTypeCompte;
    TextView editCV;
    ImageView uploadCV;
    ImageView profilePictureImageView;


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
        editCV = findViewById(R.id.editCV);
        uploadCV = findViewById(R.id.editButtonCV);
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

        uploadCV.setOnClickListener(view -> {
            openFilePicker(PICK_CV_FILE);
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



    public void openFilePicker(int fichier) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
    
        try {
            startActivityForResult(Intent.createChooser(intent, "Sélectionnez un fichier PDF"), fichier);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Veuillez installer un gestionnaire de fichiers.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CV_FILE: // Si CV
                if (resultCode == RESULT_OK) {
                    cvFileUri = data.getData();
                    setPdfName(cvFileUri);
                }
                break;
            default: // si pas un pdf
                Toast.makeText(this, R.string.erreur_pdf, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    private void setPdfName(Uri fileUri) {
        String fileName = null;
        Cursor cursor = null;
    
        try {
            String[] projection = { MediaStore.MediaColumns.DISPLAY_NAME };
            cursor = getContentResolver().query(fileUri, projection, null, null, null);
    
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                fileName = cursor.getString(nameIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (fileName != null) {
            cvFileName = fileName;
            editCV.setText(fileName);
            editCV.setTypeface(null, Typeface.BOLD);
        }
    
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

    private void setCvName() {
        String userId = user.getUid();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("CV/" + userId + "/");

        fileRef.listAll()
        .addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                editCV.setText(item.getName());
                return;
            }
            editCV.setText(R.string.no_cv);
        })
        .addOnFailureListener(exception -> {
            editCV.setText(R.string.no_cv);
        });
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
                        setCvName();
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));
    }


    private void updateAccountInfo() {

        displayLoadingScreen();

        String firstName = editNom.getText().toString();
        String lastName = editPrenom.getText().toString();
        String phoneNumber = editNumero.getText().toString();
        String email = editEmail.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        user.updateEmail(email)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Adresse e-mail mise à jour !");
                }
                else {
                    Log.e(TAG, "Erreur lors de la mise à jour de l'adresse e-mail.", task.getException());
                }
            });

        // données de l'utilisateur
        db.collection("users")
                .document(userId)
                .update("prenom", firstName,
                        "nom", lastName,
                        "telephone", phoneNumber)
                .addOnSuccessListener(aVoid -> {       
                    // Stockage du CV
                    if (cvFileUri != null) {
                        if (cvFileName == null) {
                            cvFileName = "MonCV.pdf";
                        }
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference cvFolderRef = storageRef.child("CV/" + userId + "/");

                        // supprimer tous les CV déjà existants
                        cvFolderRef.listAll()
                        .addOnSuccessListener(listResult -> {
                            for (StorageReference item : listResult.getItems()) {item.delete();}
                            
                            // ajout du cv
                            StorageReference fileRef = storageRef.child("CV/" + userId + "/" + cvFileName);
                            fileRef.putFile(cvFileUri)
                                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                                            .addOnSuccessListener(bVoid -> {
                                                dismissLoadingScreen();
                                                Toast.makeText(ModificationCompteActivity.this,R.string.compteModif,Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(ModificationCompteActivity.this, LoadingNavbarActivity.class);
                                                i.putExtra("fragment", "Compte");
                                                startActivity(i);
                                            })
                                            .addOnFailureListener(e -> {
                                                dismissLoadingScreen();
                                                Toast.makeText(this, "Erreur lors de la récupération de l'URL de téléchargement.", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "Erreur lors de la récupération de l'URL de téléchargement.", e);
                                            }))
                                    .addOnFailureListener(e -> {
                                        dismissLoadingScreen();
                                        Toast.makeText(this, "Erreur lors de l'upload du fichier.", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Erreur lors de l'upload du fichier.", e);
                                    });
                        })
                        .addOnFailureListener(e -> {
                            dismissLoadingScreen();
                            Toast.makeText(this, "Erreur lors de la mise à jour du CV.", Toast.LENGTH_SHORT).show();
                        });
                    }
                    else {
                        dismissLoadingScreen();
                        Toast.makeText(ModificationCompteActivity.this,R.string.compteModif,Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ModificationCompteActivity.this, LoadingNavbarActivity.class);
                        i.putExtra("fragment", "Compte");
                        startActivity(i);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de la mise à jour des informations", Toast.LENGTH_SHORT).show());
    }




    public void displayLoadingScreen() {
        FragLoading loadingFragment = FragLoading.newInstance("Enregistrement...");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, loadingFragment, "loading_fragment");
        transaction.commit();
    }

    public void dismissLoadingScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragLoading loadingFragment = (FragLoading) fragmentManager.findFragmentByTag("loading_fragment");

        if (loadingFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(loadingFragment);
            transaction.commit();
        }
    }

}