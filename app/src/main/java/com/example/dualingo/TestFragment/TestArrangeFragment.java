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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.OnQuestionCompletedListener;
import com.example.dualingo.R;
import com.example.dualingo.TestFirstActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestArrangeFragment extends Fragment {

    private OnQuestionCompletedListener callback;

    private boolean dung = false;

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

    RecyclerView TestArrangeResult,TestArrangeWord;
    Button TestSubmitArrange;
    TextView txt;

    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;
    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();

    private Arranging arranging;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_arrange, container, false);
        TestArrangeResult = view.findViewById(R.id.TestresultRecyclerView);
        TestArrangeWord = view.findViewById(R.id.TestWordRecyclerView);
        TestSubmitArrange = view.findViewById(R.id.TestSubmitButton);
        txt = view.findViewById(R.id.testQuestionTextView);


        TestSubmitArrange.setOnClickListener(v -> {
            checkResult();
            completeQuestion();
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            arranging = (Arranging) bundle.getSerializable("questionData"); // Hoặc `getParcelable`
            if (arranging != null) {
                Toast.makeText(getContext(), "nonull", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
            }
        }

        setupRecyclerViews();
        showCurrentQuestion();

        return view;
    }
    private void setupRecyclerViews() {
        // Set up RecyclerView for word list
        TestArrangeWord.setLayoutManager(new GridLayoutManager(getContext(), 4));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        TestArrangeWord.setAdapter(wordAdapter);

        // Set up RecyclerView for result bar
        TestArrangeResult.setLayoutManager(new GridLayoutManager(getContext(), 4));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        TestArrangeResult.setAdapter(resultAdapter);

        // Add ItemTouchHelper to rearrange words in the result bar
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(TestArrangeResult);
    }

    private void showCurrentQuestion() {
        wordList.clear();
        wordList.addAll(arranging.getWordList());
        selectedWords.clear();
        wordAdapter.notifyDataSetChanged();
        resultAdapter.notifyDataSetChanged();
        txt.setText(arranging.getQuestion());
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
        String correctAnswer = arranging.getResult();
        boolean isCorrect = resultSentence.toString().trim().equals(correctAnswer);
        if(isCorrect){
            dung = true;
            Toast.makeText(getContext(), "Dung", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Sai", Toast.LENGTH_SHORT).show();
        }
    }
}