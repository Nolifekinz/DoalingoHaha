package com.example.dualingo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Models.User;
import com.example.dualingo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    public interface OnFriendClickListener {
        void onFriendClick(User user);
    }

    private List<User> friendList;
    private OnFriendClickListener onFriendClickListener;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FriendAdapter(List<User> friendList, OnFriendClickListener onFriendClickListener) {
        this.friendList = friendList;
        this.onFriendClickListener = onFriendClickListener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User friend = friendList.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void updateFriends(List<User> newFriends) {
        friendList = newFriends;
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        Button btnFollow;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            btnFollow = itemView.findViewById(R.id.btnFollow);

            // Xử lý sự kiện nhấn vào item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onFriendClickListener != null) {
                    onFriendClickListener.onFriendClick(friendList.get(position));
                }
            });
        }

        public void bind(User friend) {
            tvUsername.setText(friend.getUsername());

            // Kiểm tra xem người dùng đã follow hay chưa
            db.collection("users")
                    .document(currentUserId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        List<String> followingList = (List<String>) documentSnapshot.get("followingList");
                        if (followingList != null && followingList.contains(friend.getId())) {
                            btnFollow.setText("Unfollow");
                        } else {
                            btnFollow.setText("Follow");
                        }

                        btnFollow.setOnClickListener(v -> {
                            if (btnFollow.getText().toString().equals("Follow")) {
                                followUser(friend);
                            } else {
                                unfollowUser(friend);
                            }
                        });
                    });
        }

        private void followUser(User friend) {
            addFollower(currentUserId, friend.getId());
            addFollowing(currentUserId, friend.getId());
            btnFollow.setText("Unfollow");
        }

        private void unfollowUser(User friend) {
            removeFollower(currentUserId, friend.getId());
            removeFollowing(currentUserId, friend.getId());
            btnFollow.setText("Follow");
        }

        private void addFollower(String userId, String followeeId) {
            db.collection("users")
                    .document(followeeId)
                    .update("followerList", FieldValue.arrayUnion(userId));
        }

        private void removeFollower(String userId, String followeeId) {
            db.collection("users")
                    .document(followeeId)
                    .update("followerList", FieldValue.arrayRemove(userId));
        }

        private void addFollowing(String userId, String followingId) {
            db.collection("users")
                    .document(userId)
                    .update("followingList", FieldValue.arrayUnion(followingId));
        }

        private void removeFollowing(String userId, String followingId) {
            db.collection("users")
                    .document(userId)
                    .update("followingList", FieldValue.arrayRemove(followingId));
        }
    }
}
