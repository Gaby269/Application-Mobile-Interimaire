package com.example.tp3_fragementsservices;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Fragment1 extends Fragment {

    EditText prenomField, nomField, anniversaireField, telephoneField, mailField;

    String FILE_NAME = "data.json";

    public Fragment1() {}

    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        prenomField = view.findViewById(R.id.prenom_field);
        nomField = view.findViewById(R.id.nom_field);
        anniversaireField = view.findViewById(R.id.anniversaire_field);
        telephoneField = view.findViewById(R.id.telephone_field);
        mailField = view.findViewById(R.id.mail_field);
        CheckBox sportsCheckbox = view.findViewById(R.id.sports_checkbox);
        CheckBox musiqueCheckbox = view.findViewById(R.id.musique_checkbox);
        CheckBox lectureCheckbox = view.findViewById(R.id.lecture_checkbox);
        CheckBox dessinCheckbox = view.findViewById(R.id.dessin_checkbox);
        CheckBox photoCheckbox = view.findViewById(R.id.photo_checkbox);
        CheckBox langueCheckbox = view.findViewById(R.id.langue_checkbox);
        CheckBox syncCheckbox = view.findViewById(R.id.sync_checkbox);

        File file = new File(getActivity().getFilesDir(), FILE_NAME);
        if (file.exists()) {
            try {
                loadData();
            }
            catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }

        // Récuperer le bouton de soumission et faire quelque chose quand on clique dessus
        Button submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = prenomField.getText().toString();
                String nom = nomField.getText().toString();
                String anniversaire = anniversaireField.getText().toString();
                String telephone = telephoneField.getText().toString();
                String mail = mailField.getText().toString();
                ArrayList<String> interests = new ArrayList<>();

                if (musiqueCheckbox.isChecked()) {
                    interests.add("Musique");
                }
                if (lectureCheckbox.isChecked()) {
                    interests.add("Lecture");
                }
                if (sportsCheckbox.isChecked()) {
                    interests.add("Sport");
                }
                if (photoCheckbox.isChecked()) {
                    interests.add("Photo");
                }
                if (dessinCheckbox.isChecked()) {
                    interests.add("Dessin");
                }
                if (langueCheckbox.isChecked()) {
                    interests.add("Langue");
                }
                boolean sync = false;

                if (syncCheckbox.isChecked()) {
                    sync = true;
                }

                Bundle bundle = new Bundle();
                bundle.putString("prenom", prenom);
                bundle.putString("nom", nom);
                bundle.putString("anniversaire", anniversaire);
                bundle.putString("telephone", telephone);
                bundle.putString("mail", mail);
                bundle.putStringArrayList("interests", interests);
                bundle.putBoolean("sync", sync);

                Fragment2 fragment2 = new Fragment2();
                fragment2.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .replace(R.id.fragment_container, fragment2)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Même chose pour le outon de telechargement
        Button telechargerButton = view.findViewById(R.id.telecharger_button);
        telechargerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadFileTask().execute("https://jsonplaceholder.typicode.com/todos/1");
            }
        });

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadFileTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                response = convertInputStreamToString(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Traiter le résultat ici
            Bundle bundle = new Bundle();
            bundle.putString("fichierJAVA", result);

            // Creer le fragment 3 et ses arguments
            Fragment3 fragment3 = new Fragment3();
            fragment3.setArguments(bundle);

            // Aller au fragment 2
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment3)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }


    // Ecriture des données
    public void loadData() throws IOException, JSONException {
        FileInputStream fileInputStream = getActivity().openFileInput(FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        fileInputStream.close();
        String json = stringBuilder.toString();
        System.out.println("Contenu du fichier JSON dans le fichier "+ FILE_NAME + ": " + json);

        JSONObject jsonObject = new JSONObject(json);

        //taitement des données
        nomField.setText(jsonObject.getString("nom"));
        anniversaireField.setText(jsonObject.getString("anniversaire"));
    }


}