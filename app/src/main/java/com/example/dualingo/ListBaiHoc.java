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

public class ListBaiHoc extends AppCompatActivity {

    Button btnGioiThieu, btnBTSapxep, btnBTDien, btnBTNghe, btnBTNoi,btnBTTuVung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_bai_hoc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listBaiHoc), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGioiThieu = findViewById(R.id.btnGioithieu);
        btnBTSapxep = findViewById(R.id.btnBTSapxep);
        btnBTDien = findViewById(R.id.btnBTDien);
        btnBTNghe = findViewById(R.id.btnBTNghe);
        btnBTNoi = findViewById(R.id.btnBTNoi);
        btnBTTuVung = findViewById(R.id.btnBTTuVung);

        String lectureId = getIntent().getStringExtra("lectureId");

        // Set up OnClickListeners for the buttons
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
        btnBTTuVung.setOnClickListener(v ->{
            Intent intent = new Intent(ListBaiHoc.this, LearningActivity.class);
            intent.putExtra("lectureId", lectureId);
            intent.putExtra("typeQuestion", "newWord");
            startActivity(intent);
        });
    }
}
