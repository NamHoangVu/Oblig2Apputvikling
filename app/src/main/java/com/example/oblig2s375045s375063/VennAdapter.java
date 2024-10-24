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
<<<<<<< Updated upstream
    private OnVennClickListener listener; // Define the listener interface
=======
    private OnVennClickListener listener; // Interface for klikkhendelse
>>>>>>> Stashed changes

    public interface OnVennClickListener {
        void onItemClick(Venn venn);
    }

<<<<<<< Updated upstream
    public VennAdapter(List<Venn> vennList, OnVennClickListener listener) {
        this.vennList = vennList;
=======
    public VennAdapter(List<Venn> itemList, OnVennClickListener listener) {
        this.vennList = itemList;
>>>>>>> Stashed changes
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_venn, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venn venn = vennList.get(position);
<<<<<<< Updated upstream
        holder.bind(venn, listener);
=======
        holder.navn.setText(venn.getNavn());
        holder.telefon.setText(venn.getTelefon());
        holder.bursdag.setText(venn.getBursdag());
>>>>>>> Stashed changes
    }

    @Override
    public int getItemCount() {
        return vennList.size();
    }

<<<<<<< Updated upstream
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
                        Intent intent = new Intent(view.getContext(), EditVenn.class);
                        intent.putExtra("vennId", venn.getId());
                        intent.putExtra("navn", venn.getNavn());
                        intent.putExtra("telefon", venn.getTelefon());
                        intent.putExtra("bursdag", venn.getBursdag());
                        view.getContext().startActivity(intent);
                    }
=======
    public void updateVennListe(List<Venn> nyVennListe) {
        this.vennList.clear();
        this.vennList.addAll(nyVennListe);
        notifyDataSetChanged();
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
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(vennList.get(position)); // Triggerer klikkhendelsen
                }
>>>>>>> Stashed changes
            });
        }

    }
}
