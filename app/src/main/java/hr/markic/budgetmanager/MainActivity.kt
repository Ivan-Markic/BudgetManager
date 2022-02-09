package hr.markic.budgetmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.markic.budgetmanager.databinding.ActivityMainBinding
import hr.markic.budgetmanager.fragments.MapsFragment
import hr.markic.budgetmanager.model.User
import hr.markic.budgetmanager.repository.RepositoryFactory
import hr.markic.budgetmanager.repository.usersDB

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RepositoryFactory.createRepository().getLocationsForBills()

        usersDB.child(Firebase.auth.currentUser!!.displayName.toString()).get().addOnSuccessListener {

            if (it.exists()){

                val email = it.child("email").value.toString()
                val username = it.child("username").value.toString()

                user = User(email, username)

                supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, MapsFragment()).commit()

            }
        }



    }
}