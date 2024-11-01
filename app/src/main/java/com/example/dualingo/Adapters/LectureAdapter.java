package com.example.dualingo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.Models.Lecture;
import com.example.dualingo.R;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> {
    private Context context;
    private List<Lecture> lectureList;
    private OnLectureClickListener listener;

    public interface OnLectureClickListener {
        void onLectureClick(Lecture lecture);
    }

    public LectureAdapter(Context context, List<Lecture> lectureList, OnLectureClickListener listener) {
        this.context = context;
        this.lectureList = lectureList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LectureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_lecture, parent, false);
        return new LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder holder, int position) {
        Lecture lecture = lectureList.get(position);
//        holder.tvLectureTitle.setText(lecture.getTitle());
//        holder.imgLecture.setImageResource(lecture.getImageResId()); // Assuming you have an image resource ID

        holder.itemView.setOnClickListener(v -> listener.onLectureClick(lecture));
    }

    @Override
    public int getItemCount() {
        return lectureList.size();
    }

    public static class LectureViewHolder extends RecyclerView.ViewHolder {
        ImageButton imgLecture;
        TextView tvLectureTitle;

        public LectureViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLecture = itemView.findViewById(R.id.imgLecture);
            tvLectureTitle = itemView.findViewById(R.id.tvLectureTitle);
        }
    }
}
