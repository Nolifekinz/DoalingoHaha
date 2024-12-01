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

    Button btnGioiThieu,btnBTSapxep,btnBTDien,btnBTNghe,btnBTNoi;
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


        btnGioiThieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ListBaiHoc.this,IntroductionActivity.class);
                startActivity(intent1);
            }
        });
        btnBTSapxep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ListBaiHoc.this,LearningActivity.class);
                intent1.putExtra("typeQuestion","sapxep");
                startActivity(intent1);
            }
        });
        btnBTDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ListBaiHoc.this,LearningActivity.class);
                intent1.putExtra("typeQuestion","dien");
                startActivity(intent1);
            }
        });
        btnBTNghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ListBaiHoc.this,LearningActivity.class);
                intent1.putExtra("typeQuestion","nghe");
                startActivity(intent1);
            }
        });
        btnBTNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ListBaiHoc.this, LearningActivity.class);
                intent1.putExtra("typeQuestion","noi");
                startActivity(intent1);
            }
        });
    }
}