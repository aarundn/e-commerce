package com.example.e_commerce.ui.login.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentRegisterBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.login.viewmodel.RegisterViewModel
import com.example.e_commerce.ui.login.viewmodel.RegisterViewModelFactory
import com.example.e_commerce.ui.showSnakeBarError
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.RegisterException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {


    override val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(requireActivity())
    }
    override fun getLayoutRes(): Int = R.layout.fragment_register

    override fun init() {
        initListeners()
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.registerState.collect { resource ->
                when(resource) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        showRegisterSuccess()
                    }
                    is Resource.Error -> {
                        val msg = resource.exception?.message ?: getString(R.string.try_again_later)
                        progressDialog.dismiss()
                        Toast.makeText(
                            requireActivity(),
                            resource.exception?.message, Toast.LENGTH_SHORT
                        ).show()
                        view?.showSnakeBarError(
                            resource.exception?.message ?: getString(R.string.try_again_later)
                        )
                        logAuthIssueToCrashlytics(msg, "Register with email and password")
                        Log.d(TAG, "initViewModel: ${resource.exception?.message}")
                    }
                    is Resource.Loading -> {
                       progressDialog.show()
                    }
                }
            }
        }
    }

    private fun showRegisterSuccess() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Register success")
            .setMessage("You have successfully registered we have " +
                    "sent you a verification email please verify your email to login")
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                findNavController().popBackStack()
            }
            .show()
    }

    private fun initListeners() {
        binding.signUpBtn.setOnClickListener {
            viewModel.registerWithEmailAndPassWord()
        }
        binding.loginTv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<RegisterException>(
            msg,
            CrashlyticsUtils.REGISTER_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }


    companion object {
        const val TAG = "RegisterFragment"
    }
}
