package learn.cm.latestnewsapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import learn.cm.latestnewsapp.R
import learn.cm.latestnewsapp.database.data.datastore.PreferenceUtils
import learn.cm.latestnewsapp.databinding.FragmentLoginBinding
import learn.cm.latestnewsapp.databinding.FragmentRegisterBinding
import learn.cm.latestnewsapp.presentation.MainActivity
import learn.cm.latestnewsapp.presentation.home.HomeActivity
import learn.cm.latestnewsapp.util.Constants
import java.util.concurrent.Executor


class LoginFragment : Fragment() {

    private var isMpinCreated: Boolean = false
    private var isBioMetricPrompted: Boolean = false
    private var isBioMetricRegister: Boolean = false
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    //biometric
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )
        setUpBioMetric()
        setUpViewModel()
        setUpUiEvents()

        return binding.root
    }

    private fun setUpViewModel() {
        loginViewModel = (activity as MainActivity).loginViewModel

        isMpinCreated = PreferenceUtils.getBoolean(Constants.IS_MPIN_CREATED,false,requireContext())
        isBioMetricPrompted = PreferenceUtils.getBoolean(Constants.IS_BIOMETRIC_PROMPTED,false,requireContext())
        isBioMetricRegister = PreferenceUtils.getBoolean(Constants.IS_BIOMETRIC_REGISTERED,false,requireContext())

        if(isMpinCreated && isBioMetricPrompted){
            if(!isBioMetricRegister) {
                biometricPrompt.authenticate(promptInfo)
                PreferenceUtils.putBoolean(Constants.IS_BIOMETRIC_REGISTERED,true,requireContext())
            }
        }

    }

    private fun setUpUiEvents() {
        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment2())
        }
        binding.btLogin.setOnClickListener {
            checkLogin()
        }
    }

    private fun checkLogin() {
        val username = binding.etLoginUsername.text.toString()
        val password = binding.etLoginPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()){
            uiScope.launch {
                val isUserExist = loginViewModel.checkLogin(username,password)
                if (isUserExist){
                    Toast.makeText(requireContext(),"Login Successful", Toast.LENGTH_SHORT).show()
                    setLoggedIn()
                    //go to create register
                    //findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPinFragment())
                }else{
                    Toast.makeText(requireContext(),"Incorrect username and password", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(requireContext(),"Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLoggedIn() {
        val logged = PreferenceUtils.getBoolean(Constants.SET_LOGGED_IN,false,requireContext())
        if (!logged) {
            PreferenceUtils.putBoolean(Constants.SET_LOGGED_IN, true, requireContext())
            Log.d("*************", "setLoggedIn: first time $logged")
        }else if(logged){
            startActivity(Intent(requireContext(),HomeActivity::class.java))
        }
        if(!isBioMetricPrompted) biometricPrompt.authenticate(promptInfo)
    }

    //biometric code below

    private fun setUpBioMetric() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                    //PreferenceUtils.putBoolean(Constants.IS_BIOMETRIC_PROMPTED,true,requireContext())
                  //  findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPinFragment())
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    PreferenceUtils.putBoolean(Constants.IS_BIOMETRIC_PROMPTED,true,requireContext())
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPinFragment())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for LatestNewsApp")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }
}