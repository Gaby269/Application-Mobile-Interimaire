package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NoInternetActivity extends AppCompatActivity {

    String activityToGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        Intent i = getIntent();
        activityToGo = i.getStringExtra("activity"); // MainActivity ou NavbarActivity

        Button retryButton = findViewById(R.id.retry_button);
        retryButton.setOnClickListener(view -> checkConnection());
    }


    private void checkConnection() {
        // Vérification de la connectivité internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            if (activityToGo.equals("MainActivity")) {
                Intent i = new Intent(NoInternetActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            else if (activityToGo.equals("NavbarActivity")) {
                Intent i = new Intent(NoInternetActivity.this, NavbarActivity.class);
                startActivity(i);
                finish();
            }
        }
        // Affichage d'un message si pas de connexion internet
        else {
            Toast.makeText(NoInternetActivity.this, "Pas de connexion internet", Toast.LENGTH_SHORT).show();
        }
    }
}