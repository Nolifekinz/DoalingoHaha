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

import com.example.dualingo.Models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestOrNo extends AppCompatActivity {

    Button btnGotoTest, btnGotoMain;

    private AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_or_no);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TestOrNo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnGotoTest = findViewById(R.id.btnGotoTest);
        btnGotoMain = findViewById(R.id.btnGotoMain);
        database = AppDatabase.getDatabase(this);

        btnGotoTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestOrNo.this,TestFirstActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnGotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = getIntent().getStringExtra("id");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Tham chiếu đến tài liệu
                DocumentReference docRef = db.collection("users").document(id);

                // Cập nhật một trường trong tài liệu
                docRef.update("isNewUser", 0)
                        .addOnSuccessListener(aVoid -> {
                            System.out.println("Trường được cập nhật thành công!");
                        })
                        .addOnFailureListener(e -> {
                            System.err.println("Lỗi khi cập nhật: " + e.getMessage());
                        });

                new Thread(()->{

                    database.userDAO().updateIsNewUser(id,0);


                    runOnUiThread(() -> { // Chuyển về UI Thread sau khi cập nhật xong
                        Intent intent = new Intent(TestOrNo.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                }).start();
            }
        });
    }
}