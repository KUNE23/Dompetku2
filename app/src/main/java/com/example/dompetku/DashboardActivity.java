package com.example.dompetku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton; // Import baru
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    String userEmail;
    String userName;
    DatabaseHelper db;
    TextView tvAvatar, tvUserName, tvUserEmail, tvSaldoDisplay;
    Button btnPemasukan, btnPengeluaran, btnRiwayat;
    ImageButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = new DatabaseHelper(this);

        tvAvatar = findViewById(R.id.tvAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvSaldoDisplay = findViewById(R.id.tvSaldoDisplay);
        btnPemasukan = findViewById(R.id.btnPemasukan);
        btnPengeluaran = findViewById(R.id.btnPengeluaran);
        btnRiwayat = findViewById(R.id.btnRiwayat);
        btnLogout = findViewById(R.id.btnLogout);

        userName = getIntent().getStringExtra("USER_NAME");
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        if (userName != null && userEmail != null) {
            tvUserName.setText(userName);
            tvUserEmail.setText(userEmail);
            String initial = String.valueOf(userName.charAt(0)).toUpperCase();
            tvAvatar.setText(initial);
        }

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(DashboardActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashboardActivity.this, SignInActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnPemasukan.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PemasukanActivity.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });

        btnPengeluaran.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PengeluaranActivity.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });

        btnRiwayat.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RiwayatFilterActivity.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null && userEmail != null) {
            double balance = db.getBalance(userEmail);
            tvSaldoDisplay.setText("Rp." + String.format("%.0f", balance));
        }
    }
}