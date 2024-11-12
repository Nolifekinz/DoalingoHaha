package com.example.dualingo.LearningFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.databinding.FragmentArrangingBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangingFragment extends Fragment {

    private FragmentArrangingBinding binding;
    private FirebaseFirestore db;
    private List<Arranging> arrangingList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArrangingBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        setupRecyclerViews();
        loadArrangingData();

        binding.submitButton.setOnClickListener(v -> checkResult());

        return binding.getRoot();
    }

    private void setupRecyclerViews() {
        // Set up RecyclerView for word list
        binding.wordRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        binding.wordRecyclerView.setAdapter(wordAdapter);

        // Set up RecyclerView for result bar
        binding.resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        binding.resultRecyclerView.setAdapter(resultAdapter);

        // Add ItemTouchHelper to rearrange words in the result bar
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
                showCurrentQuestion(); // Display the first question after loading data
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
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            currentQuestionIndex++; // Move to the next question
            if (currentQuestionIndex < arrangingList.size()) {
                showCurrentQuestion();
            } else {
                Toast.makeText(getContext(), "You've completed all questions!", Toast.LENGTH_LONG).show();
                // Logic for when the user completes all questions
            }
        } else {
            Toast.makeText(getContext(), "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
