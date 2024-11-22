package com.example.dualingo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dualingo.Models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private TextView tvUsername, tvXP, tvRanking;
    private Button btnFollowUser;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Khởi tạo các view
        ivAvatar = findViewById(R.id.ivAvatar);
        tvUsername = findViewById(R.id.tvUsername);
        tvXP = findViewById(R.id.tvXP);
        tvRanking = findViewById(R.id.tvRanking);
        btnFollowUser = findViewById(R.id.btnFollowUser);

        // Nhận userId từ Intent
        userId = getIntent().getStringExtra("user_id");

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy thông tin user từ Firestore
        fetchUserData();

//        btnFollowUser.setOnClickListener(v -> sendFriendRequest());
    }

    private void fetchUserData() {
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    displayUserInfo(user);
                }
            }
        });
    }

    private void displayUserInfo(User user) {
        // Hiển thị tên người dùng
        tvUsername.setText(user.getUsername());

        // Hiển thị XP
        tvXP.setText(String.format("XP: %d", user.getExp()));

        // Hiển thị thứ hạng
        tvRanking.setText(String.format("Rank: %d", user.getRank()));

        // Hiển thị ảnh đại diện
//        if (user.getProfilePic() != null && !user.getProfilePic().isEmpty()) {
//            Glide.with(this)
//                    .load(user.getProfilePic())
//                    .placeholder(R.drawable.default_avatar) // Ảnh mặc định nếu không có ảnh
//                    .into(ivAvatar);
//        }
    }

//    private void sendFriendRequest() {
//        // Xử lý logic gửi yêu cầu kết bạn
//        db.collection("friend_requests").add(new FriendRequest(userId, /* currentUserId */))
//                .addOnSuccessListener(documentReference -> {
//                    btnFollowUser.setText("Đã gửi lời mời");
//                    btnFollowUser.setEnabled(false);
//                });
//    }
}
