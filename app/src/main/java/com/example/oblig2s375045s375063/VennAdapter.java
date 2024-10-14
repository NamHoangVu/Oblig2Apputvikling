package com.example.oblig2s375045s375063;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VennAdapter extends RecyclerView.Adapter<VennAdapter.ViewHolder> {
    private List<Venn> VennList;
    private OnVennClickListener listener; // Define the listener interface
    public interface OnVennClickListener {
        void onItemClick(Venn venn);
    }
    public VennAdapter(List<Venn> itemList, OnVennClickListener listener) {
        this.VennList = itemList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venn_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venn venn = VennList.get(position);
        holder.navn.setText(venn.getNavn());
        holder.telefon.setText(venn.getTelefon());
        holder.bursdag.setText(venn.getBursdag());
    }
    @Override
    public int getItemCount() {
        return VennList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView navn;
        TextView telefon;
        TextView bursdag;
        public ViewHolder(View itemView) {
            super(itemView);
            navn = itemView.findViewById(R.id.navn);
            telefon = itemView.findViewById(R.id.telefon);
            bursdag = itemView.findViewById(R.id.bursdag);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(VennList.get(position)); // Trigger the onItemClick event
                    }
                }
            });
        }
    }
}


