package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dualingo.DAO.CompletedLessonDAO;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.DAO.LectureDAO;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListBaiHoc extends AppCompatActivity {

    TextView btnGioiThieu, btnBTSapxep, btnBTDien, btnBTNghe, btnBTNoi, btnBTTuVung, txtLectureTitle;
    ImageButton btnBack;
    ImageView imgBTSapxep, imgBTDien, imgBTNghe, imgBTNoi, imgBTTuVung;  // Khai báo ImageView
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    // Khai báo DAO
    CompletedLessonDAO completedLessonDAO;

    // Sử dụng Executor để chạy tác vụ ngoài thread chính
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bai_hoc);

        // Gán ImageView và các TextView
        btnBack = findViewById(R.id.btnBack);
        btnGioiThieu = findViewById(R.id.txtGioithieu);
        btnBTSapxep = findViewById(R.id.txtBTSapxep);
        btnBTDien = findViewById(R.id.txtBTDien);
        btnBTNghe = findViewById(R.id.txtBTNghe);
        btnBTNoi = findViewById(R.id.txtBTNoi);
        btnBTTuVung = findViewById(R.id.txtBTTuVung);
        txtLectureTitle = findViewById(R.id.txtLectureTitle);

        // Gán ImageView cho các bài tập
        imgBTSapxep = findViewById(R.id.imgBTSapxep);
        imgBTDien = findViewById(R.id.imgBTDien);
        imgBTNghe = findViewById(R.id.imgBTNghe);
        imgBTNoi = findViewById(R.id.imgBTNoi);
        imgBTTuVung = findViewById(R.id.imgBTTuVung);

        String lectureId = getIntent().getStringExtra("lectureId");

        // Lấy Lecture Title từ Room database
        getLectureTitle(lectureId);
        completedLessonDAO = AppDatabase.getDatabase(getApplicationContext()).completedLessonDAO();
        executorService.execute(() -> {
            // Truy vấn Room database trong background thread
            CompletedLesson completedLesson = completedLessonDAO.getCompletedLesson(userId, lectureId);

            // Cập nhật UI trên main thread
            runOnUiThread(() -> {
                if (completedLesson != null) {
                    imgBTSapxep.setVisibility(completedLesson.getArranging() == 1 ? View.VISIBLE : View.GONE);
                    imgBTDien.setVisibility(completedLesson.getFillBlank() == 1 ? View.VISIBLE : View.GONE);
                    imgBTNghe.setVisibility(completedLesson.getListening() == 1 ? View.VISIBLE : View.GONE);
                    imgBTNoi.setVisibility(completedLesson.getSpeaking() == 1 ? View.VISIBLE : View.GONE);
                    imgBTTuVung.setVisibility(completedLesson.getVocabularyLesson() == 1 ? View.VISIBLE : View.GONE);
                }
            });
        });


        // Quay lại MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ListBaiHoc.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up các OnClickListener cho các nút
        btnGioiThieu.setOnClickListener(v -> {
            Intent intent = new Intent(ListBaiHoc.this, IntroductionActivity.class);
            intent.putExtra("lectureId", lectureId);
            startActivity(intent);
        });

        btnBTSapxep.setOnClickListener(v -> {
            Intent intent = new Intent(ListBaiHoc.this, LearningActivity.class);
            intent.putExtra("lectureId", lectureId);
            intent.putExtra("typeQuestion", "arranging");
            startActivity(intent);
        });

        btnBTDien.setOnClickListener(v -> {
            Intent intent = new Intent(ListBaiHoc.this, LearningActivity.class);
            intent.putExtra("lectureId", lectureId);
            intent.putExtra("typeQuestion", "fill_blank");
            startActivity(intent);
        });

        btnBTNghe.setOnClickListener(v -> {
            Intent intent = new Intent(ListBaiHoc.this, LearningActivity.class);
            intent.putExtra("lectureId", lectureId);
            intent.putExtra("typeQuestion", "listening");
            startActivity(intent);
        });

        btnBTNoi.setOnClickListener(v -> {
            Intent intent = new Intent(ListBaiHoc.this, LearningActivity.class);
            intent.putExtra("lectureId", lectureId);
            intent.putExtra("typeQuestion", "speaking");
            startActivity(intent);
        });

        btnBTTuVung.setOnClickListener(v -> {
            Intent intent = new Intent(ListBaiHoc.this, LearningActivity.class);
            intent.putExtra("lectureId", lectureId);
            intent.putExtra("typeQuestion", "newWord");
            startActivity(intent);
        });
    }

    // Phương thức lấy thông tin bài học từ Room database
    private void getLectureTitle(String lectureId) {
        executorService.execute(() -> {
            // Truy vấn thông tin bài học từ DAO
            LectureDAO lectureDAO = AppDatabase.getDatabase(getApplicationContext()).lectureDAO();
            Lecture lecture = lectureDAO.getLectureById(lectureId);

            // Cập nhật UI trên thread chính
            runOnUiThread(() -> {
                if (lecture != null) {
                    txtLectureTitle.setText(lecture.getTitle());
                }
            });
        });
    }
}

