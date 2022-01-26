package hr.markic.budgetmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import hr.markic.budgetmanager.databinding.ActivityMainBinding
import hr.markic.budgetmanager.fragments.CURRENT_USERNAME
import hr.markic.budgetmanager.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.
        getInstance("https://budgetmanager-b7e7a-default-rtdb.europe-west1.firebasedatabase.app/");

        val usersDB = database.getReference("Users")



        usersDB.child(CURRENT_USERNAME).get().addOnSuccessListener {

            if (it.exists()){

                val username = it.child("username").value.toString()
                val email = it.child("email").value.toString()

                user = User(username, email)

                binding.tvUserName.text = user.username;
                binding.tvEmail.text = user.email;
            }
        }



    }
}