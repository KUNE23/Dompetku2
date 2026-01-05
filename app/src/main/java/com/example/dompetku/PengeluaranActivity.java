package com.example.dompetku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PengeluaranActivity extends AppCompatActivity {
    String userEmail, userName;
    TextView tvName, tvEmail, tvAv;
    Button btnMakan, btnTransport, btnOpsional, btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);

        tvName = findViewById(R.id.tvNameOut);
        tvEmail = findViewById(R.id.tvEmailOut);
        tvAv = findViewById(R.id.tvAvOut);

        btnMakan = findViewById(R.id.btnMakan);
        btnTransport = findViewById(R.id.btnTransport);
        btnOpsional = findViewById(R.id.btnOpsional);
        btnMenu = findViewById(R.id.btnMenu);

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userName = getIntent().getStringExtra("USER_NAME");

        if (userName != null) {
            tvName.setText(userName);
            tvAv.setText(String.valueOf(userName.charAt(0)).toUpperCase());
        }
        if (userEmail != null) tvEmail.setText(userEmail);

        btnMakan.setOnClickListener(v -> menujuInputNominal("Makan"));

        btnTransport.setOnClickListener(v -> menujuInputNominal("Transport"));

        btnOpsional.setOnClickListener(v -> menujuInputNominal("Lainnya"));

        btnMenu.setOnClickListener(v -> finish());
    }

    private void menujuInputNominal(String kategori) {
        Intent intent = new Intent(PengeluaranActivity.this, ActivityInputPengeluaran.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("KATEGORI", kategori);
        startActivity(intent);
    }
}