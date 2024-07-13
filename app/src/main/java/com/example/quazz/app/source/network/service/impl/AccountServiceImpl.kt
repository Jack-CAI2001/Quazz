package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.AccountService
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import javax.inject.Inject


class AccountServiceImpl @Inject constructor() : AccountService {

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
//                    this.trySend(auth.currentUser?.let { User(it.uid) })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String): Result<Unit, DataError.Network> {
        return try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            when(e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        408 -> Result.Error(DataError.Network.TIMEOUT)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        404 -> Result.Error(DataError.Network.NOT_FOUND)
                        500 -> Result.Error(DataError.Network.SERVER_ERROR)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }
                is FirebaseNetworkException -> {
                    Result.Error(DataError.Network.NETWORK_ERROR)
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error(DataError.Network.INVALID_CREDENTIALS)
                }
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun signUp(email: String, password: String): Result<Unit, DataError.Network> {

        return try {
            Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            when(e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        408 -> Result.Error(DataError.Network.TIMEOUT)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        404 -> Result.Error(DataError.Network.NOT_FOUND)
                        500 -> Result.Error(DataError.Network.SERVER_ERROR)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }
                else -> Result.Error(DataError.Network.UNKNOWN)

            }
        }
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }
}
