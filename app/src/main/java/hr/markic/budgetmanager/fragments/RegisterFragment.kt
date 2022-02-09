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
import hr.markic.budgetmanager.model.Bill
import hr.markic.budgetmanager.model.User
import hr.markic.budgetmanager.repository.AppRepository
import hr.markic.budgetmanager.repository.RepositoryFactory
import kotlinx.coroutines.GlobalScope
import java.time.LocalDateTime
import java.time.ZoneOffset


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegister.setOnClickListener {

            if (dataIsValid()) {

                val username = binding.etUsername.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                RepositoryFactory.createRepository().registerUser(username, email, password, requireContext(), requireActivity())

            } else {
                binding.etUsername.error = "Username is mandatory"
                binding.etEmail.error = "Email is mandatory"
                binding.etPassword.error = "Password is mandatory"
            }
        }
        return binding.root
    }

    private fun dataIsValid() =
        binding.etEmail.text.isNotBlank() && binding.etPassword.text.isNotBlank()
                && binding.etUsername.text.isNotBlank()
}