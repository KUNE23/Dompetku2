package com.example.dompetku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

public class PemasukanActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etAmount, etDate, etDesc;
    Button btnSubmit;
    String userEmail, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemasukan);

        db = new DatabaseHelper(this);

        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etDesc = findViewById(R.id.etDesc);
        btnSubmit = findViewById(R.id.btnSubmit);

        TextView tvNameIn = findViewById(R.id.tvNameIn);
        TextView tvEmailIn = findViewById(R.id.tvEmailIn);
        TextView tvAv = findViewById(R.id.tvAv);

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userName = getIntent().getStringExtra("USER_NAME");

        tvNameIn.setText(userName);
        tvEmailIn.setText(userEmail);
        tvAv.setText(String.valueOf(userName.charAt(0)).toUpperCase());

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String today = day + "/" + (month + 1) + "/" + year;
        etDate.setText(today);

        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(PemasukanActivity.this,
                    (view, yearSelected, monthSelected, daySelected) -> {
                        String date = daySelected + "/" + (monthSelected + 1) + "/" + yearSelected;
                        etDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnSubmit.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (amountStr.isEmpty() || date.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double amount = Double.parseDouble(amountStr);

                    boolean isInserted = db.insertTransaction(userEmail, amount, "IN", date, desc);

                    if (isInserted) {
                        Toast.makeText(this, "Saldo Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Gagal menyimpan ke database", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Nominal harus berupa angka!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}