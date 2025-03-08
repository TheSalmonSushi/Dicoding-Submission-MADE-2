package com.salmonboy.submissionakhir.ui.setting

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.salmonboy.submissionakhir.databinding.ActivitySettingBinding
import com.salmonboy.submissionakhir.ui.factory.SettingViewModelFactory
import com.salmonboy.submissionakhir.ui.notification.EventNotificationWorker
import java.util.concurrent.TimeUnit

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var workManager: WorkManager

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("SettingActivity", "Notifications permission granted")
            } else {
                Log.d("SettingActivity", "Notifications permission granted")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        workManager = WorkManager.getInstance(this)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }
        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        settingViewModel.getNotificationSettings().observe(this) { isNotificationActive: Boolean ->
            if (isNotificationActive) {
                if (!binding.switchNotification.isChecked) {
                    startPeriodicTask()
                }
                binding.switchNotification.isChecked = true
            } else {
                if (binding.switchNotification.isChecked) {
                    cancelPeriodicTask()
                }
                binding.switchNotification.isChecked = false
            }
        }

        binding.switchNotification.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                startPeriodicTask()
                settingViewModel.saveNotificationSetting(true)
            } else {
                cancelPeriodicTask()
                settingViewModel.saveNotificationSetting(false)
            }
        }
    }

    private fun startPeriodicTask() {
        val data = Data.Builder().build()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(EventNotificationWorker::class.java, 1, TimeUnit.DAYS)
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        workManager.getWorkInfosForUniqueWork("EventNotificationWork")
            .get()
            .let { workInfos ->
                val isAlreadyScheduled = workInfos.any { workInfo ->
                    workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING
                }
                if (!isAlreadyScheduled) {
                    workManager.enqueueUniquePeriodicWork(
                        "EventNotificationWork",
                        ExistingPeriodicWorkPolicy.KEEP,
                        periodicWorkRequest
                    )
                }
            }
    }

    private fun cancelPeriodicTask() {
        workManager.cancelUniqueWork("EventNotificationWork")
    }
}