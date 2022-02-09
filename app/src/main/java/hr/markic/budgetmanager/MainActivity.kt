package hr.markic.budgetmanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.markic.budgetmanager.databinding.ActivityMainBinding
import hr.markic.budgetmanager.fragments.CharFragment
import hr.markic.budgetmanager.fragments.FactFragment
import hr.markic.budgetmanager.fragments.MapsFragment
import hr.markic.budgetmanager.framework.startActivity
import hr.markic.budgetmanager.repository.RepositoryFactory
import hr.markic.budgetmanager.repository.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RepositoryFactory.createRepository().getLocationsForBills()

        val charFragment = CharFragment()
        val mapFragment = MapsFragment()
        val factFragment = FactFragment()


        setCurrentFragment(charFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.graph->setCurrentFragment(charFragment)
                R.id.map->setCurrentFragment(mapFragment)
                R.id.fact->setCurrentFragment(factFragment)

            }
            true
        }
    }


    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayout,fragment)
            commit()
        }
    }
}