package com.example.dualingo;

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
import com.example.dualingo.LearningFragment.ListeningFragment;
import com.example.dualingo.LearningFragment.SpeakingFragment;
import com.example.dualingo.Models.Arranging;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Listening;
import com.example.dualingo.Models.Speaking;

import java.util.ArrayList;
import java.util.List;

public class TestFirstActivity extends AppCompatActivity {

    Button tiep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_first);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TestFirstActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tiep = findViewById(R.id.nextTypeQuestion);
        String typeQ = "";
        
        for(int i = 0 ; i<10 ; i++){
            if(i<5){
                typeQ = "fill";
            }else if(i>=5 && i< 10){
                typeQ = "arrange";
            }
            switchFragment(typeQ);
        }
    }

    private void switchFragment(String questionType) {
        Fragment fragment;

        if("speak".equals(questionType)){
            fragment = new SpeakingFragment();
        }else if("listen".equals(questionType)){
            fragment = new ListeningFragment();
        }else if("arrange".equals(questionType)){
            fragment = new ArrangingFragment();
        } else {
            fragment = new FillInBlankFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString("testOrLearn", "test");
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.TestFragment, fragment)
                .commit();
    }
}