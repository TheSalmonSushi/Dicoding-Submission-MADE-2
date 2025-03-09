package com.salmonboy.submissionakhir.ui.detail

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.salmonboy.core.domain.model.Event
import com.salmonboy.submissionakhir.R
import com.salmonboy.submissionakhir.databinding.ActivityEventDetailBinding
import com.salmonboy.submissionakhir.ui.setting.SettingPreferences
import com.salmonboy.submissionakhir.ui.setting.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private val eventDetailViewModel: EventDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        darkModeApplier()
        hideBookmark()

        val detailEvent = intent.getParcelableExtra<Event>(EXTRA_DATA)
        showEventDetail(detailEvent)
    }

    private fun showEventDetail(eventDetail: Event?) {
        eventDetail?.let {
            bindItem(eventDetail)
        }
    }

    private fun darkModeApplier() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        lifecycleScope.launch {
            val isDarkModeActive = pref.getThemeSetting().first()
            applyTheme(isDarkModeActive)

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

    private fun bindItem(eventDetail: Event) {
        // Title and Organizer
        binding.tvEventTitle.text = eventDetail.name
        val text = "Diselenggarakan Oleh \n${eventDetail.ownerName}"
        binding.tvEventOrganiser.text = text

        // Description
        binding.tvEventDesc.text = HtmlCompat.fromHtml(
            eventDetail.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        // Date, Quota, Registrants, and Image
        val formattedDate = formatDate(eventDetail.beginTime)
        binding.tvEventDate.text = formattedDate
        val formattedQuotaLeft = "Sisa Kuota: ${eventDetail.quota - eventDetail.registrants}"
        binding.tvEventQuota.text = formattedQuotaLeft
        Glide.with(this).load(eventDetail.imageLogo).into(binding.ivItemPhoto)

        // Link Button
        binding.linkEventButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventDetail.link))
            startActivity(intent)
        }

        //BookMark
        var statusFavorite = eventDetail.isBookmarked
        showBookmarkState(statusFavorite)
        binding.ivBookmark.setOnClickListener {
            statusFavorite = !statusFavorite
            eventDetailViewModel.setFavoriteEvent(eventDetail, statusFavorite)
            showBookmarkState(statusFavorite)
        }
    }

    private fun hideBookmark() {
        var isCardVisible = true
        binding.main.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            binding.cvBookmark.let { cvBookmark ->
                if (scrollY > oldScrollY && isCardVisible) {
                    cvBookmark.height.let {
                        cvBookmark.animate()
                            .translationY(it.toFloat())
                            .alpha(0f)
                            .setDuration(200)
                            .withEndAction {
                                isCardVisible = false
                            }
                            .start()
                    }
                } else if (scrollY < oldScrollY && !isCardVisible) {
                    cvBookmark.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setDuration(200)
                        .withEndAction { isCardVisible = true }
                        .start()
                }
            }
        }
    }

    private fun showBookmarkState(isFavorited: Boolean) {
        if (isFavorited) {
            binding.ivBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_fill_24dp))
        } else {
            binding.ivBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_24dp))
        }
    }

    private fun formatDate(apiDate: String): String {
        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val desiredDateFormat = SimpleDateFormat("dd MMMM yyyy\n HH:mm", Locale.getDefault())

        return try {
            val date = apiDateFormat.parse(apiDate)
            desiredDateFormat.format(date)
        } catch (e: Exception) {
            apiDate
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }


}