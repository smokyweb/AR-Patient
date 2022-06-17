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
import com.ar.patient.databinding.RowVoiceExamQuestionListBinding;
import com.ar.patient.responsemodel.voiceexam.Definition;

import java.util.ArrayList;


public class VoiceExamQuestionListAdapter extends RecyclerView.Adapter<VoiceExamQuestionListAdapter.CardHolder> {

    private Context context;
    private ArrayList<Definition> list = new ArrayList<>();
    private Listner callback;
    private RowVoiceExamQuestionListBinding binding;

    public VoiceExamQuestionListAdapter(Context context, Listner callback) {
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewDefinition) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_voice_exam_question_list, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        Definition bean = list.get(position);

        holder.binding.tvfaqtitle.setText(bean.getQuestion());
        holder.binding.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(bean);
            }
        });

        if (bean.getIsMandatory().equalsIgnoreCase("1")) {
            holder.binding.imgStar.setVisibility(View.VISIBLE);
        } else {
            holder.binding.imgStar.setVisibility(View.GONE);
        }

    }

    public void addAll(ArrayList<Definition> Definitions) {
        this.list.clear();
        this.list.addAll(Definitions);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listner {
        void onClick(Definition value);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowVoiceExamQuestionListBinding binding;

        public CardHolder(@NonNull RowVoiceExamQuestionListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
