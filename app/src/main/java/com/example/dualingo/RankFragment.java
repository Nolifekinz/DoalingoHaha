package com.example.dualingo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dualingo.Adapters.RankAdapter;
import com.example.dualingo.Adapters.UserAdapter;
import com.example.dualingo.Models.Rank;
import com.example.dualingo.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RankFragment extends Fragment {

    private RecyclerView rvRanks, rvUsers;
    private RankAdapter rankAdapter;
    private UserAdapter userAdapter;
    private List<Rank> rankList;
    private List<User> userList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);

        rvRanks = view.findViewById(R.id.rvRanks);
        rvUsers = view.findViewById(R.id.rvUsers);

        // Initialize rank and user lists
        rankList = new ArrayList<>();
        userList = new ArrayList<>();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up rank RecyclerView
        rankAdapter = new RankAdapter(rankList);
        rvRanks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvRanks.setAdapter(rankAdapter);

        // Set up user RecyclerView
        userAdapter = new UserAdapter(userList);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUsers.setAdapter(userAdapter);

        // Load data from Firestore
        loadRankData();
        loadUserData();

        return view;
    }

    private void loadRankData() {
        db.collection("rank").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                Rank rank = document.toObject(Rank.class);
                rankList.add(rank);
            }
            rankAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load ranks", Toast.LENGTH_SHORT).show());
    }

    private void loadUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin người dùng từ Firestore
//        db.collection("users").document(firebaseUser.getUid()).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        User currentUser = documentSnapshot.toObject(User.class);
//
//                        // Kiểm tra xem opponentList có giá trị hay không
//                        if (currentUser.getOpponentList() == null || currentUser.getOpponentList().isEmpty()) {
//                            fetchUsersByRank(currentUser.getRank()); // Gọi hàm lấy người dùng theo rank
//                        } else {
//                            fetchUsersByIds(currentUser.getOpponentList()); // Gọi hàm load opponents
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "User document does not exist", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show());
    }


    private void fetchUsersByRank(Long rankId) {
        db.collection("users")
                .whereEqualTo("rank", rankId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> filteredUsers = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        User user = document.toObject(User.class);
                        filteredUsers.add(user);
                    }

                    // Randomly select up to 10 users
                    List<User> randomUsers = selectRandomUsers(filteredUsers, 10);
                    userList.clear();
                    userList.addAll(randomUsers);
                    addOpponents(randomUsers); // Thêm người dùng vào opponentList
                    userAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to fetch users by rank", Toast.LENGTH_SHORT).show());
    }

    private void addOpponents(List<User> randomUsers) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User currentUserObj = documentSnapshot.toObject(User.class);
                            if (currentUserObj != null) {
//                                for (User opponent : randomUsers) {
//                                    currentUserObj.getOpponentList().add(opponent.getId());
//                                }
                                db.collection("users").document(currentUser.getUid())
                                        .set(currentUserObj)
                                        .addOnSuccessListener(aVoid -> {
                                            // Successfully updated opponent list
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure
                                        });
                            }
                        }
                    });
        }
    }



    private void fetchUsersByIds(List<String> opponentIds) {
        List<User> opponentUsers = new ArrayList<>();
        for (String id : opponentIds) {
            db.collection("users").document(id).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            opponentUsers.add(user);
                            // Nếu đây là người cuối cùng trong danh sách, sắp xếp và cập nhật adapter
                            if (opponentUsers.size() == opponentIds.size()) {
                                opponentUsers.sort((u1, u2) -> Long.compare(u2.getExp(), u1.getExp())); // Sắp xếp giảm dần theo exp
                                userList.clear();
                                userList.addAll(opponentUsers);
                                userAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }


    private List<User> selectRandomUsers(List<User> users, int count) {
        List<User> selectedUsers = new ArrayList<>();
        Random random = new Random();
        int userCount = Math.min(users.size(), count);

        while (selectedUsers.size() < userCount) {
            int randomIndex = random.nextInt(users.size());
            selectedUsers.add(users.remove(randomIndex));
        }

        return selectedUsers;
    }


}
