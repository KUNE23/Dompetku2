package com.example.dompetku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RiwayatFilterActivity extends AppCompatActivity {
    String userEmail, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_filter);

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userName = getIntent().getStringExtra("USER_NAME");

        setupHeader();

        Button btnSebulan = findViewById(R.id.btnSebulan);
        Button btnEnamBulan = findViewById(R.id.btnEnamBulan);
        Button btnSetahun = findViewById(R.id.btnSetahun);
        Button btnMenu = findViewById(R.id.btnMenuFilter);

        btnSebulan.setOnClickListener(v -> {
            String startDate = hitungTanggalMundur(Calendar.MONTH, -1);
            bukaHalamanChart(startDate, "Sebulan Terakhir");
        });

        btnEnamBulan.setOnClickListener(v -> {
            String startDate = hitungTanggalMundur(Calendar.MONTH, -6);
            bukaHalamanChart(startDate, "6 Bulan Terakhir");
        });

        btnSetahun.setOnClickListener(v -> {
            String startDate = hitungTanggalMundur(Calendar.YEAR, -1);
            bukaHalamanChart(startDate, "Setahun Terakhir");
        });

        btnMenu.setOnClickListener(v -> finish());
    }

    private String hitungTanggalMundur(int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.add(field, amount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    private void setupHeader() {
        TextView tvName = findViewById(R.id.tvNameFilter);
        TextView tvEmail = findViewById(R.id.tvEmailFilter);
        TextView tvAv = findViewById(R.id.tvAvFilter);

        if (userName != null && !userName.isEmpty()) {
            tvName.setText(userName);
            tvAv.setText(String.valueOf(userName.charAt(0)).toUpperCase());
        }
        if (userEmail != null) tvEmail.setText(userEmail);
    }

    private void bukaHalamanChart(String startDate, String judulFilter) {
        Intent intent = new Intent(RiwayatFilterActivity.this, PengeluaranChartActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("START_DATE", startDate);
        intent.putExtra("JUDUL_FILTER", judulFilter);
        startActivity(intent);
    }
}