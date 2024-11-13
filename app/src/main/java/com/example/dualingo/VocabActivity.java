package com.example.dualingo;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.VocabularyAdapter;
import com.example.dualingo.LearningFragment.FillInBlankFragment;
import com.example.dualingo.LearningFragment.TranslateFragment;
import com.example.dualingo.Models.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class VocabActivity extends AppCompatActivity {

    private RecyclerView rvVocabulary;
    private VocabularyAdapter adapter;
    private List<Vocabulary> vocabularyList;

    private ImageButton back;
    private TextView txtLs;
    private Button btnLearnNewWords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocab);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.VocabLessonActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvVocabulary = findViewById(R.id.VocabOfLesson);
        back = findViewById(R.id.btnBackVocabLearn);;
        txtLs = findViewById(R.id.txtVocabLearn);
        btnLearnNewWords = findViewById(R.id.btnLearnNewWords);


        vocabularyList = getVocabularyList(); // Hàm để tạo danh sách từ vựng

        adapter = new VocabularyAdapter(vocabularyList, this);
        rvVocabulary.setLayoutManager(new LinearLayoutManager(this));
        rvVocabulary.setAdapter(adapter);

        Intent intent = getIntent();

        String txt = intent.getStringExtra("lessname");
        txtLs.setText(txt);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLearnNewWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(VocabActivity.this, VocabLearning.class);

                intent1.putExtra("titleNameOfVocab",txt);

                startActivity(intent1);
            }
        });
    }

    private List<Vocabulary> getVocabularyList() {
        List<Vocabulary> list = new ArrayList<>();
        list.add(new Vocabulary("1", "apple", "quả táo", "noun", "I ate an apple.", "/ˈæp.əl/")); // Thêm cách phát âm
        list.add(new Vocabulary("2", "run", "chạy", "verb", "He can run fast.", "/rʌn/")); // Thêm cách phát âm
        // Thêm nhiều từ vựng khác...
        return list;
    }

}