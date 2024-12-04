package com.example.dualingo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dualingo.Adapters.RankAdapter;
import com.example.dualingo.Adapters.UserAdapter;
import com.example.dualingo.Models.Battle;
import com.example.dualingo.Models.Rank;
import com.example.dualingo.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RankFragment extends Fragment {

    private RecyclerView rvRanks, rvUsers;
    private RankAdapter rankAdapter;
    private UserAdapter userAdapter;
    private List<Rank> rankList;
    private List<User> userList;
    private FirebaseFirestore db;

    public RankFragment() {
    }

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
        loadBattleData();

        return view;
    }

    private void loadRankData() {
        db.collection("rank").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Rank rank = document.toObject(Rank.class);
                        if (rank != null) {
                            rankList.add(rank);
                        }
                    }
                    rankAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load ranks", Toast.LENGTH_SHORT).show());
    }

    private void loadBattleData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Battle").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Battle battle = document.toObject(Battle.class);
                        if (battle != null && battle.getOpponentList() != null && battle.getOpponentList().contains(firebaseUser.getUid())) {
                            // Load users by opponentList from Battle
                            fetchUsersByIds(battle.getOpponentList());
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load battle data", Toast.LENGTH_SHORT).show());
    }

    private void fetchUsersByIds(List<String> opponentIds) {
        List<User> opponentUsers = new ArrayList<>();
        for (String id : opponentIds) {
            db.collection("users").document(id).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                opponentUsers.add(user);
                                // Print idBattle to log
                                String idBattle = user.getIdBattle();
                                Log.d("User Battle", "User ID Battle: " + idBattle);
                                // If this is the last user in the list, sort and update the adapter
                                if (opponentUsers.size() == opponentIds.size()) {
                                    opponentUsers.sort((u1, u2) -> Long.compare(u2.getExp(), u1.getExp())); // Sort by exp descending
                                    userList.clear();
                                    userList.addAll(opponentUsers);
                                    userAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to fetch user by ID", Toast.LENGTH_SHORT).show());
        }
    }

}
