package com.example.e_commerce.ui.login.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.BuildConfig
import com.example.e_commerce.R

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.user.UserRepositoryDataSourceImpl
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.common.views.ProgressDialog
import com.example.e_commerce.ui.login.viewmodel.LoginViewModel
import com.example.e_commerce.ui.showSnakeBarError
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.LoginException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }
    private val loginViewModel: LoginViewModel by lazy {
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
        initListeners()
        initViewModel()
    }


    private fun initListeners() {
        binding.signInBtn.setOnClickListener {
            loginViewModel.login()
        }
        binding.googleSigninBtn.setOnClickListener {
            loginWithGoogle()
        }
    }


    private fun initViewModel() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect { state ->

                state.let {
                    when (it) {
                        is Resource.Loading -> {
                            progressDialog.show()
                        }

                        is Resource.Success -> {
                            progressDialog.dismiss()
                            Toast.makeText(
                                requireActivity(),
                                it.data, Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resource.Error -> {
                            val msg = it.exception?.message ?: getString(R.string.try_again_later)
                            progressDialog.dismiss()
                            Toast.makeText(
                                requireActivity(),
                                it.exception?.message, Toast.LENGTH_SHORT
                            ).show()
                            view?.showSnakeBarError(
                                it.exception?.message ?: getString(R.string.try_again_later)
                            )
                            logAuthIssueToCrashlytics(msg, "Error")
                        }
                    }
                }

            }
        }
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.client_id)
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInClient.signOut()
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handelTask(task)
        } else {
            view?.showSnakeBarError(getString(R.string.google_sign_in_was_canceled_or_failed))
        }
    }

    private fun handelTask(task: Task<GoogleSignInAccount>) {
        try {

            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)

        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.try_again_later))
            val msg = e.message ?: getString(R.string.try_again_later)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        loginViewModel.loginWithGoogle(idToken)
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}