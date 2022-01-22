package hr.markic.budgetmanager.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.databinding.FragmentLoginBinding
import hr.markic.budgetmanager.databinding.FragmentRegisterBinding



class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener{

            if(dataIsValid()){

                
                var email = binding.etEmail.text.toString()
                var password = binding.etPassword.text.toString()

                registerUser(email, password)

            }
            else{
                binding.etEmail.error = "Email is mandatory"
                binding.etPassword.error = "Password is mandatory"
            }
        }

        return binding.root
    }

    private fun registerUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(context, "Authentication successed.",
                        Toast.LENGTH_SHORT).show()

                    val userToken = auth.currentUser!!.getIdToken(true);
                    val firstName = binding.etFirstname.text.toString()
                    val lastName = binding.etLastname.text.toString()
                    val database = FirebaseDatabase.
                    getInstance("https://budgetmanager-b7e7a-default-rtdb.europe-west1.firebasedatabase.app/").reference;

                    val userCollection = FirebaseDatabase.getInstance().getReference("Users")
                    userCollection.child(userToken.toString()).setValue(user);

                    database.child(userName).setValue(User).addOnSuccessListener {

                        binding.firstName.text.clear()
                        binding.lastName.text.clear()
                        binding.age.text.clear()
                        binding.userName.text.clear()

                        Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener{

                        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


                    }

                    activity?.supportFragmentManager?.beginTransaction()?.
                    replace(R.id.frameLayout, LoginFragment.newInstance(email, password))?.commit()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun dataIsValid() = binding.etEmail.text.isNotBlank() && binding.etPassword.text.isNotBlank()
}