package com.example.dualingo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dualingo.LearningFragment.ArrangingFragment;
import com.example.dualingo.LearningFragment.FillInBlankFragment;
import com.example.dualingo.LearningFragment.ListeningFragment;
import com.example.dualingo.LearningFragment.SpeakingFragment;
public class LearningActivity extends AppCompatActivity {

    Fragment learningFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LearningActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String questionType = getIntent().getStringExtra("typeQuestion");
        String lectureId = getIntent().getStringExtra("lectureId"); // Get lectureId from Intent
        switchFragment(questionType, lectureId);
    }

    private void switchFragment(String questionType, String lectureId) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString("lectureId", lectureId);  // Pass lectureId to fragment

        if ("arranging".equals(questionType)) {
            fragment = new ArrangingFragment();
        } else if ("fill_blank".equals(questionType)) {
            fragment = new FillInBlankFragment();
        } else if ("listening".equals(questionType)) {
            fragment = new ListeningFragment();
        } else if ("speaking".equals(questionType)) {
            fragment = new SpeakingFragment();
        } else {
            fragment = new SpeakingFragment(); // Default case
        }

        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.LearningFragment, fragment)
                .commit();
    }
}
