package com.example.dualingo.LearningFragment;

import android.app.Dialog;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.GridViewForVocabLearning;
import com.example.dualingo.ListBaiHoc;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.User;
import com.example.dualingo.Models.Vocabulary;
import com.example.dualingo.Models.VocabularyLesson;
import com.example.dualingo.Models.WrongQuestion;
import com.example.dualingo.R;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private int correctAnswersCount = 0;

    VocabularyLesson currentQuestion;

    private List<String> wordList = new ArrayList<>();
    private WordAdapter wordAdapter;
    private String lectureId;
    private String userId;
    public LearnNewWordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_new_words, container, false);

        btnClose = view.findViewById(R.id.btnCloseVocabLearning);
        imgOfVocabLearning = view.findViewById(R.id.imgOfWords);
        gridViewOfVocabLearning = view.findViewById(R.id.gridViewOfVocabLearning);
        btnCheckOfVocabLearning = view.findViewById(R.id.btnCheckOfVocabLearning);
        txtQuestion = view.findViewById(R.id.txtQuestionOfVocabLearning);
        btnDone = view.findViewById(R.id.btnDoneLearnVocab);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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



        //set up btncheck
        btnCheckOfVocabLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQuestion.getResult().equals(selectAnswer)){
                    currentQuestionIndex++;
                    correctAnswersCount++;
                    showResultDialog("Correct!", "Your answer is correct!", false);
                }else{
                    handleWrongQuestion(vocabularyLessonList.get(currentQuestionIndex));
                    showResultDialog("Incorrect!", "Your answer is incorrect!", false);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void getDatafromRoom() {
        new Thread(() -> {
            lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;
            vocabularyLessonList.clear();

            if (lectureId != null) {
                // Truy vấn 3 câu hỏi ngẫu nhiên
                vocabularyLessonList.addAll(database.vocabularyLessonDAO().getRandomQuestionsByLectureId(lectureId));
            } else {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "LectureId không tồn tại!", Toast.LENGTH_SHORT).show());
                return;
            }

            if (!vocabularyLessonList.isEmpty()) {
                getActivity().runOnUiThread(this::showCurrentQuestion); // Update UI trên luồng chính
            } else {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Không tìm thấy câu hỏi nào!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    private void showCurrentQuestion() {
        if (currentQuestionIndex < vocabularyLessonList.size()) {
            currentQuestion = vocabularyLessonList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            txtQuestion.setText(currentQuestion.getQuestion());
            Glide.with(this)
                    .load(currentQuestion.getImgUrl())
                    .placeholder(R.drawable.fire)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgOfVocabLearning);
            setupGridView();
        }else{

            btnDone.setVisibility(View.VISIBLE);
            btnCheckOfVocabLearning.setVisibility(View.GONE);
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCompletedLesson(lectureId);
                    String finalMessage = "Quiz Completed!\nCorrect answers: " + correctAnswersCount + " / 3";
                    showResultDialog("Quiz Completed", finalMessage, true);
                    Intent intent = new Intent(getContext(), ListBaiHoc.class);
                    intent.putExtra("lectureId",lectureId );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear stack to avoid returning to FillBlankFragment
                    startActivity(intent);
                    requireActivity().finish();
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

            }
        });
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void handleWrongQuestion(VocabularyLesson question) {
        executorService.execute(() -> {
            // Lấy thông tin User từ database
            User user = database.userDAO().getUserById(userId);

            // Nếu không tìm thấy user, không làm gì thêm
            if (user == null) {
                return;
            }

            // Kiểm tra trường hợp wrongQuestionId là null
            String wrongQuestionId = user.getIdWrongQuestion();
            if (wrongQuestionId == null) {
                // Nếu wrongQuestionId là null, tạo mới
                wrongQuestionId = userId; // Phương thức tạo ID mới hoặc có thể dùng userId làm ID

                // Tạo một đối tượng WrongQuestion mới
                List<String> wrongVocabularyLessonList = new ArrayList<>();
                wrongVocabularyLessonList.add(question.getIdVocabularyLesson());
                WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null,null , null, null, wrongVocabularyLessonList);

                // Cập nhật lại thông tin user với wrongQuestionId mới
                user.setIdWrongQuestion(wrongQuestionId);
                database.userDAO().updateUser(user);  // Lưu lại thay đổi user

                // Thêm WrongQuestion mới vào database
                database.wrongQuestionDAO().insertOrUpdateWrongQuestion(newWrongQuestion);
            } else {
                // Nếu đã có wrongQuestionId, tìm bản ghi WrongQuestion trong database
                WrongQuestion wrongQuestion = database.wrongQuestionDAO().getWrongQuestionById(wrongQuestionId);

                if (wrongQuestion == null) {
                    // Nếu chưa có bản ghi cho wrongQuestionId, tạo mới
                    List<String> wrongVocabularyLessonList = new ArrayList<>();
                    wrongVocabularyLessonList.add(question.getIdVocabularyLesson());
                    WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null, null, null, null, wrongVocabularyLessonList);
                    database.wrongQuestionDAO().insertOrUpdateWrongQuestion(newWrongQuestion);
                } else {
                    // Nếu đã có bản ghi, cập nhật danh sách câu sai dạng Arranging
                    List<String> wrongVocabularyLessonList = wrongQuestion.getIdWrongVocabularyList();
                    if (wrongVocabularyLessonList == null) {
                        wrongVocabularyLessonList = new ArrayList<>(); // Khởi tạo danh sách rỗng nếu nó null
                    } else {
                        wrongVocabularyLessonList = new ArrayList<>(wrongVocabularyLessonList); // Chuyển đổi thành danh sách mới
                    }

                    if (!wrongVocabularyLessonList.contains(question.getIdVocabularyLesson())) {
                        wrongVocabularyLessonList.add(question.getIdVocabularyLesson());
                        database.wrongQuestionDAO().updateWrongVocabularyList(wrongQuestionId, wrongVocabularyLessonList);
                    }
                }
            }
        });
    }

    private void updateCompletedLesson(String lectureId) {
        executorService.execute(() -> {
            database.runInTransaction(() -> {
                // Lấy CompletedLesson từ database
                CompletedLesson completedLesson = database.completedLessonDAO().getCompletedLesson(userId, lectureId);

                if (completedLesson == null) {
                    // Nếu chưa có, tạo mới
                    completedLesson = new CompletedLesson(userId+lectureId,userId, lectureId, 0, 0, 0, 0, 1);
                    database.completedLessonDAO().insertOrUpdate(completedLesson);
                } else {
                    // Nếu đã có, cập nhật trạng thái
                    completedLesson.setVocabularyLesson(1);
                    database.completedLessonDAO().insertOrUpdate(completedLesson);
                }
            });
        });
    }


    private void showResultDialog(String title, String message, boolean isFinalResult) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.result_dialog);
        dialog.setCancelable(false);

        TextView tvTitle = dialog.findViewById(R.id.dialog_title);
        TextView tvMessage = dialog.findViewById(R.id.dialog_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        Button btnWrongResson = dialog.findViewById(R.id.btn_wrong_reason);
        btnWrongResson.setVisibility(View.GONE);

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (isFinalResult) {
            } else {
                showCurrentQuestion();
            }
        });

        dialog.show();
    }
}