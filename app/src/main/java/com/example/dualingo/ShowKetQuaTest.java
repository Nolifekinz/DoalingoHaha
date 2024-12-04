package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class ShowKetQuaTest extends AppCompatActivity {


    TextView showKQ;
    Button didenbaihoc ;

    private AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_ket_qua_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.showKQ), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showKQ = findViewById(R.id.hienthiketqua);
        didenbaihoc = findViewById(R.id.didenbaihoc);
        database = AppDatabase.getDatabase(this);

        int socaudung = getIntent().getIntExtra("socaudung",0);
        int tongsocau = getIntent().getIntExtra("tongsocau",0);
        String id = getIntent().getStringExtra("id");

        showKQ.setText("Kết quả của bạn : " + socaudung + "/" + tongsocau);

        didenbaihoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        Intent intent = new Intent(ShowKetQuaTest.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                }).start();
            }
        });


    }
}