package com.example.gpgh_interimaire;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import java.util.Locale;


public class FragPageCompte extends Fragment {

    static final String TAG = "FragPageCompte";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ActivityResultLauncher<String> mGetContent;

    TextView editNom, editPrenom, editEmail, editNumero, editTypeCompte, editCV;
    String firstName, lastName, phoneNumber, email, typeCompte, nomCV;
    ImageView profilePictureImageView, language;

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

        editNom = view.findViewById(R.id.editNom);
        editPrenom = view.findViewById(R.id.editPrenom);
        editEmail = view.findViewById(R.id.editEmail);
        editNumero = view.findViewById(R.id.editNumero);
        editTypeCompte = view.findViewById(R.id.editTypeCompte);
        editCV = view.findViewById(R.id.editCV);

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

        ImageView change_language = view.findViewById(R.id.change_language);
        change_language.setOnClickListener(view13 -> {
            selectLanguage();   
        });

        ImageView supprimerButton = view.findViewById(R.id.image_delete);
        supprimerButton.setOnClickListener(view14 -> { deleteAccount(); });

        ImageView logoutButton = view.findViewById(R.id.image_logout);
        logoutButton.setOnClickListener(view15 -> logoutUser());

        language = view.findViewById(R.id.language);
        if (Locale.getDefault().getLanguage().equals("en")) {
            language.setImageResource(R.drawable.icon_language_us);
        } else {
            language.setImageResource(R.drawable.icon_language_fr);
        }


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

                        editPrenom.setText(firstName);
                        editNom.setText(lastName);
                        editEmail.setText(email);
                        editNumero.setText(phoneNumber);
                        editTypeCompte.setText(typeCompte);

                        setProfileImage();
                    }
                    else {
                        Log.w(TAG, "DB Non trouvée");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching user info", e));

        // récupération du CV
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("CV/" + userId + "/");

        fileRef.listAll()
        .addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                nomCV = item.getName();

                // récupérer l'URL de téléchargement
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String cvUrl = uri.toString();
                    editCV.setText(nomCV);
                    editCV.setTextColor(ContextCompat.getColor(getActivity(), R.color.bleu_500));

                    // télécharger le fichier lorsque l'on clique dessus
                    editCV.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(cvUrl), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Intent chooser = Intent.createChooser(intent, "Open with");

                        // Vérifie qu'il existe une application pouvant gérer l'intent
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(chooser);
                        } else {
                            Toast.makeText(getActivity(), "No PDF reader installed", Toast.LENGTH_SHORT).show();
                        }

                    });
                }).addOnFailureListener(exception -> {
                    editCV.setText(R.string.no_cv);
                });
                return;
            }
            editCV.setText(R.string.no_cv);
        })
        .addOnFailureListener(exception -> editCV.setText(R.string.no_cv));
    }

    private void downloadFile(StorageReference fileRef) {
        File localFile;
        try {
            // couper nomCV en 2 pour récupérer l'extension
            String[] parts = nomCV.split("\\.");
            String prefixe_cv = parts[0];
            localFile = File.createTempFile(prefixe_cv, ".pdf");
        } 
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    
        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri fileUri = Uri.fromFile(localFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getActivity(), "Erreur lors du téléchargement du fichier", Toast.LENGTH_SHORT).show();
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


    private void selectLanguage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.changer_langue)
                .setTitle(R.string.language)
                .setPositiveButton(R.string.oui, (dialog, id) -> {
                    changerLangue();
                })
                .setNegativeButton(R.string.annuler, (dialog, id) -> { dialog.dismiss(); });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void changerLangue() {
        Log.d(TAG, "changerLangue: ");

        String languageToLoad;
        if (Locale.getDefault().getLanguage() == "en") {
            languageToLoad = "fr";
            language.setImageResource(R.drawable.icon_language_fr);
        } 
        else {
            languageToLoad = "en";
            language.setImageResource(R.drawable.icon_language_us);
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        Context fragmentContext = requireContext();
        Resources fragmentResources = fragmentContext.getResources();
        fragmentResources.updateConfiguration(config, fragmentResources.getDisplayMetrics());

        // Refresh le fragment
        FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }

    


}