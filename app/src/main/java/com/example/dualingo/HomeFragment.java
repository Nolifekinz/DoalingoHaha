package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Session;
import com.example.dualingo.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  {

    private FragmentHomeBinding binding;
    private List<Session> sessionList = new ArrayList<>();
    private List<Lecture> lectures = new ArrayList<>();
    private SessionAdapter sessionAdapter;
    private RecyclerView recyclerViewSession;
    private TextView level, streak;
    private ImageView flag, notify;

    private AppDatabase database;

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

        // Khởi tạo database
        database = AppDatabase.getDatabase(requireContext());

        level.setOnClickListener(v -> showPopup(R.id.level, v));
        streak.setOnClickListener(v -> showPopup(R.id.streak, v));
        flag.setOnClickListener(v -> showPopup(R.id.flag_language, v));
        notify.setOnClickListener(v -> showPopup(R.id.notify, v));

        setupRecyclerViews();
        loadDataFromDatabase();

        return view;
    }

    private void showPopup(int id, View anchorView) {
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

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.popup_background));
        popupWindow.setElevation(10);
        popupWindow.showAsDropDown(anchorView, 0, 10);
    }

    private void setupRecyclerViews() {
        sessionAdapter = new SessionAdapter(getContext(), sessionList, (sessionPos, lecturePos) -> {
            // Xử lý sự kiện khi người dùng click vào Lecture
            Intent intent = new Intent(getContext(), LearningActivity.class);
            intent.putExtra("sessionPosition", sessionPos);
            intent.putExtra("lecturePosition", lecturePos);
            startActivity(intent);
        });
        recyclerViewSession.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSession.setAdapter(sessionAdapter);

    }

    private void loadDataFromDatabase() {
        // Quan sát LiveData từ Room Database
        LiveData<List<Session>> liveSessionList = database.sessionDAO().getAllSessionsOrdered();
        liveSessionList.observe(getViewLifecycleOwner(), new Observer<List<Session>>() {
            @Override
            public void onChanged(List<Session> sessions) {
                if (sessions != null) {
                    sessionList.clear();
                    sessionList.addAll(sessions);
                    sessionAdapter.notifyDataSetChanged();
                }
            }
        });

        // Quan sát LiveData cho Lectures
        LiveData<List<Lecture>> liveLectures = database.lectureDAO().getAllLecturesLive();
        liveLectures.observe(getViewLifecycleOwner(), new Observer<List<Lecture>>() {
            @Override
            public void onChanged(List<Lecture> lecturesList) {
                if (lecturesList != null) {
                    lectures.clear();
                    lectures.addAll(lecturesList);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
