package com.example.dompetku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityInputPengeluaran extends AppCompatActivity {
    DatabaseHelper db;
    EditText etAmount;
    Button btnSubmit;
    String userEmail, userName, selectedKategori;
    TextView tvName, tvEmail, tvAv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pengeluaran);

        db = new DatabaseHelper(this);

        etAmount = findViewById(R.id.etAmountFinal);
        btnSubmit = findViewById(R.id.btnSubmitFinal);
        tvName = findViewById(R.id.tvNameInputOut);
        tvEmail = findViewById(R.id.tvEmailInputOut);
        tvAv = findViewById(R.id.tvAvInputOut);

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userName = getIntent().getStringExtra("USER_NAME");
        selectedKategori = getIntent().getStringExtra("KATEGORI");

        if (userName != null && !userName.isEmpty()) {
            tvName.setText(userName);
            tvAv.setText(String.valueOf(userName.charAt(0)).toUpperCase());
        }
        if (userEmail != null) tvEmail.setText(userEmail);

        btnSubmit.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();

            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Mohon isi nominal!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                double currentBalance = db.getBalance(userEmail);
                if (amount > currentBalance) {
                    Toast.makeText(this, "Saldo tidak cukup!", Toast.LENGTH_LONG).show();
                    return;
                }

                SimpleDateFormat sdfDb = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateForDb = sdfDb.format(new Date());

                boolean success = db.insertTransaction(userEmail, amount, "OUT", dateForDb, selectedKategori);

                if (success) {
                    Toast.makeText(this, "Pengeluaran " + selectedKategori + " tercatat!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("USER_EMAIL", userEmail);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Input harus angka", Toast.LENGTH_SHORT).show();
            }
        });
    }
}