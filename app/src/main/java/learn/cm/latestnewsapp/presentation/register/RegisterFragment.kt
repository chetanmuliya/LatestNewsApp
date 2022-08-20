package learn.cm.latestnewsapp.presentation.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import learn.cm.latestnewsapp.R
import learn.cm.latestnewsapp.database.local.UserRegisterEntity
import learn.cm.latestnewsapp.databinding.FragmentRegisterBinding
import learn.cm.latestnewsapp.presentation.MainActivity
import learn.cm.latestnewsapp.presentation.login.LoginFragment
import learn.cm.latestnewsapp.presentation.login.LoginViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_register, container, false)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register, container, false
        )


        setUpViewModel()
        setUpUiEvents()

        return binding.root
    }

    private fun setUpViewModel() {
        loginViewModel = (activity as MainActivity).loginViewModel
        loginViewModel.users.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                if (it.first().firstName == binding.etFirstName.text.toString()) {
                    //entered new data
                    Toast.makeText(requireContext(), "Account Successfully Created", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun setUpUiEvents() {
        binding.btSubmit.setOnClickListener {
            registerUser(binding.etFirstName.text.toString(),binding.etLastName.text.toString(),
                binding.etUsername.text.toString(),binding.etPassword.text.toString())
        }
    }

    private fun registerUser(firstName: String, lastName: String, userName: String, password: String) {
        if (firstName.isNotBlank() && firstName.isNotBlank() && firstName.isNotBlank() && firstName.isNotBlank()){
            lifecycleScope.launch {
                loginViewModel.registerUser(
                    UserRegisterEntity(
                        firstName = firstName,
                        lastName = lastName,
                        userName = userName,
                        password = password
                    )
                )
            }
        }else{
            Toast.makeText(requireContext(),"Please fill all fields",Toast.LENGTH_SHORT).show()
        }

    }

}