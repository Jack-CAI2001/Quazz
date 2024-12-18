package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.handleError
import com.example.quazz.app.source.network.service.AccountService
import com.example.quazz.app.source.network.service.DatabaseConstants.User.EMAIL
import com.example.quazz.app.source.network.service.DatabaseConstants.User.PSEUDO
import com.example.quazz.app.source.network.service.DatabaseConstants.User.UID
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AccountServiceImpl @Inject constructor() : AccountService {

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

    override suspend fun signIn(email: String, password: String): Result<Unit, Error> {
        return try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun signUp(pseudo: String, email: String, password: String): Result<Unit, Error> {

        return try {
            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user!!.uid
            val user = hashMapOf(
                UID to uid,
                EMAIL to email,
                PSEUDO to pseudo,
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
            handleError(e)
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

    override suspend fun updateEmail(newEmail: String, password: String): Result<Unit, Error> {
        return try {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            reAuthenticate(user, email, password)
            user.verifyBeforeUpdateEmail(newEmail).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun updatePassword(newPassword: String, oldPassword: String): Result<Unit, Error> {
        return try {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            reAuthenticate(user, email, oldPassword)
            user.updatePassword(newPassword).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }
}
