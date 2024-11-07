package com.example.e_commerce.ui.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.databinding.FragmentFogotePasswordBinding
import com.example.e_commerce.ui.common.views.ProgressDialog
import com.example.e_commerce.ui.login.viewmodel.ForgotPasswordViewModel
import com.example.e_commerce.ui.showSnakeBarError
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FogotePasswordFragment : BottomSheetDialogFragment() {
    private val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }
    private var _binding: FragmentFogotePasswordBinding? = null
    private val binding get() = _binding!!
    private val forgetPasswordViewModel: ForgotPasswordViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFogotePasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = forgetPasswordViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            forgetPasswordViewModel.forgetPasswordState.collect {
                when (it) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        dismiss()
                        showSetEmailSuccess()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        binding.forgotPassEmailLayout.error =
                            it.exception?.message ?: getString(R.string.try_again_later)
                        view?.showSnakeBarError(it.exception?.message ?:
                        getString(R.string.try_again_later))

                    }

                    is Resource.Loading -> {
                        progressDialog.show()
                    }
                }
            }

        }

    }

    private fun initListeners() {
        binding.btnSendEmail.setOnClickListener {
            forgetPasswordViewModel.sendPasswordResetEmail()
        }
    }
    private fun showSetEmailSuccess() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Reset Password")
            .setMessage("we have sent you an email to reset your password")
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                dismiss()
            }
            .show()
    }
    companion object {
        const val TAG = "FogotePasswordFragment"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}