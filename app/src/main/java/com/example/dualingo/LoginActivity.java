package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dualingo.Models.User;
import com.example.dualingo.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;
    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = AppDatabase.getDatabase(this);
        binding.btnLogin.setOnClickListener(view -> login());

        binding.btnForgotPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPassword.class)));

        binding.btnGotoRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    void login() {
        String email = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Sai định dạng email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
            return;
        }

        loginWithFirebase(email, password);
    }

    void loginWithFirebase(String email, String password) {
        setInProgress(true);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    String id = authResult.getUser().getUid();

                    // Tham chiếu đến tài liệu trong Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("users").document(id);

                    docRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Long isNewUser = documentSnapshot.getLong("isNewUser"); // Lấy giá trị trường `isNewUser`

                            if (isNewUser != null) {
                                if (isNewUser == 1) {
                                    // Người dùng mới
                                    Intent intent = new Intent(LoginActivity.this, TestOrNo.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                } else {
                                    // Người dùng cũ
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Không tìm thấy trường isNewUser!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Tài liệu không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                        setInProgress(false); // Tắt trạng thái loading
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Lỗi khi truy vấn Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    setInProgress(false);
                });
    }


    void setInProgress(Boolean inProgress) {
//        if (inProgress) {
//            progressBar.setVisibility(View.VISIBLE);
//            btnLogin.setVisibility(View.GONE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//            btnLogin.setVisibility(View.VISIBLE);
//        }
    }

}