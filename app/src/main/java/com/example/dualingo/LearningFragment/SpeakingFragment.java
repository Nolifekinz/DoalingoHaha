package com.example.dualingo.LearningFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dualingo.DAO.SpeakingDAO;
import com.example.dualingo.ListBaiHoc;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.Speaking;
import com.example.dualingo.Models.User;
import com.example.dualingo.Models.WrongQuestion;
import com.example.dualingo.R;
import com.example.dualingo.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpeakingFragment extends Fragment {

    private TextView questionTextView, resultTextView;
    private ProgressBar recordingIndicator;
    private Button recordButton, checkAnswerButton;
    private AppDatabase database;

    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;

    private SpeakingDAO speakingDao;
    private List<Speaking> speakingList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int numberQuestion = 3;
    private int correctAnswersCount = 3;
    private String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String lectureId;

    public SpeakingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaking, container, false);
        database = AppDatabase.getDatabase(getContext());
        // Initialize views
        questionTextView = view.findViewById(R.id.questionTextView);
        resultTextView = view.findViewById(R.id.resultTextView);
        recordingIndicator = view.findViewById(R.id.recordingIndicator);
        recordButton = view.findViewById(R.id.recordButton);
        checkAnswerButton = view.findViewById(R.id.checkAnswerButton);

        // Initialize Room Database and SpeakingDao
        speakingDao = AppDatabase.getDatabase(requireContext()).speakingDAO();

        // Get lectureId from Bundle
        lectureId = getArguments().getString("lectureId");

        // Fetch speaking data from Room based on lectureId
        fetchSpeakingData(lectureId);

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());

        // Request microphone permission
        checkAndRequestPermission();

        // Set up button listeners
        recordButton.setOnClickListener(v -> toggleListening());
        checkAnswerButton.setOnClickListener(v -> stopListening());

        return view;
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void fetchSpeakingData(String lectureId) {
        new Thread(() -> {
            speakingList = speakingDao.getSpeaksByLectureId(lectureId); // Query from Room based on lectureId
            getActivity().runOnUiThread(() -> {
                if (!speakingList.isEmpty()) {
                    // Trộn ngẫu nhiên danh sách câu hỏi
                    shuffleQuestions();

                    // Lấy 3 câu hỏi ngẫu nhiên từ danh sách
                    if (speakingList.size() >= 3) {
                        Speaking currentSpeaking = speakingList.get(currentQuestionIndex);
                        questionTextView.setText(currentSpeaking.getQuestion());
                    } else {
                        questionTextView.setText("Không đủ câu hỏi.");
                    }
                }
            });
        }).start();
    }

    private void shuffleQuestions() {
        // Trộn ngẫu nhiên danh sách câu hỏi
        Collections.shuffle(speakingList);

        // Lấy chỉ 3 câu hỏi từ danh sách đã trộn
        speakingList = speakingList.subList(0, Math.min(speakingList.size(), 3));
    }


    private void toggleListening() {
        if (isListening) {
            stopListening();
        } else {
            startListening();
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            // Show recording indicator
            recordingIndicator.setVisibility(View.VISIBLE);
            recordButton.setEnabled(false);

            // Set up speech recognition intent
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(getContext(), "Bắt đầu nói...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBeginningOfSpeech() {
                    resultTextView.setText("");
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // Optional: Show sound level indicator
                }

                @Override
                public void onBufferReceived(byte[] buffer) {}

                @Override
                public void onEndOfSpeech() {
                    recordingIndicator.setVisibility(View.GONE);
                }

                @Override
                public void onError(int error) {
                    resultTextView.setText("Có lỗi xảy ra: " + error);
                    recordingIndicator.setVisibility(View.GONE);
                    isListening = false;
                    recordButton.setEnabled(true);
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String userAnswer = matches.get(0).toLowerCase().trim();
                        compareAnswer(userAnswer);
                    }
                    isListening = false;
                    recordButton.setEnabled(true);
                }

                @Override
                public void onPartialResults(Bundle partialResults) {}

                @Override
                public void onEvent(int eventType, Bundle params) {}
            });

            // Start listening
            speechRecognizer.startListening(intent);
            isListening = true;
        } else {
            Toast.makeText(getContext(), "Bạn cần cấp quyền sử dụng microphone.", Toast.LENGTH_SHORT).show();
            checkAndRequestPermission();
        }
    }

    private void stopListening() {
        if (speechRecognizer != null && isListening) {
            speechRecognizer.stopListening();
            recordingIndicator.setVisibility(View.GONE);
            recordButton.setEnabled(true);
            isListening = false;
        }
    }

    private void compareAnswer(String userAnswer) {
        if (!speakingList.isEmpty()) {
            String correctAnswer = speakingList.get(currentQuestionIndex).getQuestion().trim().toLowerCase();
            if (userAnswer.equals(correctAnswer)) {
                speakingList.remove(currentQuestionIndex);
                numberQuestion--;
                showResultDialog("Correct!", "Your answer is correct!", false);
            } else {
                if(numberQuestion!=0){
                    handleWrongQuestion(speakingList.get(currentQuestionIndex));
                    correctAnswersCount--;
                }
                numberQuestion--;
                speakingList.add(speakingList.get(currentQuestionIndex));
                speakingList.remove(currentQuestionIndex);
                showResultDialog("Incorrect!", "Your answer is incorrect!", false);
            }
        }
        if (speakingList.isEmpty()) {
            updateCompletedLesson(lectureId);
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
            dialog.dismiss();
            if (isFinalResult) {
                Intent intent = new Intent(getContext(), ListBaiHoc.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa ngăn xếp để không quay lại ArrangingFragment
                startActivity(intent);
                requireActivity().finish();
            } else {
                Speaking currentSpeaking = speakingList.get(currentQuestionIndex);
                questionTextView.setText(currentSpeaking.getQuestion());
            }
        });

        dialog.show();
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void handleWrongQuestion(Speaking question) {
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
                List<String> wrongSpeakingList = new ArrayList<>();
                wrongSpeakingList.add(question.getIdSpeaking());
                WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null, null, null, wrongSpeakingList, null);

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
                    List<String> wrongSpeakingList = new ArrayList<>();
                    wrongSpeakingList.add(question.getIdSpeaking());
                    WrongQuestion newWrongQuestion = new WrongQuestion(wrongQuestionId, null, null, wrongSpeakingList, null, null);
                    database.wrongQuestionDAO().insertOrUpdateWrongQuestion(newWrongQuestion);
                } else {
                    // Nếu đã có bản ghi, cập nhật danh sách câu sai dạng Arranging
                    List<String> wrongSpeakingList = wrongQuestion.getIdWrongSpeakingList();
                    if (wrongSpeakingList == null) {
                        wrongSpeakingList = new ArrayList<>(); // Khởi tạo danh sách rỗng nếu nó null
                    } else {
                        wrongSpeakingList = new ArrayList<>(wrongSpeakingList); // Chuyển đổi thành danh sách mới
                    }
                    if (!wrongSpeakingList.contains(question.getIdSpeaking())) {
                        wrongSpeakingList.add(question.getIdSpeaking());
                        database.wrongQuestionDAO().updateWrongSpeakingList(wrongQuestionId, wrongSpeakingList);
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
                    completedLesson = new CompletedLesson(userId+lectureId,userId, lectureId, 0, 0, 0, 1, 0);
                    database.completedLessonDAO().insertOrUpdate(completedLesson);
                } else {
                    // Nếu đã có, cập nhật trạng thái
                    completedLesson.setSpeaking(1);
                    database.completedLessonDAO().insertOrUpdate(completedLesson);
                }
            });
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Microphone permission is required for recording.", Toast.LENGTH_SHORT).show();
        }
    }
}
