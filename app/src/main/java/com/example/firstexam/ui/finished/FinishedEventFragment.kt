package com.example.firstexam.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstexam.DetailActivity
import com.example.firstexam.adapter.ListEventAdapter
import com.example.firstexam.databinding.FragmentFinishedEventBinding
import com.example.firstexam.response.ListEventsItem
import com.example.firstexam.ui.event.FinishedEventViewModel
import com.example.firstexam.ui.event.ListEventViewModel

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.onViewCreated(view, savedInstanceState)
        val finishedListEventViewModel =
            ViewModelProvider(this).get(FinishedEventViewModel::class.java)

        finishedListEventViewModel.listItem.observe(viewLifecycleOwner) { it ->
            setEventData(it)
        }
        finishedListEventViewModel.isLoading.observe(viewLifecycleOwner){it->
            showLoading(it)
        }
    }
    private fun setEventData(it: List<ListEventsItem>?) {
        val adapter = ListEventAdapter()
        adapter.setOnItemClickListener { selectedEvent ->
            var intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("detail_item", selectedEvent)
            startActivity(intent)
        }
        adapter.submitList(it)
        binding.rvHeroes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHeroes.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}