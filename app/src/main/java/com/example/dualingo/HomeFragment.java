package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

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

    private TextView level , streak;

    private ImageView flag , notify;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerViewSession = view.findViewById(R.id.rvSession);

        level = view.findViewById(R.id.level);
        streak = view.findViewById(R.id.streak);
        flag = view.findViewById(R.id.flag_language);
        notify = view.findViewById(R.id.notify);

        level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(R.id.level,v);
            }
        });
        streak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(R.id.streak,v);
            }
        });
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(R.id.flag_language,v);
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(R.id.notify,v);
            }
        });

        setupRecyclerViews();

        return view;
    }

    private void showPopup(int id , View anchorView) {

        // Sử dụng LayoutInflater để chuyển đổi layout thành view
        LayoutInflater inflater = getLayoutInflater();
        View popupView;
        if (id == R.id.level) {
            popupView = inflater.inflate(R.layout.popup_level, null);
        } else if (id == R.id.streak) {
            popupView = inflater.inflate(R.layout.popup_streak, null);
        } else if (id == R.id.flag_language) {
            popupView = inflater.inflate(R.layout.popup_flag, null);
        } else {
            popupView = inflater.inflate(R.layout.popup_notify, null);
        }

        // Đặt background cho PopupWindow
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.popup_background));
        popupWindow.setElevation(10); // Đặt độ nổi cho popup để trông rõ hơn
        popupWindow.showAsDropDown(anchorView, 0, 10);
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

        sessionList.add(new Session("1", "Session 1", "a", lecturesID1,true));
        sessionList.add(new Session("2", "Session 2", "a", lecturesID2,false));

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
        Intent intent = new Intent(getContext(), SetTimeActivity.class);
        String idLecture = lectures.get(lecturePosition).getIdLecture();

        intent.putExtra("idLecture",idLecture);

        startActivity(intent);
    }
}
