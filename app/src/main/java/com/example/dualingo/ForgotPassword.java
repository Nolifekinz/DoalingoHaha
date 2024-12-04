package com.example.dualingo;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dualingo.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Xử lý sự kiện khi nhấn nút "Đặt lại mật khẩu"
        binding.btnResetPassword.setOnClickListener(view -> resetPassword());

        // Quay lại màn hình đăng nhập
        binding.btnBackToLogin.setOnClickListener(view -> finish());
    }

    private void resetPassword() {
        String email = binding.etEmail.getText().toString().trim();

        // Kiểm tra email có hợp lệ hay không
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Sai định dạng email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi yêu cầu đặt lại mật khẩu qua Firebase Authentication
        setInProgress(true);
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
                    setInProgress(false);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Gửi email thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setInProgress(false);
                });
    }

    private void setInProgress(Boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnResetPassword.setEnabled(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnResetPassword.setEnabled(true);
        }
    }
}
