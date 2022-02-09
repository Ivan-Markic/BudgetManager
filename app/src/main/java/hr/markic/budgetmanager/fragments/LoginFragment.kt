package hr.markic.budgetmanager.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.markic.budgetmanager.MainActivity
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.SplashScreenActivity
import hr.markic.budgetmanager.databinding.FragmentLoginBinding
import hr.markic.budgetmanager.framework.startActivity
import hr.markic.budgetmanager.repository.RepositoryFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val EMAIL_PARAMETER = "hr.markic.fragments.email_parameter"
private const val PASSWORD_PARAMETER = "hr.markic.fragments.password_parameter"

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(EMAIL_PARAMETER)
            password = it.getString(PASSWORD_PARAMETER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.etEmail.setText("ivanmarka555@gmail.com");
        binding.etPassword.setText("peropero");

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            binding.etEmail.setText(email)
            binding.etPassword.setText(password)
        }

        binding.tvRegister.setOnClickListener {

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frameLayout, RegisterFragment())?.commit()
        }

        binding.btnLogin.setOnClickListener {

            if (binding.etEmail.text.toString().isNotBlank()
                && binding.etPassword.text.isNotBlank()
            )
                GlobalScope.launch {
                    RepositoryFactory.createRepository()
                        .loginUser(binding.etEmail.text.toString(), binding.etPassword.text.toString(), requireContext())
                } else {
                binding.etEmail.error = "Email is mandatory";
                binding.etEmail.error = "Password is mandatory";
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String, password: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(EMAIL_PARAMETER, email)
                    putString(PASSWORD_PARAMETER, password)
                }
            }
    }
}