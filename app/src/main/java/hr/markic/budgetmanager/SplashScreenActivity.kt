package hr.markic.budgetmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.markic.budgetmanager.databinding.ActivitySplashScreenBinding
import hr.markic.budgetmanager.framework.*
import hr.markic.budgetmanager.repository.RepositoryFactory

private const val DELAY = 2000L
const val DATA_IMPORTED = "hr.markic.product.data_imported"

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

        if (getBooleanPreference(DATA_IMPORTED)) {

            RepositoryFactory.createRepository().getBills()
            callDelayed(DELAY) {startActivity<MainActivity>()}
        } else {
            if (isOnline()) {
                RepositoryFactory.createRepository().getBills()
                Intent(this, ProductService::class.java).apply {
                    ProductService.enqueue(
                        this@SplashScreenActivity,
                        this
                    )
                }

            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) {finish()}
            }
        }
    }
}