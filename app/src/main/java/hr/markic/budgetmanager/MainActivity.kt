package hr.markic.budgetmanager

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase
import hr.markic.budgetmanager.R.array.*
import hr.markic.budgetmanager.databinding.ActivityMainBinding
import hr.markic.budgetmanager.fragments.CURRENT_USERNAME
import hr.markic.budgetmanager.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO staviti spinner na alert dialog
        //TODO staviti bar scanner na alert dialog

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.
        getInstance("https://budgetmanager-b7e7a-default-rtdb.europe-west1.firebasedatabase.app/");

        val usersDB = database.getReference("Users")

        val colorsNames = resources.getStringArray(colors_names)
        val colorsIds= resources.getIntArray(colors_ids)
        val categories = resources.getStringArray(categories)

        val spinnerArrayAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        val spinner = binding.categorySpinner
        spinner.adapter = spinnerArrayAdapter

        binding.btnGetColor.setOnClickListener {
            binding.tvUserName.text = colorsIds[categories.indexOf(spinner.selectedItem)].toString()
            binding.btnGetColor.setBackgroundColor(Color.parseColor(colorsNames[categories.indexOf(spinner.selectedItem)]))
        }


        usersDB.child(CURRENT_USERNAME).get().addOnSuccessListener {

            if (it.exists()){

                val email = it.child("email").value.toString()
                val username = it.child("username").value.toString()

                user = User(email, username)

                binding.tvUserName.text = user.username;
                binding.tvEmail.text = user.email;
            }
        }



    }
}