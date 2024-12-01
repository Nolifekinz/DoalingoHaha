package com.example.dualingo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dualingo.LearningActivity;
import com.example.dualingo.ListBaiHoc;
import com.example.dualingo.Models.Lecture;
import com.example.dualingo.Models.User;
import com.example.dualingo.R;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> {
    private Context context;
    private List<Lecture> lectureList;
    private int sessionPosition;
    private OnLectureClickListener listener;
    private User currentUser; // Lưu trữ người dùng hiện tại

    public interface OnLectureClickListener {
        void onLectureClick(int sessionPosition, int lecturePosition);
    }

    public LectureAdapter(Context context, List<Lecture> lectureList, int sessionPosition, OnLectureClickListener listener, User user) {
        this.context = context;
        this.lectureList = lectureList;
        this.sessionPosition = sessionPosition; // Gán sessionPosition vào adapter
        this.listener = listener;
        this.currentUser = user;
    }

    @NonNull
    @Override
    public LectureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture, parent, false);
        return new LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder holder, int position) {
        Lecture lecture = lectureList.get(position);

        // Lấy orderIndex của bài học hiện tại
        int lectureOrderIndex = lecture.getOrderIndex();

        // Lấy lectureIndex từ tiến trình của người dùng
        int userLectureIndex = currentUser.getProgress().getLectureIndex();

        // So sánh orderIndex với lectureIndex
        if (lectureOrderIndex <= userLectureIndex) {
            holder.overlay.setVisibility(View.GONE); // Hiển thị bài học nếu người dùng đã đạt tiến trình
        } else {
            holder.overlay.setVisibility(View.VISIBLE); // Ẩn bài học nếu chưa đạt tiến trình
        }

        // Thiết lập ảnh và tên cho bài học
        holder.tvLectureTitle.setText(lecture.getTitle());

        // Lắng nghe sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (holder.overlay.getVisibility() == View.GONE) {
                listener.onLectureClick(sessionPosition, position); // Chỉ cho phép click nếu bài học được mở

                // Mở Activity mới và truyền ID của Lecture sang
                Intent intent = new Intent(context, ListBaiHoc.class);
                intent.putExtra("lectureId", lecture.getIdLecture());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return lectureList.size();
    }

    public static class LectureViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLecture;
        TextView tvLectureTitle;
        View overlay;

        public LectureViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLecture = itemView.findViewById(R.id.imgLecture);
            tvLectureTitle = itemView.findViewById(R.id.tvLectureTitle);
            overlay = itemView.findViewById(R.id.lecture_overlay);
        }
    }
}
