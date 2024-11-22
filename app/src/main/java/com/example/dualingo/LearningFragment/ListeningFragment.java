package com.example.dualingo.LearningFragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.Models.Listening;
import com.example.dualingo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ListeningFragment extends Fragment {

    private AppDatabase appDatabase;
    private List<Listening> listeningList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;

    private TextToSpeech tts;
    private TextView questionTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listening, container, false);

        questionTextView = view.findViewById(R.id.questionTextView);

        // Initialize Text-to-Speech
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        // Initialize Room Database
        appDatabase = AppDatabase.getDatabase(requireContext());
        setupRecyclerViews(view);
        loadListeningData();

        view.findViewById(R.id.listenButton).setOnClickListener(v -> readQuestionAloud());
        view.findViewById(R.id.submitButton).setOnClickListener(v -> checkResult());

        return view;
    }

    private void setupRecyclerViews(View view) {
        RecyclerView wordRecyclerView = view.findViewById(R.id.wordRecyclerView);
        RecyclerView resultRecyclerView = view.findViewById(R.id.resultRecyclerView);

        wordRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        wordRecyclerView.setAdapter(wordAdapter);

        resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        resultRecyclerView.setAdapter(resultAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(resultRecyclerView);
    }

    private void loadListeningData() {
        new Thread(() -> {
            // Lấy dữ liệu từ Room Database
            listeningList = appDatabase.listeningDAO().getAllListenings();

            // Hiển thị câu hỏi đầu tiên trên giao diện
            requireActivity().runOnUiThread(this::showCurrentQuestion);
        }).start();
    }

    private void showCurrentQuestion() {
        if (currentQuestionIndex < listeningList.size()) {
            Listening currentQuestion = listeningList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            selectedWords.clear();
            wordAdapter.notifyDataSetChanged();
            resultAdapter.notifyDataSetChanged();
            questionTextView.setText(currentQuestion.getQuestion());
        }
    }

    private void readQuestionAloud() {
        if (tts != null && currentQuestionIndex < listeningList.size()) {
            String question = listeningList.get(currentQuestionIndex).getQuestion();
            tts.speak(question, TextToSpeech.QUEUE_FLUSH, null, null);
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

        String correctAnswer = listeningList.get(currentQuestionIndex).getResult();
        if (resultSentence.toString().trim().equals(correctAnswer)) {
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            currentQuestionIndex++;
            if (currentQuestionIndex < listeningList.size()) {
                showCurrentQuestion();
            } else {
                Toast.makeText(getContext(), "You've completed all questions!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroyView();
    }
}
