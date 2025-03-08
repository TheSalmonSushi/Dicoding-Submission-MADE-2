package com.salmonboy.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.salmonboy.core.ui.EventAdapter
import com.salmonboy.favorite.databinding.ActivityFavoriteEventBinding
import com.salmonboy.submissionakhir.ui.detail.EventDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteEventBinding

    private val favoriteViewModel: FavoriteEventViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadKoinModules(favoriteModule)

        supportActionBar?.title = "Favorite Event"

        val eventAdapter = EventAdapter()
        eventAdapter.onItemClick = { selectedData ->
            val intent = Intent(this@FavoriteEventActivity, EventDetailActivity::class.java)
            intent.putExtra(EventDetailActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }
        favoriteViewModel.favoriteEvent.observe(this) { events ->
            eventAdapter.submitList(events)
            binding.viewEmpty.root.visibility =
                if (events.isNotEmpty()) View.GONE else View.VISIBLE
        }

        with(binding.rvFavoriteEvent) {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = eventAdapter
        }

    }
}