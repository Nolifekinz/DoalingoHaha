package com.example.dualingo.TestFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.User;
import com.example.dualingo.OnQuestionCompletedListener;
import com.example.dualingo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestFillFragment extends Fragment {

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

    TextView question;
    RecyclerView ResultRecylerView , WordRecylerView;
    Button check;

    private String result;

    private boolean dung = false;
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;
    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();

    private FillBlank fillBlank ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_fill, container, false);

        question = view.findViewById(R.id.TestFillBlankQuestion);
        ResultRecylerView = view.findViewById(R.id.TestFillBlankresultRecyclerView);
        WordRecylerView = view.findViewById(R.id.TestFillBlankwordRecyclerView);
        check = view.findViewById(R.id.TestFillBlankCheckBtn);

        check.setOnClickListener(v->{
            checkResult();
            completeQuestion();
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            fillBlank = (FillBlank) bundle.getSerializable("questionData"); // Hoặc `getParcelable`
            if (fillBlank != null) {
                System.out.println("nonull");
            }else{
                System.out.println("null");
            }
        }

        setupRecyclerViews();
        showCurrentQuestion();
        return view ;
    }

    private void setupRecyclerViews() {
        // Set up RecyclerView for word list
        WordRecylerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        WordRecylerView.setAdapter(wordAdapter);

        // Set up RecyclerView for result bar
        ResultRecylerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        ResultRecylerView.setAdapter(resultAdapter);

        // Add ItemTouchHelper to rearrange words in the result bar
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(ResultRecylerView);
    }

    private void showCurrentQuestion() {
            wordList.clear();
            wordList.addAll(fillBlank.getWordList());
            selectedWords.clear();
            wordAdapter.notifyDataSetChanged();
            resultAdapter.notifyDataSetChanged();
            String questionText = fillBlank.getQuestion();
            question.setText(questionText);
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
        for (int i = 0; i < selectedWords.size(); i++) {
            resultSentence.append(selectedWords.get(i));
            if (i < selectedWords.size() - 1) {
                resultSentence.append(",");
            }
        }
        result = resultSentence.toString();


        String correctAnswer = fillBlank.getResult();
        boolean isCorrect = resultSentence.toString().trim().equals(correctAnswer);
        if(isCorrect){
            dung = true;
            Toast.makeText(getContext(), "Dung", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Sai", Toast.LENGTH_SHORT).show();
        }
    }
}