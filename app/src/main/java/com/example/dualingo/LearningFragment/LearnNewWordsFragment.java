package com.example.dualingo.LearningFragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.GridViewForVocabLearning;
import com.example.dualingo.Models.VocabularyLesson;
import com.example.dualingo.R;

import java.util.ArrayList;
import java.util.List;

public class LearnNewWordsFragment extends Fragment {

    TextView titleOfVocabLearning , txtQuestion;
    ImageButton btnClose;
    ImageView imgOfVocabLearning;
    GridView gridViewOfVocabLearning;
    Button btnCheckOfVocabLearning , btnDone;
    private int selectedPosition = -1;

    String selectAnswer="";

    private List<VocabularyLesson> vocabularyLessonList = new ArrayList<>();
    private AppDatabase database;
    private int currentQuestionIndex = 0;

    VocabularyLesson currentQuestion;

    private List<String> wordList = new ArrayList<>();
    private WordAdapter wordAdapter;

    public LearnNewWordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_new_words, container, false);

        titleOfVocabLearning = view.findViewById(R.id.titleOfVocabLearning);
        btnClose = view.findViewById(R.id.btnCloseVocabLearning);
        imgOfVocabLearning = view.findViewById(R.id.imgOfWords);
        gridViewOfVocabLearning = view.findViewById(R.id.gridViewOfVocabLearning);
        btnCheckOfVocabLearning = view.findViewById(R.id.btnCheckOfVocabLearning);
        txtQuestion = view.findViewById(R.id.txtQuestionOfVocabLearning);
        btnDone = view.findViewById(R.id.btnDoneLearnVocab);


        database = AppDatabase.getDatabase(getContext());
        getDatafromRoom();

        //titleOfVocabLearning.setText(txt);


        //chỉnh button thoát
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //set up hình ảnh của từ
        //imgOfVocabLearning.setImageResource(R.drawable.fire);

        //set up btncheck
        btnCheckOfVocabLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQuestion.getResult().equals(selectAnswer)){
                    Toast.makeText(getContext(), "Dung", Toast.LENGTH_SHORT).show();
                    currentQuestionIndex++;
                    showCurrentQuestion();
                }else{
                    Toast.makeText(getContext(), "sai", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    public void getDatafromRoom(){
        new Thread(() -> {
            String lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;
            vocabularyLessonList.clear();

            if (lectureId != null) {
                vocabularyLessonList.addAll(database.vocabularyLessonDAO().getAllVocabularyLessons()); // Truy vấn dựa trên idLecture
            }else{
                vocabularyLessonList.addAll(database.vocabularyLessonDAO().getAllVocabularyLessons());
            }

            if (!vocabularyLessonList.isEmpty()) {
                getActivity().runOnUiThread(this::showCurrentQuestion); // Update UI trên luồng chính
            } else {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "No questions found for this lecture!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void showCurrentQuestion() {
        if (currentQuestionIndex < vocabularyLessonList.size()) {
            currentQuestion = vocabularyLessonList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            txtQuestion.setText(currentQuestion.getQuestion());
            imgOfVocabLearning.setImageResource(R.drawable.fire); // setup hình ảnh
            setupGridView();
        }else{

//            new Thread(()->{
//
//                long a = database.userDAO().getCurrentUser().getExp();
//                String id = database.userDAO().getCurrentUser().getId();
//                database.userDAO().updateExp(id, a+25);
//
//            }).start();
            btnDone.setVisibility(View.VISIBLE);
            btnCheckOfVocabLearning.setVisibility(View.GONE);
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }

    private void setupGridView(){
        // Khởi tạo Adapter và thiết lập cho GridView
        GridViewForVocabLearning adapter = new GridViewForVocabLearning(getContext(), wordList);
        gridViewOfVocabLearning.setAdapter(adapter);

        // sự kiện click vào 1 item trong gridView

        gridViewOfVocabLearning.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Nếu đã có một item được chọn trước đó, đặt lại màu của item cũ
                if (selectedPosition != -1) {
                    View previousView = gridViewOfVocabLearning.getChildAt(selectedPosition);
                    if (previousView != null) {
                        previousView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.oldColor));
                    }
                }

                // Đổi màu nền của item mới được nhấn
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.duolingo));

                // Cập nhật vị trí mới cho selectedPosition
                selectedPosition = position;

                // Hiển thị thông báo cho item được nhấn
                selectAnswer = (String) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Bạn đã nhấn vào: " + selectAnswer, Toast.LENGTH_SHORT).show();
            }
        });
    }

}