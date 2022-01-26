package hr.markic.budgetmanager.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.databinding.FragmentRegisterBinding
import hr.markic.budgetmanager.model.User


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {

            if (dataIsValid()) {

                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                registerUser(email, password)

            } else {
                binding.etUsername.error = "Username is mandatory"
                binding.etEmail.error = "Email is mandatory"
                binding.etPassword.error = "Password is mandatory"
            }
        }
        return binding.root
    }

    private fun registerUser(email: String, password: String) {

        val username = binding.etUsername.text.toString()
        val database = FirebaseDatabase.getInstance("https://budgetmanager-b7e7a-default-rtdb.europe-west1.firebasedatabase.app/");
        val usersDB = database.getReference("Users")

        usersDB.child(username).get().addOnSuccessListener {

            if (!it.exists()){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(
                                context, "Authentication successed.",
                                Toast.LENGTH_SHORT
                            ).show()

                            val user = User(username, email);

                            usersDB.child(user.username).setValue(user);

                            val userProfileChangeRequest = userProfileChangeRequest {
                                this.displayName = username
                            }

                            auth.currentUser!!.updateProfile(userProfileChangeRequest)

                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.frameLayout, LoginFragment.newInstance(email, password))
                                ?.commit()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                context, task.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {
                Toast.makeText(context, "User with this username exist, try different", Toast.LENGTH_LONG).show()
            }
        }



    }

    private fun dataIsValid() =
        binding.etEmail.text.isNotBlank() && binding.etPassword.text.isNotBlank()
                && binding.etUsername.text.isNotBlank()
}