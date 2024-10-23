package com.example.e_commerce.data.repository.auth;

import android.util.Log
import com.example.e_commerce.data.models.Resource
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.core.FieldFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

public class FireBaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : FireBaseAuthRepository {
    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                emit(Resource.Success(it.uid))
            } ?: run {
                emit(Resource.Error(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }

    }

    override suspend fun loginWithGoogle(idToken: String): Flow<Resource<String>> = flow{
        try {
            emit(Resource.Loading())
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.user?.let {
                emit(Resource.Success(it.uid))
            } ?: run {
                emit(Resource.Error(Exception("User not found")))
            }

        }  catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    override suspend fun loginWithFacebook(idToken: String): Flow<Resource<String>> = flow{
        try {
            emit(Resource.Loading())
            val credential = FacebookAuthProvider.getCredential(idToken)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.user?.let {
                emit(Resource.Success(it.uid))
            } ?: run {
                emit(Resource.Error(Exception("User not found")))
            }

            // Sign in success, update UI with the signed-in user's information

        }  catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

}
