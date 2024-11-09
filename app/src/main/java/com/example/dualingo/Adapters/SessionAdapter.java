package com.example.dualingo.Adapters;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private Context context;
    private List<Session> sessionList;
    private LectureAdapter.OnLectureClickListener listener;
    private FirebaseFirestore firestore;

    public SessionAdapter(Context context, List<Session> sessionList, LectureAdapter.OnLectureClickListener listener) {
        this.context = context;
        this.sessionList = sessionList;
        this.listener = listener;
        this.firestore = FirebaseFirestore.getInstance(); // Khởi tạo FirebaseFirestore
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

//        if (session.getImgUrl() != null && !session.getImgUrl().isEmpty()) {
//            Glide.with(context)
//                    .load(session.getImgUrl())
//                    .into(holder.image_session_title);
//        } else {
//            holder.image_session_title.setImageResource(R.drawable.fire);
//        }
        holder.image_session_title.setImageResource(R.drawable.fire);
        // Fetch lectures from Firestore based on lecturesId
        fetchLecturesFromFirestore(session.getLecturesId(), holder);


//        if (session.getLecturesId() != null && !session.getLecturesId().isEmpty()) {
//            // Create and set up the LectureAdapter
//            LectureAdapter lectureAdapter = new LectureAdapter(context, session.getLecturesId(), listener);
//            holder.rvLectures.setLayoutManager(new GridLayoutManager(context, 1));
//            holder.rvLectures.setAdapter(lectureAdapter);
//        }
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    private void fetchLecturesFromFirestore(List<String> lecturesId, SessionViewHolder holder) {
        firestore.collection("Lecture")  // Giả sử bạn có một collection 'lectures' trong Firestore
                .whereIn("idLecture", lecturesId)  // Truy vấn theo danh sách lectureId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Nếu truy vấn thành công, lấy dữ liệu và chuyển đổi thành List<Lecture>
                        List<Lecture> lectures = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot document : querySnapshot) {
                            // Tạo đối tượng Lecture từ dữ liệu Firestore
                            Lecture lecture = document.toObject(Lecture.class);
                            lectures.add(lecture);
                        }

                        // Cập nhật RecyclerView với dữ liệu bài giảng
                        if (lectures != null && !lectures.isEmpty()) {
                            // Thiết lập lại adapter cho rvLectures
                            LectureAdapter lectureAdapter = new LectureAdapter(context, lectures, listener);
                            holder.rvLectures.setLayoutManager(new GridLayoutManager(context, 1));
                            holder.rvLectures.setAdapter(lectureAdapter);
                        }
                    } else {
                        // Nếu truy vấn không thành công, xử lý thất bại
                        // Ví dụ: bạn có thể hiển thị thông báo lỗi ở đây
                        Log.e("Firestore", "Lỗi truy vấn dữ liệu: " + task.getException().getMessage());
                        // Hiển thị thông báo lỗi cho người dùng
                        Toast.makeText(context, "Lỗi khi tải dữ liệu bài giảng.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý thất bại khi gặp sự cố trong quá trình truy vấn
                    Log.e("Firestore", "Truy vấn thất bại: ", e);
                    // Thông báo cho người dùng khi có lỗi nghiêm trọng
                    Toast.makeText(context, "Không thể kết nối với Firestore. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                });

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
