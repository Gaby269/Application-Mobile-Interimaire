package com.example.gpgh_interimaire;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutoActivity extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto);


        Button passerButton = findViewById(R.id.boutton_passer);
        passerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TutoActivity.this, OffresActivity.class);
                startActivity(i);
            }
        });
    }
}