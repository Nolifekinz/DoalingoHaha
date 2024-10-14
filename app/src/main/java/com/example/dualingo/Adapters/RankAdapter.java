package com.example.dualingo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Models.Rank;
import com.example.dualingo.R;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private List<Rank> rankList;

    public RankAdapter(List<Rank> rankList) {
        this.rankList = rankList;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        Rank rank = rankList.get(position);
        holder.tvRank.setText(rank.getName());
        // Load hình ảnh rank từ URL hoặc từ drawable
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    class RankViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRank;
        TextView tvRank;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRank = itemView.findViewById(R.id.imgRank);
            tvRank = itemView.findViewById(R.id.tvRank);
        }
    }
}
