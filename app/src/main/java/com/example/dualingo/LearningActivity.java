package com.example.dualingo;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dualingo.LearningFragment.FillInBlankFragment;
import com.example.dualingo.LearningFragment.TranslateFragment;

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

        String questionType = getCurrentQuestionType();

        // Chuyển đổi giữa các Fragment dựa trên loại câu hỏi
        switchFragment(questionType);


    }
    private String getCurrentQuestionType() {
        // Lấy loại câu hỏi từ dữ liệu (ví dụ từ database hoặc danh sách câu hỏi)
        return "translate"; // Chỉ là ví dụ
    }

    private void switchFragment(String questionType) {
        Fragment fragment;
        if ("translate".equals(questionType)) {
            fragment = new TranslateFragment();
        } else {
            fragment = new FillInBlankFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.LearningFragment, fragment)
                .commit();
    }
}