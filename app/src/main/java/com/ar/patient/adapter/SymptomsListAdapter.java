package com.ar.patient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.R;
import com.ar.patient.databinding.RowDidWellBinding;

import java.util.ArrayList;


public class SymptomsListAdapter extends RecyclerView.Adapter<SymptomsListAdapter.CardHolder> {

    private Context context;
    private ArrayList<String> list = new ArrayList<>();
    private RowDidWellBinding binding;

    public SymptomsListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewDidWell) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_did_well, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        String bean = list.get(position);

        holder.binding.tvfaqtitle.setText(bean);
    }

    public void addAll(ArrayList<String> DidWells) {
        this.list.clear();
        this.list.addAll(DidWells);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowDidWellBinding binding;

        public CardHolder(@NonNull RowDidWellBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
