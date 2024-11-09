package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Adapters.LectureAdapter;
import com.example.dualingo.Adapters.SessionAdapter;
import com.example.dualingo.LearningActivity;
import com.example.dualingo.Models.Session;
import com.example.dualingo.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements LectureAdapter.OnLectureClickListener {

    private FragmentHomeBinding binding;
    private List<Session> sessionList;
    private SessionAdapter sessionAdapter;
    private RecyclerView recyclerViewSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerViewSession = view.findViewById(R.id.rvSession);

        setupRecyclerViews();

        return view;
    }

    private void setupRecyclerViews() {
        // Tạo danh sách các Session với lecturesID là danh sách các ID của Lecture
        sessionList = new ArrayList<>();

        List<String> lecturesID1 = Arrays.asList("1", "2");
        List<String> lecturesID2 = Arrays.asList("1", "1");

        sessionList.add(new Session("1", "Session 1", "a", lecturesID1));
        sessionList.add(new Session("2", "Session 2", "a", lecturesID2));

        // Khởi tạo adapter và thiết lập cho RecyclerView
        sessionAdapter = new SessionAdapter(getContext(), sessionList, this);
        recyclerViewSession.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSession.setAdapter(sessionAdapter);
    }

    @Override
    public void onLectureClick(int sessionPosition, int lecturePosition) {
        Intent intent = new Intent(getContext(), LearningActivity.class);

        // Lấy ID của lecture dựa trên vị trí lecturePosition trong danh sách lecturesID
        String lectureID = sessionList.get(sessionPosition).getLecturesId().get(lecturePosition);
        intent.putExtra("idLecture", lectureID);

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
