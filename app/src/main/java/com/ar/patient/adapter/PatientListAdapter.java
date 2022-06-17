package com.ar.patient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.R;
import com.ar.patient.databinding.RowPatientsBinding;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.patientsresponse.Type;

import java.util.ArrayList;


public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.CardHolder> {

    private Context context;
    private ArrayList<Type> list = new ArrayList<>();
    private Listner callback;
    private RowPatientsBinding binding;
    private VitalsListAdapter vitalsListAdapter;

    public PatientListAdapter(Context context, Listner callback) {
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_patients, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        Type bean = list.get(position);

        if (!bean.getVoiceExamCounter().equalsIgnoreCase("0")) {
            holder.binding.relMain.setVisibility(View.VISIBLE);
        } else {
            holder.binding.relMain.setVisibility(View.GONE);
        }

        holder.binding.tvGender.setText(bean.getPtName());
        holder.binding.tvName.setText(bean.getName());
        Utils.loadImage(context, bean.getAvatar(), R.drawable.avtar, holder.binding.ImgProfile);

        holder.binding.txtSymptoms.setText(Utils.join(",", bean.getSymptoms()));

        holder.binding.recVitals.setLayoutManager(new LinearLayoutManager(context));
        vitalsListAdapter = new VitalsListAdapter(context, new VitalsListAdapter.Listner() {
            @Override
            public void onClick(int value) {
                callback.onClick(bean);
            }
        });
        holder.binding.recVitals.setAdapter(vitalsListAdapter);
        vitalsListAdapter.addAll(bean.getVitalSigns());


        holder.binding.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(bean);
            }
        });
        holder.binding.recVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(bean);
            }
        });

    }

    public void addAll(ArrayList<Type> types) {
        this.list.clear();
        this.list.addAll(types);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<Type> timezoneArrays) {
        list = timezoneArrays;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listner {
        void onClick(Type value);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowPatientsBinding binding;

        public CardHolder(@NonNull RowPatientsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
