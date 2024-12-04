package com.example.dualingo;

import android.content.Context;
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
import android.widget.Toast;

import com.example.dualingo.Adapters.LectureAdapter;
import com.example.dualingo.Adapters.SessionAdapter;
import com.example.dualingo.Models.CompletedLesson;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Session;
import com.example.dualingo.Models.User;
import com.example.dualingo.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<Session> sessionList = new ArrayList<>();
    private List<Lecture> lectures = new ArrayList<>();
    private SessionAdapter sessionAdapter;
    private RecyclerView recyclerViewSession;
    private TextView level, streak;
    private ImageView flag, notify;
    private String sessionid;
    private int sessionIndex;

    private AppDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        DataSyncManager dataSyncManager = new DataSyncManager(getContext());
        dataSyncManager.syncData(getContext());
        recyclerViewSession = view.findViewById(R.id.rvSession);
        level = view.findViewById(R.id.level);
        streak = view.findViewById(R.id.streak);
        flag = view.findViewById(R.id.flag_language);
        notify = view.findViewById(R.id.notify);

        level.setOnClickListener(v -> showPopup(R.id.level, v));
        streak.setOnClickListener(v -> showPopup(R.id.streak, v));
        flag.setOnClickListener(v -> showPopup(R.id.flag_language, v));
        notify.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SetTimeActivity.class);
            startActivity(intent);
        });

        database = AppDatabase.getDatabase(getContext());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            User user = database.userDAO().getCurrentUser();
            if (user != null && user.getProgress() != null) {
                int sessionIndex = user.getProgress().getSessionIndex();
                int lectureIndex = user.getProgress().getLectureIndex();

                // Lấy sessionId từ sessionIndex
                String sessionId = getSessionIdFromIndex(sessionIndex, getContext());

                if (sessionId != null) {
                    // Lấy danh sách Lecture theo sessionId
                    List<String> lectureIdList = database.sessionDAO().getSessionById(sessionId).getLecturesId();
                    List<Lecture> lectureList = database.lectureDAO().getLectureByIds(lectureIdList);
                    if (lectureList != null && !lectureList.isEmpty()) {
                        boolean foundNextLecture = false;

                        // Kiểm tra Lecture có lectureIndex + 1 trong session không
                        for (Lecture lecture : lectureList) {
                            if (lecture.getOrderIndex() == lectureIndex + 1) {
                                lectureIndex++;
                                foundNextLecture = true;
                                break;
                            }
                        }

                        if (!foundNextLecture) {
                            // Nếu không tìm thấy lectureIndex++, chuyển sang session mới
                            sessionIndex++;
                            lectureIndex = 1; // Đặt lại bài học đầu tiên của session mới
                        }

                        // Cập nhật Progress
                        user.getProgress().setSessionIndex(sessionIndex);
                        user.getProgress().setLectureIndex(lectureIndex);
                        database.userDAO().updateUser(user);
                    } else {
                        System.out.println("Danh sách Lecture trống hoặc không tồn tại!");
                    }
                } else {
                    System.out.println("SessionId không hợp lệ!");
                }
            }
        });




        loadDataFromDatabase();
        setupRecyclerViews();

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

    public String getSessionIdFromIndex(int sessionIndex, Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);

        // Lấy danh sách Lecture từ cơ sở dữ liệu
        List<Session> sessionList = database.sessionDAO().getAllSessions();

        // Tìm kiếm lectureId từ lectureIndex
        if (sessionIndex > 0 && sessionIndex <= sessionList.size()) {
            return sessionList.get(sessionIndex - 1).getIdSession();
        } else {
            // Trả về null nếu index không hợp lệ
            return null;
        }
    }



    private boolean isLectureUnlocked(String lectureId) {
        // Lấy CompletedLesson từ Room
        CompletedLesson completedLesson = database.completedLessonDAO().getCompletedLesson(
                database.userDAO().getCurrentUser().getId(),
                lectureId
        );

        // Kiểm tra trạng thái hoàn thành
        return completedLesson != null &&
                completedLesson.getArranging() == 1 &&
                completedLesson.getFillBlank() == 1 &&
                completedLesson.getListening() == 1 &&
                completedLesson.getSpeaking() == 1 &&
                completedLesson.getVocabularyLesson() == 1;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
