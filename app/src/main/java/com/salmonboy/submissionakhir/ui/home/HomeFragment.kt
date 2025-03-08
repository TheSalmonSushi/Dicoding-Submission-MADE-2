package com.salmonboy.submissionakhir.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.salmonboy.core.data.Resource
import com.salmonboy.core.ui.EventAdapter
import com.salmonboy.submissionakhir.R
import com.salmonboy.submissionakhir.databinding.FragmentHomeBinding
import com.salmonboy.submissionakhir.ui.detail.EventDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       if (activity != null) {
           val eventAdapter = EventAdapter()
           eventAdapter.onItemClick = { selectedData ->
               val intent = Intent(activity, EventDetailActivity::class.java)
               intent.putExtra(EventDetailActivity.EXTRA_DATA, selectedData)
               startActivity(intent)
           }

           homeViewModel.events.observe(viewLifecycleOwner) {events ->
               if (events != null) {
                   when (events) {
                       is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                       is Resource.Success -> {
                           binding.progressBar.visibility = View.GONE
                           eventAdapter.submitList(events.data)
                       }
                       is Resource.Error -> {
                           binding.progressBar.visibility = View.GONE
                           binding.viewError.root.visibility = View.VISIBLE
                           binding.viewError.tvError.text =
                               events.message ?: getString(R.string.something_wrong)
                       }
                   }
               }
           }

           with(binding.rvEvent) {
               layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
               setHasFixedSize(true)
               adapter = eventAdapter
           }
       }
    }
}