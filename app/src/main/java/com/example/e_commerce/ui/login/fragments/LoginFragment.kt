package com.example.e_commerce.ui.login.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.user.UserRepositoryDataSourceImpl
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.login.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private val loginViewModel : LoginViewModel by lazy {
        LoginViewModel(
            userRepository = UserRepositoryDataSourceImpl(requireActivity()),
            firebaseRepo = FireBaseAuthRepositoryImpl()
        )
    }
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = loginViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            loginViewModel.email.collect{
                Log.d("EMAIL", it)
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
      private const val TAG = "LoginFragment"
    }
}