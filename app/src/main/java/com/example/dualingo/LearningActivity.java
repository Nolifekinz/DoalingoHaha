package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dualingo.LearningFragment.ArrangingFragment;
import com.example.dualingo.LearningFragment.FillInBlankFragment;
import com.example.dualingo.LearningFragment.LearnNewWordsFragment;
import com.example.dualingo.LearningFragment.ListeningFragment;
import com.example.dualingo.LearningFragment.SpeakingFragment;

public class LearningActivity extends AppCompatActivity {

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

        String typeQuestion = getIntent().getStringExtra("typeQuestion");

        if(typeQuestion!=null) {
            switchFragment(typeQuestion);
        }else{
            finish();
        }
    }

    private void switchFragment(String typeQuestion) {
        Fragment fragment;
//        String lectureId = getIntent().getStringExtra("lectureId");
//        Bundle bundle = new Bundle();
        if(typeQuestion.equals("arranging")) {
            fragment = new ArrangingFragment();
        }else if(typeQuestion.equals("fill_blank")){
            fragment = new FillInBlankFragment();
        }else if(typeQuestion.equals("listening")){
            fragment = new ListeningFragment();
        }else if(typeQuestion.equals("speaking")) {
            fragment = new SpeakingFragment();
        }else {
            fragment = new LearnNewWordsFragment();
        }

//        Bundle bundle = new Bundle();
//        bundle.putString("testOrLearn", "learn");
//        bundle.putString("lectureId","2");
//        fragment.setArguments(bundle);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.LearningFragment, fragment)
                .commit();
    }
}