package hr.markic.budgetmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.markic.budgetmanager.databinding.FragmentRegisterBinding
import hr.markic.budgetmanager.repository.RepositoryFactory


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