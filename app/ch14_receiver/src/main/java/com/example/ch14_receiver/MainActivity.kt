package com.example.ch14_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ch14_receiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var batteryReceiver: BroadcastReceiver

    // 1. 권한 요청 런처를 클래스 멤버로 분리
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission() // 단일 권한 요청으로 변경
    ) { isGranted ->
        if (isGranted) {
            sendBroadcast(Intent(this, MyReceiver::class.java))
        } else {
            Toast.makeText(this, "Permission denied...", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBatteryReceiver()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        // 2. 화면이 활성화될 때 배터리 리시버 등록
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        // 3. 화면이 비활성화될 때 배터리 리시버 해제 (메모리 누수 방지)
        unregisterReceiver(batteryReceiver)
    }

    // 뷰와 관련된 초기 설정
    private fun initViews() {
        binding.button.setOnClickListener {
            sendNotificationBroadcast()
        }
    }

    // 배터리 상태 변경을 감지하는 리시버 초기화
    private fun initBatteryReceiver() {
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // 4. 배터리 상태가 변경될 때마다 이 부분이 호출됨
                when (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                    BatteryManager.BATTERY_STATUS_CHARGING -> {
                        when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                            BatteryManager.BATTERY_PLUGGED_USB -> {
                                binding.chargingResultView.text = "USB Plugged"
                                binding.chargingImageView.setImageResource(R.drawable.usb)
                            }
                            BatteryManager.BATTERY_PLUGGED_AC -> {
                                binding.chargingResultView.text = "AC Plugged"
                                binding.chargingImageView.setImageResource(R.drawable.ac)
                            }
                        }
                    }
                    else -> {
                        binding.chargingResultView.text = "Not Plugged"
                        binding.chargingImageView.setImageResource(0) // 기본 이미지 없음
                    }
                }
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPct = level / scale.toFloat() * 100
                binding.percentResultView.text = "${batteryPct.toInt()} %"
            }
        }
    }

    // 알림 브로드캐스트를 보내는 함수
    private fun sendNotificationBroadcast() {
        // 5. TIRAMISU (API 33) 이상 버전에서만 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    "android.permission.POST_NOTIFICATIONS"
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // 권한이 이미 있으면 바로 브로드캐스트 전송
                sendBroadcast(Intent(this, MyReceiver::class.java))
            } else {
                // 권한이 없으면 요청
                permissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }
        } else {
            // 하위 버전에서는 권한 없이 바로 전송
            sendBroadcast(Intent(this, MyReceiver::class.java))
        }
    }
}