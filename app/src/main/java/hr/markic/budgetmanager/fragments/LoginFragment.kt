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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val EMAIL_PARAMETER = "hr.markic.fragments.email_parameter"
private const val PASSWORD_PARAMETER = "hr.markic.fragments.password_parameter"
lateinit var CURRENT_USERNAME: String

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
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
        auth = Firebase.auth

        var tvRegister = binding.tvRegister;
        var btnLogin = binding.btnLogin;

        binding.etEmail.setText("ivanmarka555@gmail.com");
        binding.etPassword.setText("peropero");

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            binding.etEmail.setText(email)
            binding.etPassword.setText(password)
        }

        tvRegister.setOnClickListener {

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frameLayout, RegisterFragment())?.commit()
        }

        btnLogin.setOnClickListener {

            if (binding.etEmail.text.toString()
                    .isNotBlank() && binding.etPassword.text.isNotBlank()
            )
                GlobalScope.launch {
                    loginUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                } else {
                binding.etEmail.error = "Email is mandatory";
                binding.etEmail.error = "Password is mandatory";
            }
        }

        return binding.root
    }

    private fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {

                    val username = auth.currentUser!!.displayName!!;

                    CURRENT_USERNAME = username

                    requireContext().startActivity<SplashScreenActivity>()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

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