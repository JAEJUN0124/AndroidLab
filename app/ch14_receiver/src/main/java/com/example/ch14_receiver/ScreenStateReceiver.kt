package com.example.ch14_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScreenStateReceiver : BroadcastReceiver() {

    // Logcat 필터링을 위한 태그
    private val TAG = "ScreenStateReceiver"

    // 시스템에서 브로드캐스트를 보낼 때 이 함수가 호출됩니다.
    override fun onReceive(context: Context?, intent: Intent?) {
        // intent에 담겨온 action 종류에 따라 분기합니다.
        when (intent?.action) {
            // 화면이 켜졌을 때의 action
            Intent.ACTION_SCREEN_ON -> {
                Log.d(TAG, "화면이 켜졌습니다.")
            }
            // 화면이 꺼졌을 때의 action
            Intent.ACTION_SCREEN_OFF -> {
                Log.d(TAG, "화면이 꺼졌습니다.")
            }
        }
    }
}