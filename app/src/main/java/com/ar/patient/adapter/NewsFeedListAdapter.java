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
import com.ar.patient.databinding.RowNewsFeedBinding;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.Newsfeed;

import java.util.ArrayList;


public class NewsFeedListAdapter extends RecyclerView.Adapter<NewsFeedListAdapter.CardHolder> {

    private Context context;
    private ArrayList<Newsfeed> list = new ArrayList<>();
    private Listner callback;
    private RowNewsFeedBinding binding;

    public NewsFeedListAdapter(Context context, Listner callback) {
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewNewsfeed) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_news_feed, parent, false);
        return new CardHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, final int position) {
        Newsfeed bean = list.get(position);

        holder.binding.tvName.setText(bean.getTitle());
        holder.binding.txtSymptoms.setText(bean.getDescription());
        Utils.loadImageWithoutMediaUrl(context, bean.getImage(), R.drawable.avtar, holder.binding.ImgProfile);

        holder.binding.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(bean);
            }
        });

    }

    public void addAll(ArrayList<Newsfeed> Newsfeeds) {
        this.list.clear();
        this.list.addAll(Newsfeeds);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listner {
        void onClick(Newsfeed value);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        RowNewsFeedBinding binding;

        public CardHolder(@NonNull RowNewsFeedBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
