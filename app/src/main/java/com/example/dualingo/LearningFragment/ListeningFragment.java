package com.example.dualingo.LearningFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.ListBaiHoc;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.Listening;
import com.example.dualingo.Models.User;
import com.example.dualingo.Models.WrongQuestion;
import com.example.dualingo.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListeningFragment extends Fragment {

    private AppDatabase appDatabase;
    private List<Listening> listeningList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;

    private TextToSpeech tts;

    private int correctAnswersCount = 3;
    private int numberQuestion = 3;
    private String lectureId;
    private String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listening, container, false);

        // Lấy lectureId từ Bundle
        if (getArguments() != null) {
            lectureId = getArguments().getString("lectureId", ""); // Lấy lectureId
        }

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

        wordRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        wordRecyclerView.setAdapter(wordAdapter);

        resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        resultRecyclerView.setAdapter(resultAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(resultRecyclerView);
    }

    private void loadListeningData() {
        new Thread(() -> {
            // Lấy bài tập theo lectureId từ Room database
            listeningList = appDatabase.listeningDAO().getListeningsByLectureId(lectureId);  // Truy vấn theo lectureId
            List<Listening> randomQuestions = getRandomQuestions(listeningList, 3);
            listeningList.clear();
            listeningList.addAll(randomQuestions);

            requireActivity().runOnUiThread(this::showCurrentQuestion);
        }).start();
    }

    private List<Listening> getRandomQuestions(List<Listening> questions, int count) {
        List<Listening> randomQuestions = new ArrayList<>(questions);
        Collections.shuffle(randomQuestions);
        return randomQuestions.subList(0, Math.min(count, randomQuestions.size()));
    }

    private void showCurrentQuestion() {
        if (currentQuestionIndex < listeningList.size()) {
            Listening currentQuestion = listeningList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            selectedWords.clear();
            wordAdapter.notifyDataSetChanged();
            resultAdapter.notifyDataSetChanged();
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
        boolean isCorrect = resultSentence.toString().trim().equals(correctAnswer);

        if (isCorrect) {
            listeningList.remove(currentQuestionIndex);
            numberQuestion--;
            showResultDialog("Correct!", "Your answer is correct!", false);
        } else {
            if(numberQuestion!=0){
                handleWrongQuestion(listeningList.get(currentQuestionIndex));
                correctAnswersCount--;
            }
            numberQuestion--;
            listeningList.add(listeningList.get(currentQuestionIndex));
            listeningList.remove(currentQuestionIndex);
            showResultDialog("Incorrect!", "Your answer is incorrect!", false);
        }

        if (listeningList.isEmpty()) {
            updateCompletedLesson(lectureId);
            // Lấy thông tin User từ database và cập nhật EXP
            new Thread(() -> {
                User user = appDatabase.userDAO().getUserById(userId);
                if (user != null) {
                    long newExp = user.getExp() + correctAnswersCount;
                    user.setExp(newExp);

                    // Cập nhật lại thông tin User trong database
                    appDatabase.userDAO().updateUser(user);
                }
            }).start();
            String finalMessage = "Quiz Completed!\nCorrect answers: " + correctAnswersCount + " / 3";
            showResultDialog("Quiz Completed", finalMessage, true);
        }
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void handleWrongQuestion(Listening question) {
        executorService.execute(() -> {
            // Lấy thông tin User từ database
            User user = appDatabase.userDAO().getUserById(userId);

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
                List<String> wrongListeningList = new ArrayList<>();
                wrongListeningList.add(question.getIdListening());
                WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null, wrongListeningList, null, null, null);

                // Cập nhật lại thông tin user với wrongQuestionId mới
                user.setIdWrongQuestion(wrongQuestionId);
                appDatabase.userDAO().updateUser(user);  // Lưu lại thay đổi user

                // Thêm WrongQuestion mới vào database
                appDatabase.wrongQuestionDAO().insertOrUpdateWrongQuestion(newWrongQuestion);
            } else {
                // Nếu đã có wrongQuestionId, tìm bản ghi WrongQuestion trong database
                WrongQuestion wrongQuestion = appDatabase.wrongQuestionDAO().getWrongQuestionById(wrongQuestionId);

                if (wrongQuestion == null) {
                    // Nếu chưa có bản ghi cho wrongQuestionId, tạo mới
                    List<String> wrongListeningList = new ArrayList<>();
                    wrongListeningList.add(question.getIdListening());
                    WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null, null, wrongListeningList, null, null);
                    appDatabase.wrongQuestionDAO().insertOrUpdateWrongQuestion(newWrongQuestion);
                } else {
                    // Nếu đã có bản ghi, cập nhật danh sách câu sai dạng Arranging
                    List<String> wrongListeningList = wrongQuestion.getIdWrongListeningList();
                    if (wrongListeningList == null) {
                        wrongListeningList = new ArrayList<>(); // Khởi tạo danh sách rỗng nếu nó null
                    } else {
                        wrongListeningList = new ArrayList<>(wrongListeningList); // Chuyển đổi thành danh sách mới
                    }

                    if (!wrongListeningList.contains(question.getIdListening())) {
                        wrongListeningList.add(question.getIdListening());
                        appDatabase.wrongQuestionDAO().updateWrongListeningList(wrongQuestionId, wrongListeningList);
                    }
                }
            }
        });
    }


    private void updateCompletedLesson(String lectureId) {
        executorService.execute(() -> {
            appDatabase.runInTransaction(() -> {
                // Lấy CompletedLesson từ database
                CompletedLesson completedLesson = appDatabase.completedLessonDAO().getCompletedLesson(lectureId, userId);

                if (completedLesson == null) {
                    // Nếu chưa có, tạo mới
                    completedLesson = new CompletedLesson(userId+lectureId,userId, lectureId, 0, 0, 1, 0, 0);
                    appDatabase.completedLessonDAO().insertOrUpdate(completedLesson);
                } else {
                    // Nếu đã có, cập nhật trạng thái
                    completedLesson.setListening(1);
                    appDatabase.completedLessonDAO().insertOrUpdate(completedLesson);
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

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (isFinalResult) {
                Intent intent = new Intent(getContext(), ListBaiHoc.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa ngăn xếp để không quay lại ArrangingFragment
                startActivity(intent);
                requireActivity().finish();
            } else {
                showCurrentQuestion();
            }
        });

        dialog.show();
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
