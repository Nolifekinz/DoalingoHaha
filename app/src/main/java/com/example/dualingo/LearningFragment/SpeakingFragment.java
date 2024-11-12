package com.example.dualingo.LearningFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
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

import com.example.dualingo.Models.Speaking;
import com.example.dualingo.R;

import java.util.ArrayList;
import java.util.Locale;

public class SpeakingFragment extends Fragment {

    private TextView questionTextView;
    private ProgressBar recordingIndicator;
    private Button recordButton, checkAnswerButton;
    private TextView resultTextView;

    private TextToSpeech tts;
    private SpeechRecognizer speechRecognizer;
    private String recordedAnswer = "";

    private final Speaking currentSpeaking = new Speaking("1", "1", "Hello, how are you?");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout manually
        View view = inflater.inflate(R.layout.fragment_speaking, container, false);

        // Khởi tạo các view bằng findViewById
        questionTextView = view.findViewById(R.id.questionTextView);
        recordingIndicator = view.findViewById(R.id.recordingIndicator);
        recordButton = view.findViewById(R.id.recordButton);
        checkAnswerButton = view.findViewById(R.id.checkAnswerButton);
        resultTextView = view.findViewById(R.id.resultTextView);

        // Set câu hỏi cho TextView
        questionTextView.setText(currentSpeaking.getQuestion());
        recordingIndicator.setVisibility(View.GONE); // Ẩn lúc đầu

        // Khởi tạo TextToSpeech
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        // Khởi tạo SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        // Thiết lập click listener cho các nút
        recordButton.setOnClickListener(v -> startRecording());
        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        // Yêu cầu quyền ghi âm nếu chưa được cấp
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        return view; // Trả về view thay vì binding.getRoot()
    }

    private void startRecording() {
        // Hiển thị dấu hiệu ghi âm
        recordingIndicator.setVisibility(View.VISIBLE);
        recordButton.setEnabled(false); // Vô hiệu nút khi đang ghi âm

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    recordedAnswer = matches.get(0);
                    Toast.makeText(getContext(), "Recorded: " + recordedAnswer, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEndOfSpeech() {
                // Ẩn dấu hiệu ghi âm và kiểm tra câu trả lời tự động
                recordingIndicator.setVisibility(View.GONE);
                recordButton.setEnabled(true);
                checkAnswer();
            }

            // Ghi đè trống cho các phương thức không sử dụng
            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onError(int error) {
                // Ẩn dấu hiệu trong trường hợp lỗi
                recordingIndicator.setVisibility(View.GONE);
                recordButton.setEnabled(true);
            }
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizer.startListening(intent);
    }

    private void checkAnswer() {
        String correctAnswer = currentSpeaking.getQuestion().toLowerCase();
        String userAnswer = recordedAnswer.toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            resultTextView.setText("Correct!");
            resultTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            resultTextView.setText("Incorrect, try again.");
            resultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Microphone permission is required for recording", Toast.LENGTH_SHORT).show();
        }
    }
}
