package com.example.e_commerce.ui.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.e_commerce.R
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.databinding.FragmentFogotePasswordBinding
import com.example.e_commerce.ui.login.viewmodel.ForgotPasswordViewModel
import com.example.e_commerce.ui.login.viewmodel.ForgotPasswordViewModelFactory
import com.example.e_commerce.ui.login.viewmodel.LoginViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FogotePasswordFragment : BottomSheetDialogFragment(){

    private var _binding: FragmentFogotePasswordBinding? = null
    private val binding get() = _binding!!
    private val forgetPasswordViewModel : ForgotPasswordViewModel by viewModels {
        ForgotPasswordViewModelFactory(
            contextValue = requireActivity()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFogotePasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = forgetPasswordViewModel
        return binding.root
    }

    companion object {
        const val TAG = "FogotePasswordFragment"
    }
}