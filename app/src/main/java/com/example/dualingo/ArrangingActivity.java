package com.example.dualingo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.databinding.ActivityArrangingBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangingActivity extends AppCompatActivity {

    private ActivityArrangingBinding binding;
    private FirebaseFirestore db;
    private List<Arranging> arrangingList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArrangingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        setupRecyclerViews();
        loadArrangingData();

        binding.submitButton.setOnClickListener(v -> checkResult());
    }

    private void setupRecyclerViews() {
        // Thiết lập RecyclerView cho danh sách từ
        binding.wordRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        wordAdapter = new WordAdapter(this, wordList, this::onWordClicked);
        binding.wordRecyclerView.setAdapter(wordAdapter);

        // Thiết lập RecyclerView cho thanh kết quả
        binding.resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        resultAdapter = new WordAdapter(this, selectedWords, this::onResultWordClicked);
        binding.resultRecyclerView.setAdapter(resultAdapter);

        // Thêm ItemTouchHelper để sắp xếp từ trong thanh kết quả
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.resultRecyclerView);
    }

    private void loadArrangingData() {
        db.collection("Arranging").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                arrangingList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Arranging arranging = document.toObject(Arranging.class);
                    arrangingList.add(arranging);
                }
                showCurrentQuestion(); // Hiển thị câu hỏi đầu tiên sau khi tải dữ liệu
            }
        });
    }

    private void showCurrentQuestion() {
        if (currentQuestionIndex < arrangingList.size()) {
            Arranging currentQuestion = arrangingList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            selectedWords.clear();
            wordAdapter.notifyDataSetChanged();
            resultAdapter.notifyDataSetChanged();
            binding.tvQuestion.setText(currentQuestion.getQuestion());
        }
    }

    private void onWordClicked(String word) {
        wordList.remove(word);
        wordAdapter.notifyDataSetChanged();

        selectedWords.add(word);
        resultAdapter.notifyDataSetChanged();
    }

    private void onResultWordClicked(String word) {
        selectedWords.remove(word);
        resultAdapter.notifyDataSetChanged();

        wordList.add(word);
        wordAdapter.notifyDataSetChanged();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(selectedWords, fromPosition, toPosition);
            resultAdapter.notifyItemMoved(fromPosition, toPosition);

            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}
    };

    private void checkResult() {
        StringBuilder resultSentence = new StringBuilder();
        for (String word : selectedWords) {
            resultSentence.append(word).append(" ");
        }

        String correctAnswer = arrangingList.get(currentQuestionIndex).getResult();
        if (resultSentence.toString().trim().equals(correctAnswer)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            currentQuestionIndex++; // Chuyển sang câu hỏi tiếp theo
            if (currentQuestionIndex < arrangingList.size()) {
                showCurrentQuestion();
            } else {
                Toast.makeText(this, "You've completed all questions!", Toast.LENGTH_LONG).show();
                // Có thể thêm logic khi người dùng hoàn thành toàn bộ câu hỏi
            }
        } else {
            Toast.makeText(this, "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
