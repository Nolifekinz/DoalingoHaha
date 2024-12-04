package com.example.dualingo.LearningFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.dualingo.BuildConfig;
import com.example.dualingo.ListBaiHoc;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.User;
import com.example.dualingo.Models.WrongQuestion;
import com.example.dualingo.R;
import com.example.dualingo.databinding.FragmentFillInBlankBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FillInBlankFragment extends Fragment {

    private GenerativeModelFutures model;
    private FragmentFillInBlankBinding binding;
    private AppDatabase database;
    private List<FillBlank> fillBlankList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;
    private int correctAnswersCount = 3;
    private int numberQuestion = 3;
    private List<FillBlank> incorrectQuestions = new ArrayList<>();
    private String userId;

    private String result;
    private String correctAnswer;
    private String questionText,lectureId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFillInBlankBinding.inflate(inflater, container, false);
        database = AppDatabase.getDatabase(getContext()); // Initialize Room database
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", BuildConfig.API_KEY);
        model = GenerativeModelFutures.from(gm);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadFillBlankDataFromRoom();

        setupRecyclerViews();

        binding.checkBtn.setOnClickListener(v -> checkResult());

        return binding.getRoot();
    }

    private void setupRecyclerViews() {
        // Set up RecyclerView for word list
        binding.wordRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        binding.wordRecyclerView.setAdapter(wordAdapter);

        // Set up RecyclerView for result bar
        binding.resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        binding.resultRecyclerView.setAdapter(resultAdapter);

        // Add ItemTouchHelper to rearrange words in the result bar
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.resultRecyclerView);
    }

    private void loadFillBlankDataFromRoom() {
        new Thread(() -> {
            String lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;

            fillBlankList.clear();
            if (lectureId != null) {
                fillBlankList.addAll(database.fillBlankDAO().getFillBlankByLectureId(lectureId));
                List<FillBlank> randomQuestions = getRandomQuestions(fillBlankList, 3);
                fillBlankList.clear();
                fillBlankList.addAll(randomQuestions);
            }

            if (!fillBlankList.isEmpty()) {
                getActivity().runOnUiThread(this::showCurrentQuestion); // Update UI on the main thread
            } else {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "No questions found for this lecture!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private List<FillBlank> getRandomQuestions(List<FillBlank> questions, int count) {
        List<FillBlank> randomQuestions = new ArrayList<>(questions);
        Collections.shuffle(randomQuestions);  // Shuffle the list
        return randomQuestions.subList(0, Math.min(count, randomQuestions.size()));  // Get 3 random questions
    }

    private void showCurrentQuestion() {
        if (currentQuestionIndex < fillBlankList.size()) {
            FillBlank currentQuestion = fillBlankList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            selectedWords.clear();
            wordAdapter.notifyDataSetChanged();
            resultAdapter.notifyDataSetChanged();
            questionText = currentQuestion.getQuestion();
            binding.questionTextView.setText(questionText);
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
        for (int i = 0; i < selectedWords.size(); i++) {
            resultSentence.append(selectedWords.get(i));
            if (i < selectedWords.size() - 1) {
                resultSentence.append(",");
            }
        }
        result = resultSentence.toString();


        correctAnswer = fillBlankList.get(currentQuestionIndex).getResult();
        boolean isCorrect = resultSentence.toString().trim().equals(correctAnswer);

        if (isCorrect) {
            fillBlankList.remove(currentQuestionIndex);
            numberQuestion--;
            showResultDialog("Correct!", "Your answer is correct!", false);
        } else {
            if (numberQuestion != 0) {
                handleWrongQuestion(fillBlankList.get(currentQuestionIndex));
                correctAnswersCount--;
            }
            numberQuestion--;
            fillBlankList.add(fillBlankList.get(currentQuestionIndex));
            fillBlankList.remove(currentQuestionIndex); // Go back to the previous question
            showResultDialog("Incorrect!", "Your answer is incorrect!", false);
        }

        if (fillBlankList.isEmpty()) {
            lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;
            if (lectureId != null) {
                updateCompletedLesson(lectureId);
            }

            // Get user info from database and update EXP
            new Thread(() -> {
                User user = database.userDAO().getUserById(userId);
                if (user != null) {
                    long newExp = user.getExp() + correctAnswersCount;
                    user.setExp(newExp);

                    // Update user info in the database
                    database.userDAO().updateUser(user);
                }
            }).start();

            String finalMessage = "Quiz Completed!\nCorrect answers: " + correctAnswersCount + " / 3";
            showResultDialog("Quiz Completed", finalMessage, true);
        }
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void handleWrongQuestion(FillBlank question) {
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
                List<String> wrongFillBlankList = new ArrayList<>();
                wrongFillBlankList.add(question.getIdFillBlank());
                WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null,wrongFillBlankList , null, null, null);

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
                    List<String> wrongFillBlankList = new ArrayList<>();
                    wrongFillBlankList.add(question.getIdFillBlank());
                    WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null, wrongFillBlankList, null, null, null);
                    database.wrongQuestionDAO().insertOrUpdateWrongQuestion(newWrongQuestion);
                } else {
                    // Nếu đã có bản ghi, cập nhật danh sách câu sai dạng Arranging
                    List<String> wrongFillBlankList = wrongQuestion.getIdWrongFillBlankList();
                    if (wrongFillBlankList == null) {
                        wrongFillBlankList = new ArrayList<>(); // Khởi tạo danh sách rỗng nếu nó null
                    } else {
                        wrongFillBlankList = new ArrayList<>(wrongFillBlankList); // Chuyển đổi thành danh sách mới
                    }

                    if (!wrongFillBlankList.contains(question.getIdFillBlank())) {
                        wrongFillBlankList.add(question.getIdFillBlank());
                        database.wrongQuestionDAO().updateWrongArrangingList(wrongQuestionId, wrongFillBlankList);
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
                    completedLesson = new CompletedLesson(userId+lectureId,userId, lectureId, 1, 0, 0, 0, 0);
                    database.completedLessonDAO().insertOrUpdate(completedLesson);
                } else {
                    // Nếu đã có, cập nhật trạng thái
                    completedLesson.setFillBlank(1);
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

        if (!"Your answer is incorrect!".equals(message)){
            btnWrongResson.setVisibility(View.GONE);
        }
        else{
            String question = "tôi đang giải bài tập Tiếng Anh, câu hỏi của tôi là: "+
                    questionText+" tôi cần phải điền các từ Tiếng Anh vào chỗ trống. Và đây là câu trả lời của tôi:"+
                    result+" Chỉ ra lỗi sai của tôi. Nói ngắn gọn bằng Tiếng Việt";
            Content content = new Content.Builder()
                    .addText(question)
                    .build();

            Executor executor = Executors.newSingleThreadExecutor();

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    // Update UI with response text
                    requireActivity().runOnUiThread(() -> tvMessage.setText(result.getText()));
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    requireActivity().runOnUiThread(() -> tvMessage.setText("An error occurred. Please try again."));
                }
            }, executor);
        }

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (isFinalResult) {
                Intent intent = new Intent(getContext(), ListBaiHoc.class);
                intent.putExtra("lectureId",lectureId );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear stack to avoid returning to FillBlankFragment
                startActivity(intent);
                requireActivity().finish();
            } else {
                showCurrentQuestion();
            }
        });

        dialog.show();
    }
}
