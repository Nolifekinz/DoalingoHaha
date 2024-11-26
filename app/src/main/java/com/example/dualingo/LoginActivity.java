package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dualingo.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(view -> login());

        binding.btnGotoRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    void login() {
        String email = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();
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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    setInProgress(false);
                });
    }

    void resetPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Gửi email thất bại", Toast.LENGTH_SHORT).show());
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