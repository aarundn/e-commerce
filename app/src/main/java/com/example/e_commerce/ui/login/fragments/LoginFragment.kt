package com.example.e_commerce.ui.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce.R
import com.example.e_commerce.data.repository.user.UserRepositoryDataSourceImpl
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.login.viewmodel.LoginViewModel


class LoginFragment : Fragment() {

    val loginViewModel : LoginViewModel by lazy {
        LoginViewModel(userRepository = UserRepositoryDataSourceImpl(requireActivity()))
    }
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
      private const val TAG = "LoginFragment"
    }
}