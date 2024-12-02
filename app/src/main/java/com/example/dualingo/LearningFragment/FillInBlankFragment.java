package com.example.dualingo.LearningFragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.R;

import java.util.ArrayList;
import java.util.List;

public class FillInBlankFragment extends Fragment {

    ImageButton btnback ;
    RecyclerView answerInput;
    Button btnCheck,btnDone;
    ProgressBar proBar;
    TextView questionTextView,answerTextView,translatePrompt;

    FillBlank currentQuestion;
    String currentAnswer,selectWord,testOrLearn;

    private List<FillBlank> fillBlankList = new ArrayList<>();
    private AppDatabase database;
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private WordAdapter wordAdapter;

    @NonNull
    public static FillInBlankFragment newInstance(String param1, String param2) {
        FillInBlankFragment fragment = new FillInBlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fill_in_blank, container, false);

        btnback = view.findViewById(R.id.buttonBack);
        questionTextView = view.findViewById(R.id.questionTextView);
        answerTextView = view.findViewById(R.id.answerTextView);
        answerInput = view.findViewById(R.id.answerInput);
        btnCheck = view.findViewById(R.id.checkBtn);
        btnDone = view.findViewById(R.id.doneBtn);
        translatePrompt = view.findViewById(R.id.translatePrompt);
//        proBar = view.findViewById(R.id.progressBar);


        database = AppDatabase.getDatabase(getContext());

        setupRecyclerViews();

        getDatafromRoom(testOrLearn);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo LayoutInflater và View cho popup từ popup_bottom_layout.xml
                LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_check_answer, null);

                // Tạo PopupWindow
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        false
                );

                // Đặt background bo góc cho PopupWindow
                popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.popup_background));
                popupWindow.setElevation(10); // Tùy chọn: thêm độ nổi cho PopupWindow

                // Thiết lập sự kiện cho nút đóng trong popup
                Button btnClosePopup = popupView.findViewById(R.id.btnClosePopup);
                btnClosePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss(); // Đóng popup
                    }
                });
                TextView txtTitle , txtMessage;
                Button btnTiepTheo;
                txtTitle = popupView.findViewById(R.id.tvPopupTitle);
                txtMessage = popupView.findViewById(R.id.tvPopupMessage);
                btnTiepTheo = popupView.findViewById(R.id.btnClosePopup);

                if(currentAnswer.equals(selectWord)){
                    txtTitle.setText("Đúng rồi");
                    txtMessage.setText("Tuyệt vời");
                    popupWindow.showAtLocation(requireActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
                    answerInput.setEnabled(false);
                    btnTiepTheo.setText("Tiếp Theo");
                    //btnCheck.setVisibility(View.GONE);
                }else{
                    txtTitle.setText("Sai rồi");
                    txtMessage.setText("Làm Lại");
                    btnTiepTheo.setText("Tiếp Theo");
                    answerInput.setEnabled(false);
                    fillBlankList.add(currentQuestion);
                    popupWindow.showAtLocation(requireActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
                }
                btnTiepTheo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answerInput.setEnabled(true);
                        currentQuestionIndex++;
                        showCurrentQuestion();
                        popupWindow.dismiss();
//                            proBar.setProgress(proBar.getProgress()+20);
                    }
                });
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void setupRecyclerViews() {
        // Set up RecyclerView for word list
        answerInput.setLayoutManager(new GridLayoutManager(getContext(), 3));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        answerInput.setAdapter(wordAdapter);
    }

    public void getDatafromRoom(String c){
        new Thread(() -> {
            String lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;
            fillBlankList.clear();

            if (lectureId != null) {
                fillBlankList.addAll(database.fillBlankDAO().getFillBlankByLectureId(lectureId)); // Truy vấn dựa trên idLecture
            }else{
                fillBlankList.addAll(database.fillBlankDAO().getFillBlankByLectureId("1"));
            }

            if (!fillBlankList.isEmpty()) {
                getActivity().runOnUiThread(this::showCurrentQuestion); // Update UI trên luồng chính
            } else {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "No questions found for this lecture!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void showCurrentQuestion() {
        if (currentQuestionIndex < fillBlankList.size()) {
            selectWord="";
            answerTextView.setText("");
            currentQuestion = fillBlankList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            wordAdapter.notifyDataSetChanged();

            questionTextView.setText(currentQuestion.getQuestion());
            currentAnswer=currentQuestion.getResult();
        }else{

//            new Thread(()->{
//
//                long a = database.userDAO().getCurrentUser().getExp();
//                String id = database.userDAO().getCurrentUser().getId();
//                database.userDAO().updateExp(id, a+25);
//
//            }).start();
            answerTextView.setText("Bạn được cộng 25 điểm kinh nghiệm");
            questionTextView.setText("");
            answerInput.setAdapter(null);
            translatePrompt.setText("");
            btnCheck.setVisibility(View.GONE);
            btnDone.setVisibility(View.VISIBLE);
        }
    }

    private void onWordClicked(String word) {
        if(selectWord!=""){
            wordList.remove(word);
            wordList.add(selectWord);
            wordAdapter.notifyDataSetChanged();
        }else{
            wordList.remove(word);
            wordAdapter.notifyDataSetChanged();
        }
        selectWord = word;

        String txt = questionTextView.getText().toString();
        String answer = txt.replace("___",word);

        answerTextView.setText("Câu trả lời của bạn : " + answer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}