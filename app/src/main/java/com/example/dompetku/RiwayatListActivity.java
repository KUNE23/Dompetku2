package com.example.dompetku;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RiwayatListActivity extends AppCompatActivity {
    DatabaseHelper db;
    RecyclerView recyclerView;
    TransaksiAdapter adapter;
    String userEmail, startDateFilter, judulFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_list);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.rvTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        startDateFilter = getIntent().getStringExtra("START_DATE");
        judulFilter = getIntent().getStringExtra("JUDUL_FILTER");

        TextView tvTitle = findViewById(R.id.tvTitleList);
        TextView tvSubtitle = findViewById(R.id.tvSubtitleList);
        tvTitle.setText("Riwayat: " + judulFilter);
        tvSubtitle.setText("Sejak tanggal: " + startDateFilter);

        loadData();
    }

    private void loadData() {
        List<Transaksi> listTransaksi = db.getTransactionsFiltered(userEmail, startDateFilter);

        if (listTransaksi.isEmpty()) {
            Toast.makeText(this, "Tidak ada transaksi pada periode ini.", Toast.LENGTH_SHORT).show();
        }

        adapter = new TransaksiAdapter(this, listTransaksi);
        recyclerView.setAdapter(adapter);
    }
}