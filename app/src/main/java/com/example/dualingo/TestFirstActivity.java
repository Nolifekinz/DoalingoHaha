package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.example.dualingo.TestFragment.TestArrangeFragment;
import com.example.dualingo.TestFragment.TestFillFragment;
import com.example.dualingo.TestFragment.TestListenFragment;
import com.example.dualingo.TestFragment.TestSpeakFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestFirstActivity extends AppCompatActivity implements OnQuestionCompletedListener{

    private OnQuestionCompletedListener callback;

    Button tiep;

    private List<FillBlank> fillBlankList = new ArrayList<>();
    private List<Arranging> arrangingList = new ArrayList<>();
    private List<Speaking> speakingList = new ArrayList<>();
    private List<Listening> listeningList = new ArrayList<>();

    private int currentQuestionIndex = 0;
    private List<String> questionOrder = new ArrayList<>();

    private AppDatabase database ;

    private int socaudung=0;

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

        database = AppDatabase.getDatabase(this);
        getDatafromRoom();

        tiep = findViewById(R.id.nextTypeQuestion);

        String id = getIntent().getStringExtra("id");

        tiep.setOnClickListener(v -> {
            if (currentQuestionIndex < questionOrder.size()-1) {
                tiep.setVisibility(View.GONE);
                currentQuestionIndex++;
                switchFragment(questionOrder.get(currentQuestionIndex), currentQuestionIndex);
            } else {
                Toast.makeText(this, "Bạn đã hoàn thành bài kiểm tra!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TestFirstActivity.this,ShowKetQuaTest.class);
                intent.putExtra("id",id);
                intent.putExtra("socaudung",socaudung);
                intent.putExtra("tongsocau",questionOrder.size());
                startActivity(intent);
            }
        });
    }

    private void switchFragment(String questionType , int i) {
        Fragment fragment;
        Bundle args = new Bundle();

        if ("speak".equals(questionType)) {
            args.putSerializable("questionData",speakingList.get(i % speakingList.size()));
            fragment = new TestSpeakFragment();
        } else if ("listen".equals(questionType)) {
            args.putSerializable("questionData",listeningList.get(i % listeningList.size()));
            fragment = new TestListenFragment();
        } else if ("arrange".equals(questionType)) {
            args.putSerializable("questionData", arrangingList.get(i % arrangingList.size()));
            fragment = new TestArrangeFragment();
        } else if ("fill".equals(questionType)) {
            args.putSerializable("questionData", fillBlankList.get(i % fillBlankList.size()));
            fragment = new TestFillFragment();
        } else {
            fragment = new Fragment(); // Default fragment
        }

        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.TestFragment, fragment)
                .commit();
    }

    public void getDatafromRoom(){
        new Thread(() -> {
            fillBlankList.clear();
            arrangingList.clear();
            speakingList.clear();
            listeningList.clear();

            fillBlankList.addAll(database.fillBlankDAO().getRandomFillBlank());
            arrangingList.addAll(database.arrangingDAO().getRandomArranging());
            speakingList.addAll(database.speakingDAO().getRandomSpeaking());
            listeningList.addAll(database.listeningDAO().getRandomListening());

            for (int i = 0; i < fillBlankList.size(); i++) questionOrder.add("fill");
            for (int i = 0; i < arrangingList.size(); i++) questionOrder.add("arrange");
            for (int i = 0; i < speakingList.size(); i++) questionOrder.add("speak");
            for (int i = 0; i < listeningList.size(); i++) questionOrder.add("listen");

            //java.util.Collections.shuffle(questionOrder);

            runOnUiThread(() -> {
                if (!questionOrder.isEmpty()) {
                    switchFragment(questionOrder.get(currentQuestionIndex), currentQuestionIndex);
                }
            });

        }).start();
    }

    public void showNextButton() {
        tiep.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQuestionCompleted(boolean isCorrect) {
        if (isCorrect) {
            socaudung++; // Tăng số câu trả lời đúng
        }
        // Hiển thị nút Tiếp khi câu hỏi hoàn thành
        showNextButton();
    }


}

