package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dualingo.Adapters.FriendAdapter;
import com.example.dualingo.Models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddFriendDialogFragment extends DialogFragment {

    private EditText etSearch;
    private RecyclerView rvFriends;
    private FriendAdapter friendAdapter;
    private FirebaseFirestore db;
    private boolean showSearchBar;
    private List<User> userList = new ArrayList<>();

    public static AddFriendDialogFragment newInstance(boolean showSearchBar, List<User> userList) {
        AddFriendDialogFragment fragment = new AddFriendDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("showSearchBar", showSearchBar);
        args.putSerializable("userList", (ArrayList<User>) userList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend_dialog, container, false);

        db = FirebaseFirestore.getInstance(); // Khởi tạo Firestore
        etSearch = view.findViewById(R.id.etSearch);
        rvFriends = view.findViewById(R.id.rvFriends);

        if (getArguments() != null) {
            showSearchBar = getArguments().getBoolean("showSearchBar");
            userList = (List<User>) getArguments().getSerializable("userList");
        }

        if (!showSearchBar) {
            etSearch.setVisibility(View.GONE);
        } else {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchFriends(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Set up FriendAdapter with OnClickListener
        friendAdapter = new FriendAdapter(userList, user -> {
            // Khi người dùng nhấn vào một người bạn, mở trang cá nhân của người đó
            openUserProfile(user);
        });

        rvFriends.setAdapter(friendAdapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void openUserProfile(User user) {
        // Tạo Intent để mở UserProfileActivity
        Intent intent = new Intent(getContext(), UserProfileActivity.class);

        // Gửi thông tin của user qua Intent
        intent.putExtra("user_id", user.getId());
        intent.putExtra("username", user.getUsername());
        intent.putExtra("email", user.getEmail()); // Nếu có thêm thông tin cần truyền
        startActivity(intent);
    }


    private void searchFriends(String query) {
        db.collection("users")
                .whereGreaterThanOrEqualTo("username", query)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            if (user != null) {
                                users.add(user);
                            }
                        }
                        friendAdapter.updateFriends(users);
                    }
                });
    }
}
