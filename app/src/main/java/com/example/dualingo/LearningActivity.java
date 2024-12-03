package com.example.dualingo;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dualingo.LearningFragment.ArrangingFragment;
import com.example.dualingo.LearningFragment.FillInBlankFragment;
import com.example.dualingo.LearningFragment.LearnNewWordsFragment;
import com.example.dualingo.LearningFragment.ListeningFragment;
import com.example.dualingo.LearningFragment.SpeakingFragment;

public class LearningActivity extends AppCompatActivity {

    private DataSyncManager dataSyncManager;
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning);
        dataSyncManager = new DataSyncManager(this);
        dataSyncManager.observeRoomChanges(this);

        // Áp dụng padding cho thanh trạng thái và điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LearningActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Chuyển sang Fragment phù hợp
        String typeQuestion = getIntent().getStringExtra("typeQuestion");
        if (typeQuestion != null) {
            switchFragment(typeQuestion);
        } else {
            finish();
        }
    }

    private void switchFragment(String typeQuestion) {
        Fragment fragment;
        String lectureId = getIntent().getStringExtra("lectureId");
        Bundle bundle = new Bundle();

        // Chọn Fragment dựa vào loại câu hỏi
        switch (typeQuestion) {
            case "arranging":
                fragment = new ArrangingFragment();
                break;
            case "fill_blank":
                fragment = new FillInBlankFragment();
                break;
            case "listening":
                fragment = new ListeningFragment();
                break;
            case "speaking":
                fragment = new SpeakingFragment();
                break;
            default:
                fragment = new LearnNewWordsFragment();
        }

        // Truyền dữ liệu vào Fragment
        bundle.putString("testOrLearn", "learn");
        bundle.putString("lectureId", lectureId);
        fragment.setArguments(bundle);

        // Thay thế Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.LearningFragment, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Kiểm tra mạng và đồng bộ khi chuyển từ offline sang online
        dataSyncManager.syncData(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký quan sát nếu cần thiết
        dataSyncManager.stopObservingRoomChanges();
    }
    @Override
    protected void onStart() {
        super.onStart();

        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}
