package com.example.shopmart

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.shopmart.databinding.FragmentSignInActivityBinding

class SignInActivity : Fragment() {
    private lateinit var binding: FragmentSignInActivityBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInActivityBinding.inflate(inflater,container,false)
        setStatusBarColor()
        getUserNumber()
        onContinueClick()
        return binding.root
    }

    private fun onContinueClick(){
        binding.buttonContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString().trim()
            if (number.isEmpty() || number.length != 10){
                Utils.showToast(requireContext(),"Enter valid number")
            }else{
               val bundle = Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_signInActivity_to_OTPActivity,bundle)

            }
        }
    }

    private fun setStatusBarColor(){
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(),R.color.yellow)
            statusBarColor=statusBarColors
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun getUserNumber(){
        binding.etUserNumber.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(number: CharSequence?, start: Int, before: Int, count: Int) {
                val len = number?.length
                if (len == 10){
                binding.buttonContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green))
            }else{
                binding.buttonContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grayish_blue))
            }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

}