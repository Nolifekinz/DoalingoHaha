package com.example.dualingo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            // Nếu có mạng, thực hiện đồng bộ
            DataSyncManager syncManager = new DataSyncManager(context);
            syncManager.syncRoomToFirestore(); // Đồng bộ từ Room lên Firestore
            syncManager.syncCompletedLessonToFirestore();
            syncManager.syncWrongQuestionToFirestore();
            syncManager.syncData(context); // Đồng bộ từ Firestore về Room
        }
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
}
