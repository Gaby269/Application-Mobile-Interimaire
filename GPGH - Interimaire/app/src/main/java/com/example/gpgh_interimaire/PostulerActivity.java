package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PostulerActivity extends AppCompatActivity {

    String TAG = "PostulerActivity";

    private static final int PICK_CV_FILE = 2;
    private static final int PICK_LM_FILE = 3;

    FirebaseUser user;
    FirebaseFirestore db;

    Uri cvFileUri, lmFileUri;
    String nomCV, nomLM;

    String id_offre, titre_offre, typeCompte;

    EditText descriptionOffre;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postuler);

        Intent i = getIntent();
        id_offre = i.getStringExtra("idOffre");
        titre_offre = i.getStringExtra("titreOffre");
        typeCompte = i.getStringExtra("typeCompte");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        descriptionOffre = findViewById(R.id.descriptionOffreEditText);

        getCV();

        TextView titreOffreTextView = findViewById(R.id.offreTextView);
        titreOffreTextView.setText(titre_offre);

        ImageButton retourButton = findViewById(R.id.bouton_retour);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;

                i = new Intent(PostulerActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            }
        });

        Button CVbutton = findViewById(R.id.CVbutton);
        CVbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker(PICK_CV_FILE);
            }
        });

        Button LMbutton = findViewById(R.id.LMbutton);
        LMbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker(PICK_LM_FILE);
            }
        });

        Button envoyerButton = findViewById(R.id.boutton_envoyer);
        envoyerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Envoyer la candidature
                if (cvFileUri == null && nomCV == null) {
                    TextView CVTextView = findViewById(R.id.CVTextView);
                    CVTextView.setError(getString(R.string.cv_vide));
                }
                else {
                    displayLoadingScreen();
                    uploadCandidatureToFirebase();
                }
            }
        });

    }

    private void getCV() {
        // récupération du CV
        String userId = user.getUid();
        TextView editCV = findViewById(R.id.CVTextView);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("CV/" + userId + "/");

        fileRef.listAll()
        .addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                nomCV = item.getName();
                editCV.setText(nomCV);
                editCV.setTextColor(ContextCompat.getColor(this, R.color.bleu_500));
                return;
            }
            editCV.setText(R.string.no_cv);
        })
        .addOnFailureListener(exception -> editCV.setText(R.string.no_cv));
    }
    

    public void openFilePicker(int fichier) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
    
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Sélectionnez un fichier PDF"), fichier);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Veuillez installer un gestionnaire de fichiers.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CV_FILE:  // Si CV
                if (resultCode == RESULT_OK) {
                    cvFileUri = data.getData();
                    setPdfName(cvFileUri, R.id.CVTextView, "CV");
                }
                break;
            case PICK_LM_FILE:  // Si lettre de motivation
                if (resultCode == RESULT_OK) {
                    lmFileUri = data.getData();
                    setPdfName(lmFileUri, R.id.LMTextView, "LM");
                }
                break;
            default: // si pas un pdf
                Toast.makeText(this, R.string.erreur_pdf, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private void setPdfName(Uri fileUri, int textViewId, String type) {
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
            if (type.equals("CV")) {nomCV = fileName;}
            else {nomLM = fileName;}
            TextView textView = findViewById(textViewId);
            textView.setText(fileName);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    
    }
    

    private void uploadFileToFirebaseStorage(Uri fileUri, String filePrefix) {
        if (fileUri == null) {
            return;
        }
        
        String userId = user.getUid();
    
        String filePath = "candidatures/" + id_offre + "/" + userId + "/" + nomLM;

        if (filePrefix.equals("CV")) {
            filePath = "CV/" + userId + "/" + nomCV;

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference cvFolderRef = storageRef.child("CV/" + userId + "/");

            // supprimer tous les CV déjà existants
            cvFolderRef.listAll()
            .addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {item.delete();}
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Erreur lors de la mise à jour du CV.", Toast.LENGTH_SHORT).show();
            });
        }
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(filePath);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {return;})
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erreur lors de la récupération de l'URL de téléchargement.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Erreur lors de la récupération de l'URL de téléchargement.", e);
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de l'upload du fichier.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Erreur lors de l'upload du fichier.", e);
                });
    }
    

    private void uploadCandidatureToFirebase() {
        
        String userId = user.getUid();
        String description = descriptionOffre.getText().toString();
    
        uploadFileToFirebaseStorage(cvFileUri, "CV");
        uploadFileToFirebaseStorage(lmFileUri, "LM");

        Map<String, Object> candidature = new HashMap<>();
        candidature.put("id_offre", id_offre);
        candidature.put("userId", userId);
        candidature.put("description", description);
        candidature.put("etat", "En attente");

        db.collection("candidatures")
            .add(candidature)
            .addOnSuccessListener(documentReference -> {
                dismissLoadingScreen();
                Toast.makeText(this, R.string.candidature_envoyee, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PostulerActivity.this, NavbarActivity.class);
                i.putExtra("fragment", "Offre");
                i.putExtra("typeCompte", typeCompte);
                startActivity(i);
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Erreur lors de l'enregistrement de la candidature.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erreur lors de l'enregistrement de la candidature.", e);
            });
    }


    public void displayLoadingScreen() {
        FragLoading loadingFragment = FragLoading.newInstance("Envoi de la candidature...");
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