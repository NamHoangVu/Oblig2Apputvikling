package com.example.oblig2s375045s375063;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VennAdapter extends RecyclerView.Adapter<VennAdapter.ViewHolder> {
    private List<Venn> vennList;
    private OnVennClickListener listener;

    public interface OnVennClickListener {
        void onItemClick(Venn venn);
    }

    public VennAdapter(List<Venn> vennList, OnVennClickListener listener) {
        this.vennList = vennList;
        this.listener = listener; // Lagrer lytteren
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venn_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venn venn = vennList.get(position);
        holder.bind(venn, listener); // Passer lytteren til bind-metoden
    }

    @Override
    public int getItemCount() {
        return vennList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView navnTextView, telefonTextView, bursdagTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            navnTextView = itemView.findViewById(R.id.navnTextView);
            telefonTextView = itemView.findViewById(R.id.telefonTextView);
            bursdagTextView = itemView.findViewById(R.id.bursdagTextView);
        }

        public void bind(final Venn venn, final OnVennClickListener listener) {
            navnTextView.setText(venn.getNavn());
            telefonTextView.setText(venn.getTelefon());
            bursdagTextView.setText(venn.getBursdag());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(venn); // Bruker lytteren
                }
            });
        }
    }
}



