package com.example.e_commerce.ui.login.fragments

import android.os.Bundle
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
import com.example.e_commerce.ui.common.views.ProgressDialog
import com.example.e_commerce.ui.login.viewmodel.RegisterViewModel
import com.example.e_commerce.ui.login.viewmodel.RegisterViewModelFactory
import com.example.e_commerce.ui.showSnakeBarError
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {


    private val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }

    private  var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel : RegisterViewModel by viewModels {
        RegisterViewModelFactory(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = registerViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { resource ->
                when(resource) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        showLoginSuccess()
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
                    }
                    is Resource.Loading -> {
                       progressDialog.show()
                    }
                }
            }
        }
    }

    private fun showLoginSuccess() {
        AlertDialog.Builder(requireContext())
            .setTitle("Login success")
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
            registerViewModel.registerWithEmailAndPassWord()
        }
        binding.loginTv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val TAG = "RegisterFragment"
    }
}
