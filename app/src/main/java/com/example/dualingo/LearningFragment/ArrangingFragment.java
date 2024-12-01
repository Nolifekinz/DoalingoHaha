package com.example.dualingo.LearningFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.R;
import com.example.dualingo.databinding.FragmentArrangingBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangingFragment extends Fragment {

    private FragmentArrangingBinding binding;
    private AppDatabase database; // Room database instance
    private List<Arranging> arrangingList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;
    private int correctAnswersCount = 3;
    private int numberQuestion = 3;
    private List<Arranging> incorrectQuestions = new ArrayList<>(); // Dành cho câu trả lời sai

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArrangingBinding.inflate(inflater, container, false);
        database = AppDatabase.getDatabase(getContext()); // Initialize Room database
        setupRecyclerViews();
        loadArrangingDataFromRoom();

        binding.submitButton.setOnClickListener(v -> checkResult());

        return binding.getRoot();
    }

    private void setupRecyclerViews() {
        // Set up RecyclerView for word list
        binding.wordRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        binding.wordRecyclerView.setAdapter(wordAdapter);

        // Set up RecyclerView for result bar
        binding.resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        binding.resultRecyclerView.setAdapter(resultAdapter);

        // Add ItemTouchHelper to rearrange words in the result bar
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.resultRecyclerView);
    }

    private void loadArrangingDataFromRoom() {
        new Thread(() -> {
            String lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;

            arrangingList.clear();
            if (lectureId != null) {
                arrangingList.addAll(database.arrangingDAO().getArrangingByLectureId(lectureId));
                List<Arranging> randomQuestions = getRandomQuestions(arrangingList, 3);
                arrangingList.clear();
                arrangingList.addAll(randomQuestions);
            }

            if (!arrangingList.isEmpty()) {
                getActivity().runOnUiThread(this::showCurrentQuestion); // Cập nhật UI trên luồng chính
            } else {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "No questions found for this lecture!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private List<Arranging> getRandomQuestions(List<Arranging> questions, int count) {
        List<Arranging> randomQuestions = new ArrayList<>(questions);
        Collections.shuffle(randomQuestions);  // Xáo trộn danh sách
        return randomQuestions.subList(0, Math.min(count, randomQuestions.size()));  // Lấy 3 câu hỏi ngẫu nhiên
    }


    private void showCurrentQuestion() {
        if (currentQuestionIndex < arrangingList.size()) {
            Arranging currentQuestion = arrangingList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            selectedWords.clear();
            wordAdapter.notifyDataSetChanged();
            resultAdapter.notifyDataSetChanged();
            binding.questionTextView.setText(currentQuestion.getQuestion());
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
        boolean isCorrect = resultSentence.toString().trim().equals(correctAnswer);

        if (isCorrect) {
            arrangingList.remove(currentQuestionIndex);
            numberQuestion--;
            showResultDialog("Correct!", "Your answer is correct!", false);
        } else {
            if(numberQuestion!=0){
                correctAnswersCount--;
            }
            numberQuestion--;
            arrangingList.add(arrangingList.get(currentQuestionIndex));
            arrangingList.remove(currentQuestionIndex);// Quay lại câu hỏi trước đó
            showResultDialog("Incorrect!", "Your answer is incorrect!", false);
        }


        // Kiểm tra số câu đúng chỉ với 3 câu hỏi
        if (arrangingList.isEmpty()) {
            String finalMessage = "Quiz Completed!\nCorrect answers: " + correctAnswersCount + " / 3";
            showResultDialog("Quiz Completed", finalMessage, true);
        }
    }



    private void showResultDialog(String title, String message, boolean isFinalResult) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.result_dialog);
        dialog.setCancelable(false);

        TextView tvTitle = dialog.findViewById(R.id.dialog_title);
        TextView tvMessage = dialog.findViewById(R.id.dialog_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (isFinalResult) {

                showResultDialog("Quiz Completed", message, true);
            } else {
                showCurrentQuestion();
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
