package com.example.dualingo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.SessionAdapter;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Session;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {

    private RecyclerView recyclerView;
    private SessionAdapter sessionAdapter;
    private List<Session> sessionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        // Khởi tạo danh sách session và lecture
        initSessionList();

        // Thiết lập RecyclerView cho session
        recyclerView = view.findViewById(R.id.rvLectures);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1)); // 1 cột cho session
        sessionAdapter = new SessionAdapter(getContext(), sessionList,null);
        recyclerView.setAdapter(sessionAdapter);

        return view;
    }

    private void initSessionList() {
        sessionList = new ArrayList<>();

//        // Tạo lecture
//        List<Lecture> lectures1 = new ArrayList<>();
//        lectures1.add(new Lecture("Lecture 1", R.drawable.mail_icon));
//        lectures1.add(new Lecture("Lecture 2", R.drawable.coin));
//
//        List<Lecture> lectures2 = new ArrayList<>();
//        lectures2.add(new Lecture("Lecture 3", R.drawable.mail_icon));
//        lectures2.add(new Lecture("Lecture 4", R.drawable.coin));
//
//        // Tạo session
//        sessionList.add(new Session("Session 1", lectures1));
//        sessionList.add(new Session("Session 2", lectures2));
    }
}
