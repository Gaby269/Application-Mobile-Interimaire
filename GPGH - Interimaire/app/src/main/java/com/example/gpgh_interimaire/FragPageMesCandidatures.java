package com.example.gpgh_interimaire;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragPageMesCandidatures extends Fragment {

    String TAG = "FragPageMesCandidatures";
    FirebaseFirestore db;

    RecyclerView recyclerView;
    String userId;

    // Constructeur
    public FragPageMesCandidatures() {}

    // Création de la vue pour le fragment 1
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Associé la vue au layout du fragment 1
        View view = inflater.inflate(R.layout.frag_page_mes_candidatures, container, false);
        recyclerView = view.findViewById(R.id.recycleview);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        // on cache les trucs qui marchent pas
        LinearLayout layout_recherche = view.findViewById(R.id.layout_recherche);
        layout_recherche.setVisibility(View.GONE);

        List<ItemCandidature> items = new ArrayList<ItemCandidature>();
        // items.add(new ItemCandidature("0", "1", "John", "Doe", "Expérience en tant que magasinier", "Disponible immédiatement", "cv_john_doe.pdf"));
        // items.add(new ItemCandidature("0", "2", "Jane", "Smith", "Compétences en secrétariat et gestion administrative", "Bilingue français-anglais", "cv_jane_smith.pdf"));
        // items.add(new ItemCandidature("0", "3", "Michael", "Johnson", "Expérience en tant que chauffeur-livreur", "Permis de conduire valide", "cv_michael_johnson.pdf"));
        // items.add(new ItemCandidature("0", "4", "Emily", "Brown", "Compétences en marketing digital", "Maîtrise des réseaux sociaux", "cv_emily_brown.pdf"));
        // items.add(new ItemCandidature("0", "5", "Daniel", "Davis", "Expérience en tant que technicien informatique", "Certification Cisco CCNA", "cv_daniel_davis.pdf"));

        Log.d(TAG, "Récupération des candidatures de l'utilisateur : " + userId);
        db.collection("candidatures")
        .whereEqualTo("userId", userId)
        .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                // Vérifier si la QuerySnapshot n'est pas null ou vide
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String documentId = document.getId();
                        //Récupérer les données de l'offre
                        String offreId = document.getString("id_offre");
                        String description = document.getString("description");
                        String etat = document.getString("etat");

                        // Récupérer les données de l'offre à partir de la collection "offres"
                        DocumentReference docRef = db.collection("offres").document(offreId);
                        docRef.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot document1 = task1.getResult();
                                if (document1.exists()) {
                                    Log.d(TAG, "candidature récupérée");
                                    String titre = document1.getString("titre");
                                    String entreprise = document1.getString("nomEntreprise");
                                    String dateDebut = document1.getString("dateDeb");
                                    items.add(new ItemCandidature(documentId, userId, titre, "", entreprise, etat, dateDebut));
                                    setupRecyclerView(items);
                                }
                                else {
                                    Log.d(TAG, "Erreurs lors de la récupération de l'offre : ", task1.getException());
                                }
                            }
                            else {
                                Log.d(TAG, "Erreur lors de la récupération de la candidature : ", task1.getException());
                                setupRecyclerView(items);
                            }
                        });
                    }
                }
                else { // La QuerySnapshot est vide
                    Log.d(TAG, "Aucune candidature trouvée");
                    setupRecyclerView(items);
                }
            }
            else {
                Log.d(TAG, "Erreur lors de la récupération des candidatures : ", task.getException());
            }
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "Erreur lors de la récupération des candidatures : ", e);
            setupRecyclerView(items);
        });

        return view;
    }


    private void setupRecyclerView(List<ItemCandidature> items) {
        if (items.isEmpty()) {
            items.add(new ItemCandidature("0", "0", "Oh", "oh", "Vous n'avez pas encore candidaté", "", "Trouvez une offre qui vous correspond et postulez dès maintenant !"));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyAdapterCandidature(getActivity(), items, "Candidat"));
    }
}