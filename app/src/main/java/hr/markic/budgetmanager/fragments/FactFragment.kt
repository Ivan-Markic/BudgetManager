package hr.markic.budgetmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.markic.budgetmanager.ItemAdapter
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.databinding.FragmentFactBinding
import hr.markic.budgetmanager.databinding.FragmentMapsBinding
import hr.markic.budgetmanager.framework.fetchItems
import hr.markic.budgetmanager.model.Item


class FactFragment : Fragment() {

    lateinit var binding:FragmentFactBinding
    private lateinit var items: MutableList<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFactBinding.inflate(inflater, container, false)
        items = requireContext().fetchItems()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemAdapter(context, items)
        }
    }
}