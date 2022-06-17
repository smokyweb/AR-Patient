package com.ar.patient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.R;
import com.ar.patient.databinding.RowExamListBinding;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.myprofile.Test;

import java.util.ArrayList;


public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.CardHolder> {

    private Context context;
    private ArrayList<Test> list = new ArrayList<>();
    private Listner callback;
    private RowExamListBinding binding;
    private int pStatus = 0;
    private Handler handler = new Handler();

    public ExamListAdapter(Context context, Listner callback) {
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTest) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_exam_list, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        Test bean = list.get(position);

        holder.binding.tvName.setText(bean.getPatientName());
        holder.binding.txtPatientType.setText(bean.getPatientType());
        Utils.loadImage(context, bean.getPatientAvatar(), R.drawable.avtar, holder.binding.ImgProfile);

        holder.binding.progressBar.setProgress((int) Math.round(Double.parseDouble(bean.getResult())));
        holder.binding.txtResult.setText(bean.getResult() + " %");

        holder.binding.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(bean);
            }
        });

    }

    public void addAll(ArrayList<Test> Tests) {
        this.list.clear();
        this.list.addAll(Tests);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listner {
        void onClick(Test value);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowExamListBinding binding;

        public CardHolder(@NonNull RowExamListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
