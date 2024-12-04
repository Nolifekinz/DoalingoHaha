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

import com.example.dualingo.Models.Arranging;
import com.example.dualingo.Models.FillBlank;
import com.example.dualingo.Models.Listening;
import com.example.dualingo.Models.Speaking;
import com.example.dualingo.TestFragment.TestArrangeFragment;
import com.example.dualingo.TestFragment.TestFillFragment;
import com.example.dualingo.TestFragment.TestListenFragment;
import com.example.dualingo.TestFragment.TestSpeakFragment;

import java.util.ArrayList;
import java.util.List;

public class TestToUnlockLectureOrSesson extends AppCompatActivity implements OnQuestionCompletedListener{


    private List<FillBlank> fillBlankList = new ArrayList<>();
    private List<Arranging> arrangingList = new ArrayList<>();
    private List<Speaking> speakingList = new ArrayList<>();
    private List<Listening> listeningList = new ArrayList<>();


    private int currentQuestionIndex = 0;
    private List<String> questionOrder = new ArrayList<>();
    private List<String> lectureIds = new ArrayList<>();

    private AppDatabase database ;

    private int socaudung;

    Button tiep ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_to_unlock_lecture_or_sesson);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TestToUnlockLectureOrSesson), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tiep = findViewById(R.id.TestUnlockBtn);

        tiep.setOnClickListener(v -> {
            if (currentQuestionIndex < questionOrder.size()-1) {
                tiep.setVisibility(View.GONE);
                currentQuestionIndex++;
                switchFragment(questionOrder.get(currentQuestionIndex), currentQuestionIndex);
            } else {
                Toast.makeText(this, "Bạn đã hoàn thành bài kiểm tra!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TestToUnlockLectureOrSesson.this,ShowKetQuaTest.class);
                intent.putExtra("socaudung",socaudung);
                intent.putExtra("tongsocau",questionOrder.size());
                startActivity(intent);
            }
        });


        lectureIds.add("1");
        database = AppDatabase.getDatabase(this);
        getDatafromRoom();
    }

    public void getDatafromRoom(){
        new Thread(() -> {
            fillBlankList.clear();
            arrangingList.clear();
            speakingList.clear();
            listeningList.clear();

            fillBlankList.addAll(database.fillBlankDAO().getFillBlankByLectureIds(lectureIds));
            arrangingList.addAll(database.arrangingDAO().getArrangeByLectureIds(lectureIds));
            speakingList.addAll(database.speakingDAO().getSpeakingByLectureIds(lectureIds));
            listeningList.addAll(database.listeningDAO().getListeningByLectureIds(lectureIds));

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
                .replace(R.id.TestUnlockFragment, fragment)
                .commit();
    }

    public void showNextButton() {
        tiep.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQuestionCompleted(boolean isCorrect) {
        showNextButton();
    }
}