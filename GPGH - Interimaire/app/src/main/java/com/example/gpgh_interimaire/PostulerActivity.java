package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

    Uri cvFileUri;
    Uri lmFileUri;

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
                if (cvFileUri == null) {
                    TextView CVTextView = findViewById(R.id.CVTextView);
                    CVTextView.setError(getString(R.string.cv_vide));
                }
                else {
                    uploadCandidatureToFirebase();
                }
            }
        });

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
                    setPdfName(cvFileUri, R.id.CVTextView);
                }
                break;
            case PICK_LM_FILE:  // Si lettre de motivation
                if (resultCode == RESULT_OK) {
                    lmFileUri = data.getData();
                    setPdfName(lmFileUri, R.id.LMTextView);
                }
                break;
            default: // si pas un pdf
                Toast.makeText(this, R.string.erreur_pdf, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private void setPdfName(Uri fileUri, int textViewId) {
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
            TextView textView = findViewById(textViewId);
            textView.setText(fileName);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    
    }
    


    public void uploadCandidatureToFirebase() {
        String description = descriptionOffre.getText().toString();
        String currentUserId = user.getUid();
    
        uploadFileToFirebaseStorage(cvFileUri, currentUserId, "cv",
            cvDownloadUrl -> uploadFileToFirebaseStorage(lmFileUri, currentUserId, "lm",
            lmDownloadUrl -> saveCandidatureToFirestore(currentUserId, description, cvDownloadUrl, lmDownloadUrl)));
    }
    

    private void uploadFileToFirebaseStorage(Uri fileUri, String userId, String filePrefix, OnSuccessListener<String> onSuccess) {
        if (fileUri == null) {
            onSuccess.onSuccess(null);
            return;
        }
    
        String filePath = "candidatures/" + id_offre + "/" + userId + "/" + filePrefix + ".pdf";
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(filePath);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> onSuccess.onSuccess(uri.toString()))
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erreur lors de la récupération de l'URL de téléchargement.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Erreur lors de la récupération de l'URL de téléchargement.", e);
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de l'upload du fichier.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Erreur lors de l'upload du fichier.", e);
                });
    }
    

    private void saveCandidatureToFirestore(String userId, String description, String cvDownloadUrl, String lmDownloadUrl) {
    
        Map<String, Object> candidature = new HashMap<>();
        candidature.put("userId", userId);
        candidature.put("description", description);
        candidature.put("cvDownloadUrl", cvDownloadUrl);
        candidature.put("lmDownloadUrl", lmDownloadUrl);

        db.collection("candidatures")
            .add(candidature)
            .addOnSuccessListener(documentReference -> {
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
    
}