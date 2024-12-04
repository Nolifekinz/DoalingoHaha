package com.example.dualingo;

// Giao diện callback cho các fragment
public interface OnQuestionCompletedListener {
    void onQuestionCompleted(boolean isCorrect);  // Phương thức được gọi khi câu hỏi hoàn thành
}
