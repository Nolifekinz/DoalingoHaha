package com.example.dualingo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public SessionAdapter(Context context, List<Session> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessionList.get(position);
//        holder.tvSessionTitle.setText(session.getSessionTitle());

        // Thiết lập RecyclerView con cho Lecture
//        LectureAdapter lectureAdapter = new LectureAdapter(context, session.getLectures(), lecture -> {
//            // Xử lý khi click vào lecture
//        });
        holder.rvLectures.setLayoutManager(new GridLayoutManager(context, 2));
//        holder.rvLectures.setAdapter(lectureAdapter);
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionTitle;
        RecyclerView rvLectures;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSessionTitle = itemView.findViewById(R.id.tvSession);
            rvLectures = itemView.findViewById(R.id.rvLectures);
        }
    }
}
