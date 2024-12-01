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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.dualingo.Models.Speaking;
import com.example.dualingo.R;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.DAO.SpeakingDAO;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

public class SpeakingFragment extends Fragment {

    private TextView questionTextView, resultTextView;
    private ProgressBar recordingIndicator;
    private Button recordButton, checkAnswerButton;

    private TextToSpeech tts;
    private SpeechRecognizer speechRecognizer;
    private String recordedAnswer = "";

    private List<Speaking> speakingList = new ArrayList<>();
    private Speaking currentSpeaking;

    private final String currentLectureId = "1"; // Giả sử bạn đã có ID của bài học hiện tại

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaking, container, false);

        // Initialize views
        questionTextView = view.findViewById(R.id.questionTextView);
        resultTextView = view.findViewById(R.id.resultTextView);
        recordingIndicator = view.findViewById(R.id.recordingIndicator);
        recordButton = view.findViewById(R.id.recordButton);
        checkAnswerButton = view.findViewById(R.id.checkAnswerButton);

        recordingIndicator.setVisibility(View.GONE); // Hide recording indicator initially

        // Initialize Text-to-Speech
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        // Setup button listeners
        recordButton.setOnClickListener(v -> startRecording());
        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        // Request microphone permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        // Load speaking data from Room
        loadSpeakingDataFromRoom();

        return view;
    }

    private void loadSpeakingDataFromRoom() {
        AppDatabase database = AppDatabase.getDatabase(getContext());
        SpeakingDAO speakingDAO = database.speakingDAO();

        LiveData<List<Speaking>> liveDataSpeakingList = speakingDAO.getSpeaksByLectureId(currentLectureId);

        liveDataSpeakingList.observe(getViewLifecycleOwner(), new Observer<List<Speaking>>() {
            @Override
            public void onChanged(List<Speaking> speakingList) {
                if (speakingList != null && !speakingList.isEmpty()) {
                    currentSpeaking = speakingList.get(0); // Lấy bài học đầu tiên
                    questionTextView.setText(currentSpeaking.getQuestion()); // Hiển thị câu hỏi
                }
            }
        });


        if (!speakingList.isEmpty()) {
            // Select the first speaking lesson
            currentSpeaking = speakingList.get(0);

            // Update UI with the question of the current lesson
            questionTextView.setText(currentSpeaking.getQuestion());
        }
    }

    private void startRecording() {
        // Show recording indicator and disable record button
        recordingIndicator.setVisibility(View.VISIBLE);
        recordButton.setEnabled(false);

        // Start speech recognition intent
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    recordedAnswer = matches.get(0).trim();
                }
                Toast.makeText(getContext(), "Recorded: " + recordedAnswer, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEndOfSpeech() {
                // Hide recording indicator and re-enable record button
                recordingIndicator.setVisibility(View.GONE);
                recordButton.setEnabled(true);
                checkAnswer(); // Automatically check answer after recording
            }

            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onError(int error) {
                recordingIndicator.setVisibility(View.GONE);
                recordButton.setEnabled(true);

                String errorMessage = getSpeechRecognitionErrorMessage(error);
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizer.startListening(intent);
    }

    private String getSpeechRecognitionErrorMessage(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO: return "Audio recording error";
            case SpeechRecognizer.ERROR_CLIENT: return "Client error";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: return "Insufficient permissions";
            case SpeechRecognizer.ERROR_NETWORK: return "Network error";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: return "Network timeout";
            case SpeechRecognizer.ERROR_NO_MATCH: return "No match found";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: return "Recognizer is busy";
            case SpeechRecognizer.ERROR_SERVER: return "Server error";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: return "Speech timeout";
            default: return "Unknown error";
        }
    }

    private void checkAnswer() {
        if (recordedAnswer.isEmpty()) {
            Toast.makeText(getContext(), "Please record your answer first!", Toast.LENGTH_SHORT).show();
            return;
        }

        String correctAnswer = currentSpeaking.getQuestion().trim().toLowerCase();
        String userAnswer = recordedAnswer.toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            resultTextView.setText("Correct!");
            resultTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            resultTextView.setText("Incorrect. Try again!");
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
            Toast.makeText(getContext(), "Microphone permission is required for recording.", Toast.LENGTH_SHORT).show();
        }
    }
}
