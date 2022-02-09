package hr.markic.budgetmanager

import android.os.Bundle
import android.util.Log
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import hr.markic.budgetmanager.databinding.ActivitySplashScreenBinding
import hr.markic.budgetmanager.framework.*
import hr.markic.budgetmanager.repository.RepositoryFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivSplash.startAnimation(R.anim.rotate)
        binding.tvSplash.startAnimation(R.anim.blink)
    }

    private fun redirect() {
        if (!getBooleanPreference("message")) {

            GlobalScope.launch {

                RepositoryFactory.createRepository().getBills()

                withContext(Dispatchers.Main){
                    callDelayed(3000) {startActivity<MainActivity>()}
                }
            }

        } else {
            if (isOnline()) {

            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(3000) {finish()}
            }
        }
    }
}