package com.example.dualingo.LearningFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.dualingo.Models.Speaking;
import com.example.dualingo.R;
import com.example.dualingo.AppDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeakingFragment extends Fragment {

    private TextView questionTextView, resultTextView;
    private ProgressBar recordingIndicator;
    private Button recordButton, checkAnswerButton;

    private AudioRecord audioRecord;
    private String audioFilePath;
    private boolean isRecording = false;
    private Thread recordingThread;

    private SpeakingDAO speakingDao;
    private List<Speaking> speakingList = new ArrayList<>();

    public SpeakingFragment() {
        // Required empty public constructor
    }

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

        // Initialize Room Database and SpeakingDao
        speakingDao = AppDatabase.getDatabase(requireContext()).speakingDAO();

        // Get lectureId from Bundle
        String lectureId = getArguments().getString("lectureId");

        // Fetch speaking data from Room based on lectureId
        fetchSpeakingData(lectureId);

        // Set up file path for audio
        audioFilePath = requireContext().getExternalFilesDir(null) + "/audioRecording.pcm";

        // Set up button listeners
        recordButton.setOnClickListener(v -> toggleRecording());
        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        // Request microphone permission
        checkAndRequestPermission();

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
                    Speaking currentSpeaking = speakingList.get(0); // Use first item as example
                    questionTextView.setText(currentSpeaking.getQuestion());
                }
            });
        }).start();
    }

    private void toggleRecording() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        // Check permission before starting
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            // Show recording indicator and disable record button
            recordingIndicator.setVisibility(View.VISIBLE);
            recordButton.setEnabled(false);

            // Set up AudioRecord
            int sampleRate = 16000; // Set the sample rate (16kHz is common for speech)
            int channelConfig = AudioFormat.CHANNEL_IN_MONO; // Mono channel
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT; // 16-bit encoding

            int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);

            try {
                // Start the recording in a new thread
                audioRecord.startRecording();
                isRecording = true;

                recordingThread = new Thread(() -> writeAudioDataToFile());
                recordingThread.start();

            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Microphone access denied. Please grant the permission.", Toast.LENGTH_SHORT).show();
            }

        } else {
            // Permission not granted, show a toast or request permission again
            Toast.makeText(getContext(), "Permission is required to record audio.", Toast.LENGTH_SHORT).show();
            checkAndRequestPermission();
        }
    }

    private void writeAudioDataToFile() {
        byte[] audioData = new byte[1024];
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(audioFilePath);
            while (isRecording) {
                int read = audioRecord.read(audioData, 0, audioData.length);
                if (read != AudioRecord.ERROR_INVALID_OPERATION) {
                    fos.write(audioData, 0, read);
                }
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        // Stop recording and release resources
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
        recordingIndicator.setVisibility(View.GONE);
        recordButton.setEnabled(true);
    }

    private void checkAnswer() {
        stopRecording(); // Stop recording after checking answer

        // Process the recorded audio
        String recordedAnswer = processRecordedAudio(audioFilePath); // Example method for processing audio

        if (recordedAnswer.isEmpty()) {
            Toast.makeText(getContext(), "Please record your answer first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the correct answer from the fetched data
        if (!speakingList.isEmpty()) {
            String correctAnswer = speakingList.get(0).getQuestion().trim().toLowerCase(); // Assuming using first item
            String userAnswer = recordedAnswer.toLowerCase();

            if (userAnswer.equals(correctAnswer)) {
                resultTextView.setText("Correct!");
                resultTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                resultTextView.setText("Incorrect. Try again!");
                resultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }

    private String processRecordedAudio(String audioFilePath) {
        // Implement a method to process the audio file and return the text (you can use libraries like Speech-to-Text)
        return ""; // Return transcribed text
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
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
