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
import com.ar.patient.databinding.RowLeaderboardBinding;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.leaderboardresponse.Student;

import java.util.ArrayList;


public class LeaderboardListAdapter extends RecyclerView.Adapter<LeaderboardListAdapter.CardHolder> {

    private Context context;
    private ArrayList<Student> list = new ArrayList<>();
    private Listner callback;
    private RowLeaderboardBinding binding;
    private VitalsListAdapter vitalsListAdapter;

    public LeaderboardListAdapter(Context context, Listner callback) {
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewStudent) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_leaderboard, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        Student bean = list.get(position);

        holder.binding.tvName.setText(bean.getName());
        holder.binding.txtPercentage.setText(bean.getResult() + "%");
        Utils.loadImage(context, bean.getAvatar(), R.drawable.avtar, holder.binding.ImgProfile);
        holder.binding.txtNumber.setText("" + (position + 1));

        holder.binding.RelMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(bean);
            }
        });
    }

    public void addAll(ArrayList<Student> Students) {
        this.list.clear();
        list.addAll(Students);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listner {
        void onClick(Student value);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowLeaderboardBinding binding;

        public CardHolder(@NonNull RowLeaderboardBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
