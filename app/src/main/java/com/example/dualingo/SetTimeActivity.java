package com.example.dualingo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SetTimeActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button setTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        timePicker = findViewById(R.id.timePicker);
        setTimeButton = findViewById(R.id.setTimeButton);

        setTimeButton.setOnClickListener(v -> setNotificationTime());
    }

    private void setNotificationTime() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Tạo đối tượng Calendar để cài đặt thời gian thông báo
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Đảm bảo thời gian thông báo là trong ngày, nếu qua rồi thì đặt vào ngày mai
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        // Kiểm tra quyền SCHEDULE_EXACT_ALARM
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Kiểm tra quyền SCHEDULE_EXACT_ALARM
                if (!alarmManager.canScheduleExactAlarms()) {
                    Toast.makeText(this, "Ứng dụng cần quyền đặt thông báo chính xác!", Toast.LENGTH_SHORT).show();
                    // Bạn có thể mở giao diện yêu cầu quyền bằng cách:
                    Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                    return;
                }
            }

            // Tạo PendingIntent để kích hoạt NotificationReceiver
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    new Intent(this, NotificationReceiver.class),
                    PendingIntent.FLAG_IMMUTABLE
            );

            try {
                // Đặt thông báo chính xác
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(this, "Đã đặt thời gian thông báo!", Toast.LENGTH_SHORT).show();
            } catch (SecurityException e) {
                Toast.makeText(this, "Lỗi khi đặt thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

}
