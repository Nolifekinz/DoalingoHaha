package com.example.dualingo.Adapters;
import com.example.dualingo.DAO.LectureDAO;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.Session;
import com.example.dualingo.R;
import com.example.dualingo.AppDatabase;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private Context context;
    private List<Session> sessionList;
    private LectureAdapter.OnLectureClickListener listener;
    private AppDatabase appDatabase;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public SessionAdapter(Context context, List<Session> sessionList, LectureAdapter.OnLectureClickListener listener) {
        this.context = context;
        this.sessionList = sessionList;
        this.listener = listener;
        this.appDatabase = AppDatabase.getDatabase(context);
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

        // Set session title text
        holder.tvSessionTitle.setText(session.getSessionTitle());

        // Set image for session
        holder.image_session_title.setImageResource(R.drawable.fire);

        // Fetch lectures from Room Database
        fetchLecturesFromRoom(session.getLecturesId(), holder);

        // Check if session is unlocked
        if (session.getIsUnlocked() == 0) {
            // Show overlay if session is locked
            holder.overlay.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false); // Disable click

            // Handle session click to show unlock dialog
            holder.overlay.setOnClickListener(v -> showUnlockDialog(session));
        } else {
            // Hide overlay if session is unlocked
            holder.overlay.setVisibility(View.GONE);
            holder.itemView.setEnabled(true); // Enable click
        }
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    private void fetchLecturesFromRoom(List<String> lecturesId, SessionViewHolder holder) {
        // Use LiveData to fetch data asynchronously from Room
        LectureDAO lectureDao = appDatabase.lectureDAO();

        LiveData<List<Lecture>> lecturesLiveData = lectureDao.getLectureByIds(lecturesId);

        // Observer to update UI when data is fetched from Room
        lecturesLiveData.observeForever(new Observer<List<Lecture>>() {
            @Override
            public void onChanged(List<Lecture> lectures) {
                if (lectures != null && !lectures.isEmpty()) {
                    // Set up RecyclerView with LectureAdapter
                    LectureAdapter lectureAdapter = new LectureAdapter(context, lectures, listener);
                    holder.rvLectures.setLayoutManager(new GridLayoutManager(context, 1));
                    holder.rvLectures.setAdapter(lectureAdapter);
                } else {
                    Toast.makeText(context, "No lectures found", Toast.LENGTH_SHORT).show();
                }
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
        session.setIsUnlocked(1); // Update the session state to unlocked

        // Update session in the database
        executor.execute(() -> {
            appDatabase.sessionDAO().update(session);

            // Refresh UI after unlocking the session
            ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
        });
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionTitle;
        RecyclerView rvLectures;
        ImageView image_session_title;
        View overlay; // Overlay view

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            image_session_title = itemView.findViewById(R.id.image_session_title);
            tvSessionTitle = itemView.findViewById(R.id.session_title);
            rvLectures = itemView.findViewById(R.id.rvLectures);
            overlay = itemView.findViewById(R.id.overlay); // Initialize overlay
        }
    }
}
