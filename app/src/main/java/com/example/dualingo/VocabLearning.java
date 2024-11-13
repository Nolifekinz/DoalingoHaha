package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VocabLearning extends AppCompatActivity {

    TextView titleOfVocabLearning;
    ImageButton btnClose;
    ImageView imgOfVocabLearning;
    GridView gridViewOfVocabLearning;
    Button btnCheckOfVocabLearning;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocab_learning);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String txt = intent.getStringExtra("titleNameOfVocab");

        titleOfVocabLearning = findViewById(R.id.titleOfVocabLearning);
        btnClose = findViewById(R.id.btnCloseVocabLearning);
        imgOfVocabLearning = findViewById(R.id.imgOfWords);
        gridViewOfVocabLearning = findViewById(R.id.gridViewOfVocabLearning);
        btnCheckOfVocabLearning = findViewById(R.id.btnCheckOfVocabLearning);

        String result = "";
        String selectResult = "";

        titleOfVocabLearning.setText(txt);


        //chỉnh button thoát
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set up hình ảnh của từ
        imgOfVocabLearning.setImageResource(R.drawable.fire);


        //set up gridView đáp án
        // Ví dụ về mảng dữ liệu để hiển thị
        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4"};

        // Khởi tạo Adapter và thiết lập cho GridView
        GridViewForVocabLearning adapter = new GridViewForVocabLearning(this, items);
        gridViewOfVocabLearning.setAdapter(adapter);

        // sự kiện click vào 1 item trong gridView

        gridViewOfVocabLearning.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Nếu đã có một item được chọn trước đó, đặt lại màu của item cũ
                if (selectedPosition != -1) {
                    View previousView = gridViewOfVocabLearning.getChildAt(selectedPosition);
                    if (previousView != null) {
                        previousView.setBackgroundColor(ContextCompat.getColor(VocabLearning.this, R.color.oldColor));
                    }
                }

                // Đổi màu nền của item mới được nhấn
                view.setBackgroundColor(ContextCompat.getColor(VocabLearning.this, R.color.duolingo));

                // Cập nhật vị trí mới cho selectedPosition
                selectedPosition = position;

                // Hiển thị thông báo cho item được nhấn
                String selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(VocabLearning.this, "Bạn đã nhấn vào: " + selectedItem + position, Toast.LENGTH_SHORT).show();
            }
        });

        //set up btncheck
        btnCheckOfVocabLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectResult.equals(result)){
                    Toast.makeText(VocabLearning.this, "Dung", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VocabLearning.this, "sai", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}