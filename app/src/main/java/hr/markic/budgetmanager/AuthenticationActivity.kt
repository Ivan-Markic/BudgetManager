package hr.markic.budgetmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import hr.markic.budgetmanager.databinding.ActivityAuthenticationBinding
import hr.markic.budgetmanager.fragments.LoginFragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import hr.markic.budgetmanager.fragments.RegisterFragment
import hr.markic.budgetmanager.repository.auth
import java.util.ArrayList


class AuthenticationActivity : AppCompatActivity() {

    
    private lateinit var binding : ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth.signOut()

        val user = Firebase.auth.currentUser

        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)})
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, LoginFragment()).commit()
        }
    }
}