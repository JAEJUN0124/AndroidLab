package com.example.ch14_receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ScreenStateService : Service() {

    private val screenStateReceiver = ScreenStateReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 포그라운드 서비스를 시작하기 위한 Notification 설정
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, "screen_state_channel")
            .setContentTitle("화면 상태 감지 서비스")
            .setContentText("서비스가 실행 중입니다.")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 아이콘을 적절히 변경해주세요.
            .build()

        startForeground(1, notification)

        // 리시버 등록
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateReceiver, filter)

        // 서비스가 시스템에 의해 종료될 경우 다시 시작하도록 설정
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // 서비스가 종료될 때 리시버 해제
        unregisterReceiver(screenStateReceiver)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "screen_state_channel",
                "Screen State Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null // 이 서비스는 바인딩을 사용하지 않음
    }
}