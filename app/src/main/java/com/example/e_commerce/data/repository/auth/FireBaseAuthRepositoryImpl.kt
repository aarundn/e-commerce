package com.example.e_commerce.data.repository.auth;

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.user.AuthProvider
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.LoginException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FireBaseAuthRepository {

    // Example usage for email and password login
    override suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ) = login(AuthProvider.EMAIL) { auth.signInWithEmailAndPassword(email, password).await() }

    override suspend fun loginWithGoogle(idToken: String): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                // perform firebase auth sign in request
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val userId = auth.currentUser?.uid

                if (userId == null) {
                    val msg = "Sign in UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.GOOGLE.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }


                // get user details from firestore
                val userDoc = firestore.collection("users").document(userId).get().await()
                if (!userDoc.exists()) {
                    // New user - Add user details to Firestore
                    val user = result.user
                    val userDetails = UserDetailsModel(
                        id = userId,
                        name = user?.displayName ?: "Unknown Name",
                        email = user?.email ?: "Unknown Email",
                        createdAt = System.currentTimeMillis()
                    )
                    firestore.collection("users").document(userId).set(userDetails).await()
                }

                // map user details to UserDetailsModel
                val savedUserDoc = firestore.collection("users").document(userId).get().await()
                val userDetails = savedUserDoc.toObject(UserDetailsModel::class.java)
                userDetails?.let {
                    emit(Resource.Success(userDetails))
                } ?: run {
                    val msg = "Error mapping user details to UserDetailsModel, user id = $userId"
                    logAuthIssueToCrashlytics(msg, AuthProvider.GOOGLE.name)
                    emit(Resource.Error(Exception(msg)))
                }
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}", AuthProvider.GOOGLE.name
                )
                emit(Resource.Error(e)) // Emit error
            }
        }
    }

    // Example usage for Facebook login
    override suspend fun loginWithFacebook(token: String) = login(AuthProvider.FACEBOOK) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).await()
    }

    override suspend fun registerWithEmailAndPassWord(
        fullName: String,
        email: String,
        password: String
    ): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                // perform firebase auth sign up request
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.EMAIL.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                // create user details in firestore
                val userDetails = UserDetailsModel(
                    id = userId,
                    name = fullName,
                    email = email,
                    createdAt = System.currentTimeMillis()
                )
                firestore.collection("users").document(userId).set(userDetails).await()
                authResult.user?.sendEmailVerification()?.await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.EMAIL.name
                )
                emit(Resource.Error(e)) // Emit error
            }
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Flow<Resource<String>> {
        return flow {
            try {
                emit(Resource.Loading())
                auth.sendPasswordResetEmail(email).await()
                emit(Resource.Success("Password reset email sent"))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.EMAIL.name
                )
                emit(Resource.Error(e)) // Emit error
            }
        }
    }

    private suspend fun login(
        provider: AuthProvider,
        signInRequest: suspend () -> AuthResult,
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
            emit(Resource.Loading())
            // perform firebase auth sign in request
            val authResult = signInRequest()
            val userId = authResult.user?.uid

            if (userId == null) {
                val msg = "Sign in UserID not found"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }
            if (authResult.user?.isEmailVerified == false) {
                val msg = "Email not verified"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }
            // get user details from firestore
            val userDoc = firestore.collection("users").document(userId).get().await()
            if (!userDoc.exists()) {
                val msg = "Logged in user not found in firestore"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }

            // map user details to UserDetailsModel
            val userDetails = userDoc.toObject(UserDetailsModel::class.java)
            userDetails?.let {
                emit(Resource.Success(userDetails))
            } ?: run {
                val msg = "Error mapping user details to UserDetailsModel, user id = $userId"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
            }
        } catch (e: Exception) {
            logAuthIssueToCrashlytics(
                e.message ?: "Unknown error from exception = ${e::class.java}", provider.name
            )
            emit(Resource.Error(e)) // Emit error
        }
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }

    override fun logout() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "FirebaseAuthRepositoryI"
    }

}
