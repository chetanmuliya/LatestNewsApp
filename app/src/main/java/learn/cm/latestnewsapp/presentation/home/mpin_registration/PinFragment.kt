package learn.cm.latestnewsapp.presentation.home.mpin_registration

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import learn.cm.latestnewsapp.R
import learn.cm.latestnewsapp.database.data.datastore.PreferenceUtils
import learn.cm.latestnewsapp.databinding.FragmentLoginBinding
import learn.cm.latestnewsapp.databinding.FragmentPinBinding
import learn.cm.latestnewsapp.presentation.MainActivity
import learn.cm.latestnewsapp.presentation.home.HomeActivity
import learn.cm.latestnewsapp.presentation.login.LoginFragmentDirections
import learn.cm.latestnewsapp.util.Constants
import java.util.concurrent.Executor
import kotlin.math.log

class PinFragment : Fragment() {

    private var mPin: String? = null
    private var isMpinCreated: Boolean = false
    private lateinit var binding: FragmentPinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pin, container, false
        )

        setUpUI()

        return binding.root
    }

    private fun setUpUI() {

        isMpinCreated = PreferenceUtils.getBoolean(Constants.IS_MPIN_CREATED,false,requireContext())
        mPin = PreferenceUtils.getString(Constants.SET_MPIN,"",requireContext())
        setUI()
        binding.save.setOnClickListener {
            if (isMpinCreated){
                checkMpinIsCorrect()
            }else{
                saveMpin()
            }

        }
    }

    private fun checkMpinIsCorrect() {
        val pin = binding.password.text.toString()
        if(pin.isNotEmpty()){
            if (mPin == pin){
                findNavController().navigate(PinFragmentDirections.actionPinFragmentToLoginFragment())
            }else{
                binding.password.text.clear()
                Toast.makeText(requireContext(),"mpin is Incorrect",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUI() {
        binding.password.text.clear()
        if (!isMpinCreated){
            binding.tvHead.text = "Create MPin Password"
            binding.save.text = "Create"
        }else{
            binding.tvHead.text = "Enter MPin Password"
            binding.save.text = "Login"
        }
    }

    private fun saveMpin() {
        val pin = binding.password.text.toString()
        if(pin.isNotEmpty()){
            PreferenceUtils.putString(Constants.SET_MPIN,pin,requireContext())
            PreferenceUtils.putBoolean(Constants.IS_MPIN_CREATED,true,requireContext())
            Toast.makeText(requireContext(),"mpin created",Toast.LENGTH_SHORT).show()
            findNavController().navigate(PinFragmentDirections.actionPinFragmentToLoginFragment())
        }
    }

}