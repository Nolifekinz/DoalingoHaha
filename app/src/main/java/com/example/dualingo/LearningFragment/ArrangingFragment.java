package com.example.dualingo.LearningFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.BuildConfig;
import com.example.dualingo.ListBaiHoc;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.User;
import com.example.dualingo.Models.WrongQuestion;
import com.example.dualingo.R;
import com.example.dualingo.databinding.FragmentArrangingBinding;
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

public class ArrangingFragment extends Fragment {

    private GenerativeModelFutures model;
    private FragmentArrangingBinding binding;
    private AppDatabase database; // Room database instance
    private List<Arranging> arrangingList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private List<String> wordList = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private WordAdapter wordAdapter;
    private WordAdapter resultAdapter;
    private int correctAnswersCount = 3;
    private int numberQuestion = 3;
    private List<Arranging> incorrectQuestions = new ArrayList<>();
    private String userId;

    private String result;
    private String correctAnswer;
    private String questionText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArrangingBinding.inflate(inflater, container, false);
        database = AppDatabase.getDatabase(getContext()); // Initialize Room database
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", BuildConfig.API_KEY);
        model = GenerativeModelFutures.from(gm);

        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadArrangingDataFromRoom();

        setupRecyclerViews();

        binding.submitButton.setOnClickListener(v -> checkResult());

        return binding.getRoot();
    }

    private void setupRecyclerViews() {
        // Set up RecyclerView for word list
        binding.wordRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        wordAdapter = new WordAdapter(getContext(), wordList, this::onWordClicked);
        binding.wordRecyclerView.setAdapter(wordAdapter);

        // Set up RecyclerView for result bar
        binding.resultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        resultAdapter = new WordAdapter(getContext(), selectedWords, this::onResultWordClicked);
        binding.resultRecyclerView.setAdapter(resultAdapter);

        // Add ItemTouchHelper to rearrange words in the result bar
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.resultRecyclerView);
    }


    private void loadArrangingDataFromRoom() {
        new Thread(() -> {
            String lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;

            arrangingList.clear();
            if (lectureId != null) {
                arrangingList.addAll(database.arrangingDAO().getArrangingByLectureId(lectureId));
                List<Arranging> randomQuestions = getRandomQuestions(arrangingList, 3);
                arrangingList.clear();
                arrangingList.addAll(randomQuestions);
            }

            if (!arrangingList.isEmpty()) {
                getActivity().runOnUiThread(this::showCurrentQuestion); // Cập nhật UI trên luồng chính
            } else {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "No questions found for this lecture!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private List<Arranging> getRandomQuestions(List<Arranging> questions, int count) {
        List<Arranging> randomQuestions = new ArrayList<>(questions);
        Collections.shuffle(randomQuestions);  // Xáo trộn danh sách
        return randomQuestions.subList(0, Math.min(count, randomQuestions.size()));  // Lấy 3 câu hỏi ngẫu nhiên
    }


    private void showCurrentQuestion() {
        if (currentQuestionIndex < arrangingList.size()) {
            Arranging currentQuestion = arrangingList.get(currentQuestionIndex);
            wordList.clear();
            wordList.addAll(currentQuestion.getWordList());
            selectedWords.clear();
            wordAdapter.notifyDataSetChanged();
            resultAdapter.notifyDataSetChanged();
            questionText=currentQuestion.getQuestion();
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
        for (String word : selectedWords) {
            resultSentence.append(word).append(" ");
        }
        result=resultSentence.toString();

        correctAnswer = arrangingList.get(currentQuestionIndex).getResult();
        boolean isCorrect = resultSentence.toString().trim().equals(correctAnswer);

        if (isCorrect) {
            arrangingList.remove(currentQuestionIndex);
            numberQuestion--;
            showResultDialog("Correct!", "Your answer is correct!", false);
        } else {
            if(numberQuestion!=0){
                handleWrongQuestion(arrangingList.get(currentQuestionIndex));
                correctAnswersCount--;
            }
            numberQuestion--;
            arrangingList.add(arrangingList.get(currentQuestionIndex));
            arrangingList.remove(currentQuestionIndex);// Quay lại câu hỏi trước đó
            showResultDialog("Incorrect!", "Your answer is incorrect!", false);
        }


        if (arrangingList.isEmpty()) {
            String lectureId = getArguments() != null ? getArguments().getString("lectureId") : null;
            if (lectureId != null) {
                updateCompletedLesson(lectureId);
            }

            // Lấy thông tin User từ database và cập nhật EXP
            new Thread(() -> {
                User user = database.userDAO().getUserById(userId);
                if (user != null) {
                    long newExp = user.getExp() + correctAnswersCount;
                    user.setExp(newExp);

                    // Cập nhật lại thông tin User trong database
                    database.userDAO().updateUser(user);
                }
            }).start();

            String finalMessage = "Quiz Completed!\nCorrect answers: " + correctAnswersCount + " / 3";
            showResultDialog("Quiz Completed", finalMessage, true);
        }

    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void handleWrongQuestion(Arranging question) {
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
                List<String> wrongArrangingList = new ArrayList<>();
                wrongArrangingList.add(question.getIdArranging());
                WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, wrongArrangingList, null, null, null, null);

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
                    List<String> wrongArrangingList = new ArrayList<>();
                    wrongArrangingList.add(question.getIdArranging());
                    WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, wrongArrangingList, null, null, null, null);
                    database.wrongQuestionDAO().insertOrUpdateWrongQuestion(newWrongQuestion);
                } else {
                    // Nếu đã có bản ghi, cập nhật danh sách câu sai dạng Arranging
                    List<String> wrongArrangingList = new ArrayList<>(wrongQuestion.getIdWrongArrangingList());
                    if (!wrongArrangingList.contains(question.getIdArranging())) {
                        wrongArrangingList.add(question.getIdArranging());
                        database.wrongQuestionDAO().updateWrongArrangingList(wrongQuestionId, wrongArrangingList);
                    }
                }
            }
        });
    }


    private void updateCompletedLesson(String lectureId) {
        executorService.execute(() -> {
            database.runInTransaction(() -> {
                // Lấy CompletedLesson từ database
                CompletedLesson completedLesson = database.completedLessonDAO().getCompletedLesson(lectureId, userId);

                if (completedLesson == null) {
                    // Nếu chưa có, tạo mới
                    completedLesson = new CompletedLesson(userId, lectureId, 1, 0, 0, 0, 0);
                    database.completedLessonDAO().insertOrUpdate(completedLesson);
                } else {
                    // Nếu đã có, cập nhật trạng thái
                    completedLesson.setArranging(1);
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
            String question="tôi đang giải bài tập Tiếng Anh, câu hỏi của tôi là 1 câu Tiếng Việt: "+
                    questionText+" tôi cần phải sắp xếp các từ Tiếng Anh sao cho ra 1 câu có nghĩ giống câu Tiếng Việt. Và đây là câu sau khi tôi đã sắp xếp:"+
                    result+" Chỉ ra lỗi sai của tôi. Nói ngắn gọn thôi";
            Content content = new Content.Builder()
                    .addText(question)
                    .build();

            Executor executor = Executors.newSingleThreadExecutor();

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    // Cập nhật giao diện với câu trả lời
                    requireActivity().runOnUiThread(() -> tvMessage.setText(result.getText()));
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    requireActivity().runOnUiThread(() -> tvMessage.setText("Đã xảy ra lỗi. Vui lòng thử lại."));
                }
            }, executor);
        }

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
        super.onDestroyView();
        binding = null;
    }
}
