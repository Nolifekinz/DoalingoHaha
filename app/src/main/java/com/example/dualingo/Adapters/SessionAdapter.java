package com.example.dualingo.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Session;
import com.example.dualingo.R;
import com.example.dualingo.AppDatabase;
import com.example.dualingo.DAO.LectureDAO;
import com.example.dualingo.Models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.Executors;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private Context context;
    private List<Session> sessionList;
    private LectureAdapter.OnLectureClickListener listener;
    private AppDatabase appDatabase;
    private User currentUser; // Lưu trữ người dùng hiện tại

    public SessionAdapter(Context context, List<Session> sessionList, LectureAdapter.OnLectureClickListener listener) {
        this.context = context;
        this.sessionList = sessionList;
        this.listener = listener;
        this.appDatabase = AppDatabase.getDatabase(context);
        Executors.newSingleThreadExecutor().execute(() -> {
            currentUser = appDatabase.userDAO().getCurrentUser();

        });
    }


    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for session item
        View view = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessionList.get(position);

        if (currentUser.getProgress().getSessionIndex() >= position+1) {
            holder.overlay.setVisibility(View.GONE); // Session được mở
        } else {
            holder.overlay.setVisibility(View.VISIBLE); // Session bị khóa
        }

        holder.tvSessionTitle.setText(session.getSessionTitle());
        holder.image_session_title.setImageResource(R.drawable.fire);

        fetchLecturesFromRoom(session.getLecturesId(), holder, position);
    }



    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    private void fetchLecturesFromRoom(List<String> lecturesId, SessionViewHolder holder, int sessionPosition) {
        LectureDAO lectureDao = appDatabase.lectureDAO();

        LiveData<List<Lecture>> lecturesLiveData = lectureDao.getLectureByIdsOrdered(lecturesId);

        lecturesLiveData.observeForever(lectures -> {
            if (lectures != null && !lectures.isEmpty()) {
                LectureAdapter lectureAdapter = new LectureAdapter(context, lectures, sessionPosition, listener, currentUser);
                holder.rvLectures.setLayoutManager(new GridLayoutManager(context, 1));
                holder.rvLectures.setAdapter(lectureAdapter);
            }
        });
    }


    // Show dialog to unlock session
    private void showUnlockDialog(Session session) {
        new AlertDialog.Builder(context)
                .setTitle("Unlock Session")
                .setMessage("Do you want to unlock this session?")
                .setPositiveButton("Yes", (dialog, which) -> unlockSession(session))
                .setNegativeButton("No", null)
                .show();
    }

    // Unlock the session and update its state in the database
    private void unlockSession(Session session) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int currentSessionIndex = sessionList.indexOf(session);
            if (currentUser.getProgress().getSessionIndex() < currentSessionIndex) {
                currentUser.getProgress().setSessionIndex(currentSessionIndex);
                appDatabase.userDAO().updateUser(currentUser);

                // Refresh RecyclerView để cập nhật giao diện
                ((Activity) context).runOnUiThread(this::notifyDataSetChanged);
            }
        });
    }


    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionTitle;
        RecyclerView rvLectures;
        ImageView image_session_title;
        View overlay;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            image_session_title = itemView.findViewById(R.id.image_session_title);
            tvSessionTitle = itemView.findViewById(R.id.session_title);
            rvLectures = itemView.findViewById(R.id.rvLectures);
            overlay = itemView.findViewById(R.id.overlay);
        }
    }
}
