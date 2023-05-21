package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class TutoActivity extends AppCompatActivity {

    Fragment fragment;
    String tuto;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto);

        Bundle args = new Bundle();
        args.putString("tuto", tuto);
        if (tuto == null){
            tuto = "1";
        }

        // Gérer les fragment pour savoir lequel afficher
        switch (tuto) {
            case "2":
                fragment = new FragTuto2();
                break;
            case "3":
                fragment = new FragTuto3();
                break;
            case "4":
                fragment = new FragTuto4();
                break;
            case "5":
                fragment = new FragTuto5();
                break;
            default:
                fragment = new FragTuto1();
                break;
        }

        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_tuto, fragment);
        transaction.addToBackStack(null); // ajouter à la pile de retour
        transaction.commit();


        Button passerButton = findViewById(R.id.boutton_passer);
        passerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TutoActivity.this, LoadingNavbarActivity.class);
                startActivity(i);
            }
        });

        Button pastButton = findViewById(R.id.boutton_past);
        if (tuto.equals("1")){
            pastButton.setVisibility(View.GONE);
        }
        pastButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                fragment.setArguments(args);
                switch (tuto) {
                    case "2":
                        fragment = new FragTuto1();
                        tuto = "1";
                        pastButton.setVisibility(View.GONE);
                        break;
                    case "3":
                        fragment = new FragTuto2();
                        tuto = "2";
                        pastButton.setVisibility(View.VISIBLE);
                        break;
                    case "4":
                        fragment = new FragTuto3();
                        tuto = "3";
                        pastButton.setVisibility(View.VISIBLE);
                        break;
                    case "5":
                        fragment = new FragTuto4();
                        tuto = "4";
                        pastButton.setVisibility(View.VISIBLE);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_tuto, fragment);
                transaction.addToBackStack(null); // ajouter à la pile de retour
                transaction.commit();
            }

        });

        Button nextButton = findViewById(R.id.boutton_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (tuto == "5"){
                    Intent i = new Intent(TutoActivity.this, LoadingNavbarActivity.class);
                    startActivity(i);
                }
                else {
                    fragment.setArguments(args);
                    switch (tuto) {
                        case "1":
                            fragment = new FragTuto2();
                            tuto = "2";
                            pastButton.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            fragment = new FragTuto3();
                            tuto = "3";
                            pastButton.setVisibility(View.VISIBLE);
                            break;
                        case "3":
                            fragment = new FragTuto4();
                            tuto = "4";
                            pastButton.setVisibility(View.VISIBLE);
                            break;
                        case "4":
                            fragment = new FragTuto5();
                            tuto = "5";
                            pastButton.setVisibility(View.VISIBLE);
                            break;
                    }

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_tuto, fragment);
                    transaction.addToBackStack(null); // ajouter à la pile de retour
                    transaction.commit();
                }
            }
        });
    }
}