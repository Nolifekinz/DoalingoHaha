package com.example.dualingo.TestFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Listening;
import com.example.dualingo.Models.Speaking;
import com.example.dualingo.Models.User;
import com.example.dualingo.OnQuestionCompletedListener;
import com.example.dualingo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TestListenFragment extends Fragment {

    private OnQuestionCompletedListener callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Đảm bảo rằng Activity thực hiện giao diện OnQuestionCompletedListener
        if (context instanceof OnQuestionCompletedListener) {
            callback = (OnQuestionCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnQuestionCompletedListener");
        }
    }

    private void completeQuestion() {
        if (callback != null) {
            callback.onQuestionCompleted(dung);  // Gọi callback khi câu hỏi hoàn thành
        }
    }

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;

    private Listening listening;

    private TextToSpeech tts;

    private boolean dung = false;

    RecyclerView wordRecyclerView , resultRecyclerView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_listen, container, false);

        wordRecyclerView = view.findViewById(R.id.TestListenresultRecyclerView);
        resultRecyclerView = view.findViewById(R.id.TestListenwordRecyclerView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            listening = (Listening) bundle.getSerializable("questionData"); // Hoặc `getParcelable`
            if (listening != null) {
                System.out.println("nonull");
            }else{
                System.out.println("null");
            }
        }

        setupRecyclerViews();
        showCurrentQuestion();

        // Initialize Text-to-Speech
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        view.findViewById(R.id.TestlistenButton).setOnClickListener(v -> readQuestionAloud());
        view.findViewById(R.id.TestListensubmitButton).setOnClickListener(v -> {
            checkResult();
            completeQuestion();
        });

        return view ;
    }

    private void setupRecyclerViews() {
        wordRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        wordRecyclerView.setAdapter(wordAdapter);

        resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        resultRecyclerView.setAdapter(resultAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(resultRecyclerView);
    }

    private void showCurrentQuestion() {
        wordList.clear();
        wordList.addAll(listening.getWordList());
        selectedWords.clear();
        wordAdapter.notifyDataSetChanged();
        resultAdapter.notifyDataSetChanged();
    }

    private void readQuestionAloud() {
            String question = listening.getQuestion();
            tts.speak(question, TextToSpeech.QUEUE_FLUSH, null, null);
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
    private void checkResult() {
        StringBuilder resultSentence = new StringBuilder();
        for (String word : selectedWords) {
            resultSentence.append(word).append(" ");
        }

        String correctAnswer = listening.getResult();
        boolean isCorrect = resultSentence.toString().trim().equals(correctAnswer);
        if(isCorrect){
            dung = true ;
            Toast.makeText(getContext(), "Dung", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Sai", Toast.LENGTH_SHORT).show();
        }
        }

}