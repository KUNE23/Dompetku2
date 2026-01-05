package com.example.dompetku;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PengeluaranChartActivity extends AppCompatActivity {
    BarChart barChart;
    DatabaseHelper db;
    String userEmail, userName, startDateFilter, judulFilter;
    Button btnBackToHome;
    NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran_chart);
        Button btnBackToHome = findViewById(R.id.btnBackToHome);

        btnBackToHome.setOnClickListener(v -> {
             Intent intent = new Intent(PengeluaranChartActivity.this, DashboardActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
             startActivity(intent);
        });
        db = new DatabaseHelper(this);
        barChart = findViewById(R.id.barChart);

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userName = getIntent().getStringExtra("USER_NAME");
        startDateFilter = getIntent().getStringExtra("START_DATE");
        judulFilter = getIntent().getStringExtra("JUDUL_FILTER");

        setupHeader();
        loadChartData();
    }

    private void setupHeader() {
        TextView tvName = findViewById(R.id.tvNameChart);
        TextView tvAv = findViewById(R.id.tvAvChart);
        TextView tvSubtitle = findViewById(R.id.tvSubtitleChart);
        TextView tvEmail = findViewById(R.id.tvEmailChart);

        if (userName != null && !userName.isEmpty()) {
            tvName.setText(userName);
            tvAv.setText(String.valueOf(userName.charAt(0)).toUpperCase());
        }

        if (userEmail != null && !userEmail.isEmpty()) {
            tvEmail.setText(userEmail);
        }

        if (judulFilter != null) {
            tvSubtitle.setText(judulFilter);
        }
    }

    private void loadChartData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        Cursor cursor = db.getTotalPengeluaranPerKategori(userEmail, startDateFilter);

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String kategori = cursor.getString(0);
                float total = cursor.getFloat(1);

                entries.add(new BarEntry(index, total));
                labels.add(kategori);

                if (kategori.equalsIgnoreCase("Makan")) colors.add(Color.parseColor("#6366F1"));
                else if (kategori.equalsIgnoreCase("Transport") || kategori.equalsIgnoreCase("Transportasi"))
                    colors.add(Color.parseColor("#EC4899"));
                else colors.add(Color.parseColor("#22C55E"));

                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();



        if (entries.isEmpty()) {
            barChart.setNoDataText("Belum ada pengeluaran di periode ini.");
            barChart.invalidate();
            return;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Kategori");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return rupiahFormat.format((double) value);
            }
        });

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}