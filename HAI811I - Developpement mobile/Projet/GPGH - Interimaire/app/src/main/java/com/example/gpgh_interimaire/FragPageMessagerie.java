package com.example.gpgh_interimaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.Collections;

public class FragPageMessagerie extends Fragment {

    String TAG = "FragPageMessagerie";

    private FirebaseFirestore db;
    FirebaseUser currentUser;

    private EditText editTextTextEmailAddress;
    private Button button_add;

    // Constructeur
    public FragPageMessagerie() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_messagerie, container, false);
    
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        editTextTextEmailAddress = view.findViewById(R.id.editTextTextEmailAddress);
        button_add = view.findViewById(R.id.button_add);

        button_add.setOnClickListener(v -> {
            String email = editTextTextEmailAddress.getText().toString().trim();
            if (!email.isEmpty()) {
                createConversation(email);
            } 
            else {
                editTextTextEmailAddress.setError(getString(R.string.erreurChamp));
            }
        });

        return view;
    }

    // mettre la recyclerview pour fetch toutes les conversaitons


    private void createConversation(String email) {
        CollectionReference conversationsRef = db.collection("conversations");
    
        // Récupérer l'utilisateur connecté
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
    
            // Vérifier si l'adresse e-mail entrée correspond à un autre utilisateur
            Query query = db.collection("users")
                    .whereEqualTo("email", email)
                    .limit(1);
    
            query.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String otherUserId = documentSnapshot.getId();
    
                            if (!otherUserId.equals(currentUserId)) {
    
                                // Vérifier si la conversation existe déjà
                                Query existingConversationQuery = conversationsRef
                                    .whereIn("participants", Arrays.asList(
                                            Arrays.asList(currentUserId, otherUserId),
                                            Arrays.asList(otherUserId, currentUserId)))
                                    .limit(1);
    
                                existingConversationQuery.get()
                                        .addOnSuccessListener(existingQueryDocumentSnapshots -> {
                                            if (existingQueryDocumentSnapshots.isEmpty()) {
                                                // Aucune conversation existante trouvée, en créer une nouvelle
                                                Conversation conversation = new Conversation();
                                                conversation.setDernierMessage("");
                                                conversation.setParticipants(Arrays.asList(currentUserId, otherUserId));
                                                conversation.setNonLu(Collections.singletonList(otherUserId));
    
                                                // Ajouter la conversation à Firestore
                                                conversationsRef.add(conversation)
                                                        .addOnSuccessListener(documentReference -> {
                                                            String conversationId = documentReference.getId();
                                                            Log.d(TAG, "Conversation créée avec succès, ID : " + conversationId);
    
                                                            editTextTextEmailAddress.setText("");
                                                            Intent intent = new Intent(getActivity(), MessagesActivity.class);
                                                            intent.putExtra("conversationId", conversationId);
                                                            startActivity(intent);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e(TAG, "Erreur lors de la création de la conversation", e);
                                                            Toast.makeText(getActivity(), "Erreur lors de la création de la conversation", Toast.LENGTH_SHORT).show();
                                                        });
                                            } 
                                            else {
                                                // Une conversation existante a été trouvée, la lancer
                                                DocumentSnapshot documentSnapshot1 = existingQueryDocumentSnapshots.getDocuments().get(0);
                                                String conversationId = documentSnapshot1.getId();
                                                Log.d(TAG, "Conversation récupérée avec succès, ID : " + conversationId);
    
                                                editTextTextEmailAddress.setText("");
                                                Intent intent = new Intent(getActivity(), MessagesActivity.class);
                                                intent.putExtra("conversationId", conversationId);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Erreur lors de la vérification de l'existence d'une conversation", e);
                                            Toast.makeText(getActivity(), "Erreur lors de la vérification de l'existence d'une conversation", Toast.LENGTH_SHORT).show();
                                        });
    
                            } else {
                                // L'adresse e-mail entrée correspond à l'utilisateur connecté
                                Toast.makeText(getActivity(), "Vous ne pouvez pas démarrer une conversation avec vous-même", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Aucun utilisateur trouvé avec l'adresse e-mail entrée
                            Toast.makeText(getActivity(), "Aucun utilisateur trouvé avec cette adresse e-mail", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Erreur lors de la vérification de l'adresse e-mail", e);
                        Toast.makeText(getActivity(), "Erreur lors de la vérification de l'adresse e-mail", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    
}