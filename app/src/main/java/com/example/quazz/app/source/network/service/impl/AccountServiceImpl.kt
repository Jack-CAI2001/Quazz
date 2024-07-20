package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.AccountService
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
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

    override suspend fun getUser(): Result<FirebaseUser, DataError.Network> {
        if (hasUser()) {
            return Result.Success(Firebase.auth.currentUser!!)
        }
        return Result.Error(DataError.Network.UNAUTHORIZED)
    }

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

    override suspend fun signUp(pseudo: String, email: String, password: String): Result<Unit, DataError.Network> {

        return try {
            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user!!.uid
            val user = hashMapOf(
                "uid" to uid,
                "email" to email,
                "pseudo" to pseudo,
            )
            Firebase.firestore
                .collection("user")
                .document(uid)
                .set(user)
                .addOnSuccessListener {
                    Firebase.auth.signOut()
                }
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

    override fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }

    private suspend fun reAuthenticate(user: FirebaseUser, email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).await()
    }

    override suspend fun updateEmail(newEmail: String, password: String): Result<Unit, DataError.Network> {
        return try {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            reAuthenticate(user, email, password)
            user.verifyBeforeUpdateEmail(newEmail).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun updatePassword(newPassword: String, oldPassword: String): Result<Unit, DataError.Network> {
        return try {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            reAuthenticate(user, email, oldPassword)
            user.updatePassword(newPassword).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
