package com.ar.patient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.R;
import com.ar.patient.databinding.RowFaqListBinding;
import com.ar.patient.responsemodel.Faq;

import java.util.ArrayList;


public class FAQListAdapter extends RecyclerView.Adapter<FAQListAdapter.CardHolder> {

    private Context context;
    private ArrayList<Faq> list = new ArrayList<>();
    private Listner callback;
    private RowFaqListBinding binding;

    public FAQListAdapter(Context context, Listner callback) {
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewFaq) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_faq_list, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        Faq bean = list.get(position);

        holder.binding.tvfaqtitle.setText(bean.getTitle());
        holder.binding.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(bean);
            }
        });

    }

    public void addAll(ArrayList<Faq> Faqs) {
        this.list.clear();
        this.list.addAll(Faqs);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listner {
        void onClick(Faq value);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowFaqListBinding binding;

        public CardHolder(@NonNull RowFaqListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
