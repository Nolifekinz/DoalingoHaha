package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dualingo.Adapters.LectureAdapter;
import com.example.dualingo.Adapters.SessionAdapter;
import com.example.dualingo.Adapters.WordAdapter;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Session;
import com.example.dualingo.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements LectureAdapter.OnLectureClickListener {

    private FragmentHomeBinding binding;

    private List<Session> sessionList ;

    private List<Lecture> lectures;

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

        sessionList = new ArrayList<Session>();

        lectures = new ArrayList<Lecture>();

        lectures.add(new Lecture("1","1","a"));
        lectures.add(new Lecture("2","2","a"));
        lectures.add(new Lecture("3","3","a"));
        lectures.add(new Lecture("4","4","a"));
        lectures.add(new Lecture("5","5","a"));

        List<String> lecturesID1 = Arrays.asList("1", "2");
        List<String> lecturesID2 = Arrays.asList("1", "1");

        sessionList.add(new Session("1", "Session 1", "a", lecturesID1));
        sessionList.add(new Session("2", "Session 2", "a", lecturesID2));

        sessionAdapter = new SessionAdapter(getContext(),sessionList,this);

        recyclerViewSession.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSession.setAdapter(sessionAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onLectureClick(int sessionPosition, int lecturePosition) {
        Intent intent = new Intent(getContext(), LearningActivity.class);
        String idLecture = lectures.get(lecturePosition).getIdLecture();

        intent.putExtra("idLecture",idLecture);

        startActivity(intent);
    }
}
