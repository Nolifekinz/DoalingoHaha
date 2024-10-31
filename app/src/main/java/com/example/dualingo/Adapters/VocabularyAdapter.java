package com.example.dualingo.Adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dualingo.Models.Vocabulary;
import com.example.dualingo.R;

import java.util.List;
import java.util.Locale;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder> {
    private List<Vocabulary> vocabularyList;
    private TextToSpeech textToSpeech;

    public VocabularyAdapter(List<Vocabulary> vocabularyList, Context context) {
        this.vocabularyList = vocabularyList;
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.SUCCESS) {
                // Xử lý khi khởi tạo không thành công
            } else {
                textToSpeech.setLanguage(Locale.ENGLISH); // Đặt ngôn ngữ
            }
        });
    }

    @NonNull
    @Override
    public VocabularyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
        return new VocabularyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabularyViewHolder holder, int position) {
        Vocabulary vocabulary = vocabularyList.get(position);
        holder.tvEnglishWord.setText(vocabulary.getEnglishWord());
        holder.tvVietnameseMeaning.setText(vocabulary.getVietnameseMeaning());
        holder.tvWordType.setText(vocabulary.getWordType());
        holder.tvExample.setText("Ví dụ: " + vocabulary.getExample());
        holder.tvPronunciation.setText("Cách phát âm: " + vocabulary.getPronunciation());

        // Hiển thị hoặc ẩn ví dụ dựa trên trạng thái mở rộng
        holder.tvExample.setVisibility(vocabulary.isExpanded() ? View.VISIBLE : View.GONE);

        // Xử lý sự kiện nhấn vào để mở rộng hoặc thu gọn ví dụ
        holder.itemView.setOnClickListener(v -> {
            vocabulary.setExpanded(!vocabulary.isExpanded());
            notifyItemChanged(position);
        });

        // Xử lý sự kiện nhấn vào nút loa
        holder.ivSpeak.setOnClickListener(v -> {
            speakWord(vocabulary.getEnglishWord());
        });
    }


    private void speakWord(String word) {
        if (textToSpeech != null) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public int getItemCount() {
        return vocabularyList.size();
    }

    public static class VocabularyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEnglishWord, tvVietnameseMeaning, tvWordType, tvExample,tvPronunciation;
        ImageView ivSpeak;

        public VocabularyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnglishWord = itemView.findViewById(R.id.tvEnglishWord);
            tvVietnameseMeaning = itemView.findViewById(R.id.tvVietnameseMeaning);
            tvWordType = itemView.findViewById(R.id.tvWordType);
            tvExample = itemView.findViewById(R.id.tvExample);
            ivSpeak = itemView.findViewById(R.id.ivSpeak);
            tvPronunciation= itemView.findViewById(R.id.tvPronunciation);
        }
    }

    // Giải phóng tài nguyên khi không còn sử dụng adapter
    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
