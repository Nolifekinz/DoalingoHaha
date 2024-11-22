package com.example.dualingo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Tạo NotificationChannel nếu chưa có (cho Android 8.0 trở lên)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký NotificationChannel với hệ thống
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Tạo thông báo
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Time to Study!")
                .setContentText("You haven't started your lesson yet. Let's study!")
                .setSmallIcon(R.drawable.iconapp)  // Thêm biểu tượng thông báo
                .setAutoCancel(true)  // Thêm cờ AutoCancel để đóng thông báo khi người dùng nhấn vào
                .build();

        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }
}
 