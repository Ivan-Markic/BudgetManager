package hr.markic.budgetmanager.fragments

import android.content.ContentValues
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
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
import com.google.firebase.ktx.Firebase
import hr.markic.budgetmanager.MainActivity
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.databinding.FragmentLoginBinding

private const val EMAIL_PARAMETER = "hr.markic.fragments.email_parameter"
private const val PASSWORD_PARAMETER = "hr.markic.fragments.password_parameter"

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

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()){
            binding.etEmail.setText(email)
            binding.etPassword.setText(password)
        }

        tvRegister.setOnClickListener{

            activity?.supportFragmentManager?.beginTransaction()?.
            replace(R.id.frameLayout, RegisterFragment())?.commit()
        }

        btnLogin.setOnClickListener{

            loginUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        return binding.root
    }

    private fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    Toast.makeText(context, "Authentication successed.",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    //Pod upitnikom iz nekog razloga app ne radi

                        //Do ovog dijela
                        startActivity(Intent(context, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)})
                    }
                 else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, task.exception?.message,
                        Toast.LENGTH_SHORT).show()
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