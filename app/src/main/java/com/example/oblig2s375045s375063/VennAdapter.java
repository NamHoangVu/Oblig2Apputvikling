package com.example.oblig2s375045s375063;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VennAdapter extends RecyclerView.Adapter<VennAdapter.VennViewHolder> {
    private List<Venn> venner;

    public static class VennViewHolder extends RecyclerView.ViewHolder {
        public TextView navnView;
        public TextView telefonView;
        public TextView bursdagView;

        public VennViewHolder(View itemView) {
            super(itemView);
            navnView = itemView.findViewById(R.id.navn);
            telefonView = itemView.findViewById(R.id.telefon);
            bursdagView = itemView.findViewById(R.id.bursdag);
        }
    }

    public VennAdapter(List<Venn> venner) {
        this.venner = venner;
    }

    @Override
    public VennViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.venn_item, parent, false);
        return new VennViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VennViewHolder holder, int position) {
        Venn venn = venner.get(position);
        holder.navnView.setText(venn.getNavn());
        holder.telefonView.setText(venn.getTelefon());
        holder.bursdagView.setText(venn.getBursdag());
    }

    @Override
    public int getItemCount() {
        return venner.size();
    }
}
