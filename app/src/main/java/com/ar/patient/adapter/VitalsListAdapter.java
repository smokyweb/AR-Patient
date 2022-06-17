package com.ar.patient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.R;
import com.ar.patient.databinding.RowVitalsBinding;
import com.ar.patient.responsemodel.patientsresponse.VitalSign;

import java.util.ArrayList;


public class VitalsListAdapter extends RecyclerView.Adapter<VitalsListAdapter.CardHolder> {

    private Context context;
    private ArrayList<VitalSign> list = new ArrayList<>();
    private Listner callback;
    private RowVitalsBinding binding;
    private Fragment fragment;

    public VitalsListAdapter(Context context, Listner callback) {
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_vitals, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        VitalSign bean = list.get(position);

        holder.binding.txtvitalsName.setText(bean.getVitalSign() + ": ");
        holder.binding.txtvitalsvalue.setText(bean.getValue());
    }

    public void addAll(ArrayList<VitalSign> types) {
        this.list.clear();
        this.list.addAll(types);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listner {
        void onClick(int value);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowVitalsBinding binding;

        public CardHolder(@NonNull RowVitalsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
