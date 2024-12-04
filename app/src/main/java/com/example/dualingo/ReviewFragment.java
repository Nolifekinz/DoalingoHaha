package com.example.dualingo;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ReviewFragment extends Fragment {

    private CardView cardReviewWrongAnswers, cardPracticeExercises;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        // Ánh xạ các CardView
        cardReviewWrongAnswers = view.findViewById(R.id.cardReviewWrongAnswers);
        cardPracticeExercises = view.findViewById(R.id.cardPracticeExercises);

        // Xử lý sự kiện khi nhấn vào "Học lại câu hỏi sai"
        cardReviewWrongAnswers.setOnClickListener(v -> {
            reviewWrongAnswers();
        });

        // Xử lý sự kiện khi nhấn vào "Làm bài tập ôn luyện"
        cardPracticeExercises.setOnClickListener(v -> {
            practiceExercises();
        });

        return view;
    }

    private void reviewWrongAnswers() {
        Toast.makeText(getActivity(), "Học lại câu hỏi sai", Toast.LENGTH_SHORT).show();
    }

    private void practiceExercises() {
        Toast.makeText(getActivity(), "Làm bài tập ôn luyện", Toast.LENGTH_SHORT).show();
    }
}
