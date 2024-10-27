package com.example.e_commerce.ui.login.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.BuildConfig
import com.example.e_commerce.R

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.ui.common.views.ProgressDialog
import com.example.e_commerce.ui.home.MainActivity
import com.example.e_commerce.ui.login.viewmodel.LoginViewModel
import com.example.e_commerce.ui.login.viewmodel.LoginViewModelFactory
import com.example.e_commerce.ui.showSnakeBarError
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.LoginException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException

import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }
    private val loginViewModel: LoginViewModel by viewModels {
            LoginViewModelFactory(contextValue = requireActivity())
    }
    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>
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



        loginLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
        }
    }


    private fun initListeners() {

        binding.signInBtn.setOnClickListener {
            loginViewModel.login()
        }
        binding.googleSigninBtn.setOnClickListener {
            loginWithGoogle()
        }


        binding.facebookSigninBtn.setOnClickListener {
            loginManager.logInWithReadPermissions(
                this,
                callbackManager,
                listOf("email", "public_profile")
            )

            loginWithFb(loginManager)
        }
    }

    private fun loginWithFb(loginManager: LoginManager) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                val accessToken = loginResult.accessToken
                loginViewModel.loginWithFacebook(accessToken.token)

            }

            override fun onCancel() {
                Log.d(TAG, "Facebook login canceled")
            }

            override fun onError(exception: FacebookException) {
                view?.showSnakeBarError(exception.message ?: getString(R.string.try_again_later))
                logAuthIssueToCrashlytics(
                    exception.message ?: getString(R.string.try_again_later),
                    "Facebook"
                )
            }
        })
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
                            goToHome()
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

    private fun goToHome() {
        requireActivity().startActivity(Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        requireActivity().finish()
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