package hr.markic.budgetmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.databinding.FragmentCharBinding
import hr.markic.budgetmanager.databinding.FragmentMapsBinding


class CharFragment : Fragment() {

    lateinit var binding: FragmentCharBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCharBinding.inflate(inflater, container, false)

        return binding.root
    }

}