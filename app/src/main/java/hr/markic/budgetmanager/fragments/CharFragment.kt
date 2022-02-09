package hr.markic.budgetmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.databinding.FragmentCharBinding
import hr.markic.budgetmanager.framework.CharUtil
import java.time.LocalDateTime
import java.time.YearMonth


class CharFragment : Fragment() {

    lateinit var binding: FragmentCharBinding
    lateinit var charUtil: CharUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCharBinding.inflate(inflater, container, false)

        charUtil = CharUtil(binding.bcCosts)


        val listOfGraphDisplay = resources.getStringArray(R.array.listOfGraphDisplay)

        val categories = resources.getStringArray(R.array.categories)


        val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, listOfGraphDisplay)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        binding.spinner.adapter = spinnerArrayAdapter

        binding.spinner.selectedItem

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    1 -> {
                        val hours = resources.getStringArray(R.array.listOfHours)
                        charUtil.fillGraphWithData(hours, categories, hours.size)
                    }
                    2 -> {
                        val days = resources.getStringArray(R.array.listOfDays)
                        charUtil.fillGraphWithData(days, categories, days.size)
                    }
                    3 -> {

                        val yearMonth = YearMonth.of(LocalDateTime.now().year, LocalDateTime.now().month)
                        val dayInMonth = yearMonth.lengthOfMonth()

                        val listDays = Array(dayInMonth){"${it + 1}"}

                        charUtil.fillGraphWithData(listDays, categories, dayInMonth)
                    }
                    4 -> {
                        val months = resources.getStringArray(R.array.listOfMonths)
                        charUtil.fillGraphWithData(months, categories, months.size)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        return binding.root
    }

}