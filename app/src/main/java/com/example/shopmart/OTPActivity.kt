package com.example.shopmart

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopmart.databinding.FragmentOTPActivityBinding
import com.example.shopmart.models.Users
import com.example.shopmart.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.Util

class OTPActivity : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentOTPActivityBinding
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOTPActivityBinding.inflate(inflater, container, false)
        getUserNumber()
        customizingEnteringOtp()
        onBackButtonClick()
        sendOTP()
        onLoginButtonClicked()
        return binding.root
    }

    private fun onBackButtonClick() {
        binding.tbOtpFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPActivity_to_signInActivity)
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()

        binding.tvUserNumber.text = userNumber
    }

    private fun customizingEnteringOtp() {
        val editTexts = arrayOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    } else if (s?.length == 0) {
                        if (i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP....")
        viewModel.apply {
            sendOTP(userNumber, requireActivity())
            lifecycleScope.launch {
                otpSent.collect {
                    if (it) {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "OTP sent successfully")
                    }
                }
            }
        }
    }

    private fun onLoginButtonClicked(){
      binding.buttonLogin.setOnClickListener {
          Utils.showDialog(requireContext(), "Signing you....")
          val editTexts = arrayOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)
          val otp = editTexts.joinToString("") {
              it.text.toString()
          }

          if (otp.length != 6){
              Utils.showToast(requireContext(), "Enter valid OTP")
          }else{
              editTexts.forEach { it.text?.clear(); it.clearFocus()}
              verifyOTP(otp)
          }
      }
    }

    private fun verifyOTP(otp: String) {
        val users = Users(Utils.getUid(), userPhoneNumber = userNumber,null)
        viewModel.signInWithPhoneAuthCredential(otp, userNumber,users)
        lifecycleScope.launch {
            viewModel.isSignedIn.collect {
                if (it) {
                    Utils.hideDialog()
                    Utils.showToast(requireContext(),"OTP verified successfully")
                    startActivity(Intent(requireActivity(), UserMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

}
