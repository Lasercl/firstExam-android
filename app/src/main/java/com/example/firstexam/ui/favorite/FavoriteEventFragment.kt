package com.example.firstexam.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstexam.DetailActivity
import com.example.firstexam.R
import com.example.firstexam.adapter.ListEventAdapter
import com.example.firstexam.databinding.FragmentFavoriteEventBinding
import com.example.firstexam.databinding.FragmentFinishedEventBinding
import com.example.firstexam.response.ListEventsItem
import com.example.firstexam.ui.event.FinishedEventViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteEventFragment : Fragment() {
    private var _binding: FragmentFavoriteEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteEventBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favoriteEventBinding =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[FavoriteEventViewModel::class.java]
        favoriteEventBinding.listItem.observe(viewLifecycleOwner) { it ->
            setEventData(it)
        }
        favoriteEventBinding.isLoading.observe(viewLifecycleOwner){it->
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