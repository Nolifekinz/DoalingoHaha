package com.example.dualingo.TestFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import com.example.dualingo.ListBaiHoc;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Speaking;
import com.example.dualingo.Models.User;
import com.example.dualingo.OnQuestionCompletedListener;
import com.example.dualingo.R;

import java.util.ArrayList;


public class TestSpeakFragment extends Fragment {

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

    private TextView questionTextView, resultTextView;
    private ProgressBar recordingIndicator;
    private Button recordButton, checkAnswerButton;

    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;

    private boolean dung = false;

    private Speaking speaking ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_speak, container, false);

        questionTextView = view.findViewById(R.id.TestSpeakquestionTextView);
        resultTextView = view.findViewById(R.id.TestSpeakresultTextView);
        checkAnswerButton = view.findViewById(R.id.checkAnswerButton);
        recordButton = view.findViewById(R.id.TestSpeakrecordButton);
        recordingIndicator = view.findViewById(R.id.recordingIndicator);

        Bundle bundle = getArguments();
        if (bundle != null) {
            speaking = (Speaking) bundle.getSerializable("questionData"); // Hoặc `getParcelable`
            if (speaking != null) {
                System.out.println("nonull");
            }else{
                System.out.println("null");
            }
        }
        questionTextView.setText(speaking.getQuestion());

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());

        checkAndRequestPermission();

        recordButton.setOnClickListener(v -> toggleListening());
        checkAnswerButton.setOnClickListener(v -> {
            stopListening();
            completeQuestion();
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(getContext(), "Ứng dụng cần quyền sử dụng microphone để tiếp tục.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
        }
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
            recordButton.setText("Đang ghi...");
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
        String correctAnswer = speaking.getQuestion().trim().toLowerCase();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            //showResultDialog("Dung","Bạn đã trả lời đúng",false);
            dung = true;
            resultTextView.setText(userAnswer);
            Toast.makeText(getContext(), "Đúng!", Toast.LENGTH_SHORT).show();
        } else {
            int similarity = calculateLevenshteinDistance(correctAnswer, userAnswer);
            if (similarity <= 2) { // Cho phép sai lệch 2 ký tự
                //showResultDialog("Gần đúng","Gần Đúng Rồi",false);
                resultTextView.setText(userAnswer);
                Toast.makeText(getContext(), "Gần đúng! Cố gắng hơn.", Toast.LENGTH_SHORT).show();
            } else {
                //showResultDialog("Sai","Bạn đã trả lời sai",false);
                resultTextView.setText(userAnswer);
                Toast.makeText(getContext(), "Sai!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int calculateLevenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1]
                                    + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Microphone permission is required for recording.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showResultDialog(String title, String message, boolean isFinalResult) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.result_dialog);
        dialog.setCancelable(false);

        TextView tvTitle = dialog.findViewById(R.id.dialog_title);
        TextView tvMessage = dialog.findViewById(R.id.dialog_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);

        tvTitle.setText(title);
        tvMessage.setText(message);
        dialog.show();
    }
}




