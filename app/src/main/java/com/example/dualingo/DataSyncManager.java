package com.example.dualingo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.room.Room;

import com.example.dualingo.Models.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DataSyncManager {
    private final AppDatabase database;
    private final FirebaseFirestore firestore;
    private final Executor executor;
    private final SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "sync_prefs";
    private static final String LAST_SYNC_KEY = "last_sync_time";
    private static final long SYNC_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24 giờ

    public DataSyncManager(Context context) {
//        context.deleteDatabase("dualingo_db");
        database = Room.databaseBuilder(context, AppDatabase.class, "dualingo_db").build();
        firestore = FirebaseFirestore.getInstance();
        executor = Executors.newSingleThreadExecutor();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Kiểm tra trạng thái mạng
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    // Kiểm tra nếu cần đồng bộ
    private boolean shouldSync() {
        long lastSyncTime = sharedPreferences.getLong(LAST_SYNC_KEY, 0);
        long currentTime = System.currentTimeMillis();
        return currentTime - lastSyncTime > SYNC_INTERVAL_MS;
    }

    // Lưu timestamp sau khi đồng bộ
    private void saveLastSyncTime() {
        sharedPreferences.edit().putLong(LAST_SYNC_KEY, System.currentTimeMillis()).apply();
    }

    // Thực hiện đồng bộ trên Background Thread
    public void syncData(Context context) {
        if (!isNetworkAvailable(context)) {
            // Không có mạng, thoát
            return;
        }

//        if (!shouldSync()) {
//            return;
//        }

        executor.execute(() -> {
            // Đồng bộ từng collection
            syncCollection("Lecture", Lecture.class, data -> database.lectureDAO().insertLecture((Lecture) data));
            syncCollection("Arranging", Arranging.class, data -> database.arrangingDAO().insertArranging((Arranging) data));
            syncCollection("FillBlank", FillBlank.class, data -> database.fillBlankDAO().insertFillBlank((FillBlank) data));
            syncCollection("Grammar", Grammar.class, data -> database.grammarDAO().insertGrammar((Grammar) data));
            syncCollection("Introduction", Introduction.class, data -> database.introductionDAO().insertIntroduction((Introduction) data));
            syncCollection("Listening", Listening.class, data -> database.listeningDAO().insertListening((Listening) data));
            syncCollection("Speaking", Speaking.class, data -> database.speakingDAO().insertSpeaking((Speaking) data));
            syncCollection("Vocabulary", Vocabulary.class, data -> database.vocabularyDAO().insertVocabulary((Vocabulary) data));
            syncCollection("VocabularyLesson", VocabularyLesson.class, data -> database.vocabularyLessonDAO().insertVocabularyLesson((VocabularyLesson) data));
            syncCollection("Session", Session.class, data -> database.sessionDAO().insert((Session) data));
            syncCollection("WrongQuestion", WrongQuestion.class, data -> database.wrongQuestionDAO().insertWrongQuestion((WrongQuestion) data));
            syncUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
            // Lưu timestamp sau khi hoàn tất
            saveLastSyncTime();
        });
    }

    // Hàm dùng chung để đồng bộ dữ liệu từ Firestore vào Room
    private <T> void syncCollection(String collectionName, Class<T> modelClass, DataCallback<T> callback) {
        firestore.collection(collectionName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (com.google.firebase.firestore.DocumentSnapshot doc : task.getResult().getDocuments()) {
                    T data = doc.toObject(modelClass);
                    if (data != null) {
                        executor.execute(() -> callback.onDataSynced(data));
                    }
                }
            }
        });
    }

    // Thêm hàm syncUser trong DataSyncManager
    public void syncUser(String currentUserId) {
        executor.execute(() -> {
            firestore.collection("users")
                    .whereEqualTo("id", currentUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (com.google.firebase.firestore.DocumentSnapshot doc : task.getResult().getDocuments()) {
                                User user = doc.toObject(User.class);
                                if (user != null) {
                                    executor.execute(() -> database.userDAO().insertUser(user));
                                }
                            }
                            saveLastSyncTime(); // Cập nhật thời gian đồng bộ
                        }
                    });
        });
    }


    // Giao diện callback để xử lý dữ liệu
    public interface DataCallback<T> {
        void onDataSynced(T data);
    }
}
