package com.salmonboy.submissionakhir

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.salmonboy.submissionakhir.ui.setting.SettingPreferences
import com.salmonboy.submissionakhir.ui.setting.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val pref = SettingPreferences.getInstance(application.dataStore)
        lifecycleScope.launch {
            val isDarkModeActive = pref.getThemeSetting().first()
            applyTheme(isDarkModeActive)

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun applyTheme(isDarkModeActive: Boolean) {
        val mode = if (isDarkModeActive) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

}