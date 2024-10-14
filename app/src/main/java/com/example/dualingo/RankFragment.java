package com.example.dualingo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dualingo.Adapters.RankAdapter;
import com.example.dualingo.Adapters.UserAdapter;
import com.example.dualingo.Models.Rank;
import com.example.dualingo.Models.User;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);

        rvRanks = view.findViewById(R.id.rvRanks);
        rvUsers = view.findViewById(R.id.rvUsers);

        // Initialize rank and user lists
        rankList = new ArrayList<>();
        userList = new ArrayList<>();

        // Set up rank RecyclerView
        rankAdapter = new RankAdapter(rankList);
        rvRanks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvRanks.setAdapter(rankAdapter);

        // Set up user RecyclerView
        userAdapter = new UserAdapter(userList);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUsers.setAdapter(userAdapter);

        // Load dữ liệu từ Firestore (hoặc bất cứ nguồn nào)
        loadRankData();
        loadUserData();

        return view;
    }

    private void loadRankData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rank").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                Rank rank = document.toObject(Rank.class);
                rankList.add(rank);
            }
            rankAdapter.notifyDataSetChanged();
        });
    }

    private void loadUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Fetch user list based on rank...
    }

}
