package com.example.dompetku;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {

    private Context context;
    private List<Transaksi> transaksiList;

    NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public TransaksiAdapter(Context context, List<Transaksi> transaksiList) {
        this.context = context;
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaksi transaction = transaksiList.get(position);

        holder.tvDesc.setText(transaction.getDescription());
        holder.tvDate.setText(transaction.getDate());

        double amount = transaction.getAmount();
        if (transaction.getType().equals("IN")) {
            holder.tvAmount.setTextColor(Color.parseColor("#22C55E"));
            holder.tvAmount.setText("+ " + rupiahFormat.format(amount));
        } else {
            holder.tvAmount.setTextColor(Color.parseColor("#EF4444"));
            holder.tvAmount.setText("- " + rupiahFormat.format(amount));
        }
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDesc, tvDate, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.tvItemDesc);
            tvDate = itemView.findViewById(R.id.tvItemDate);
            tvAmount = itemView.findViewById(R.id.tvItemAmount);
        }
    }
}