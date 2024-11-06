package com.example.dualingo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Models.Session;
import com.example.dualingo.R;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private Context context;
    private List<Session> sessionList;
    private LectureAdapter.OnLectureClickListener listener;

    public SessionAdapter(Context context, List<Session> sessionList,LectureAdapter.OnLectureClickListener listener) {
        this.context = context;
        this.sessionList = sessionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessionList.get(position);
        // Thiết lập ảnh chủ đề và tên chủ đề cho session
        holder.tvSessionTitle.setText(session.getSessionTitle());
        holder.image_session_title.setImageResource(session.getImgUrl());

        // Thiết lập RecyclerView con cho Lecture
        LectureAdapter lectureAdapter = new LectureAdapter(context, session.getLectures(), listener);

        // Kết nối adapter cho rv lecture
        holder.rvLectures.setLayoutManager(new GridLayoutManager(context, 1));
        holder.rvLectures.setAdapter(lectureAdapter);
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionTitle;
        RecyclerView rvLectures;
        ImageView image_session_title;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            image_session_title = itemView.findViewById(R.id.image_session_title);
            tvSessionTitle = itemView.findViewById(R.id.session_title);
            rvLectures = itemView.findViewById(R.id.rvLectures);
        }

    }
}
