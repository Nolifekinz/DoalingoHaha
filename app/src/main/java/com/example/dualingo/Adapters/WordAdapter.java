package com.example.dualingo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.R;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<String> wordList;
    private Context context;
    private OnWordClickListener onWordClickListener;

    public WordAdapter(Context context, List<String> wordList, OnWordClickListener onWordClickListener) {
        this.context = context;
        this.wordList = wordList;
        this.onWordClickListener = onWordClickListener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.word_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.wordTextView.setText(wordList.get(position));

        // Gọi callback khi nhấn vào từ
        holder.itemView.setOnClickListener(v -> onWordClickListener.onWordClick(wordList.get(position)));
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
        }
    }

    public interface OnWordClickListener {
        void onWordClick(String word);
    }
}
