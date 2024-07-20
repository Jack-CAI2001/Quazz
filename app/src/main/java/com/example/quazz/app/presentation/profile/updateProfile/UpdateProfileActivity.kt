package com.example.quazz.app.presentation.profile.updateProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.quazz.R
import com.example.quazz.app.presentation.profile.updateProfile.UpdateProfile.UpdateEmail
import com.example.quazz.app.presentation.profile.updateProfile.UpdateProfile.UpdatePassword
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class UpdateProfileActivity: ComponentActivity() {

    companion object {
        const val TYPE = "UpdateProfileActivity"
        fun newIntent(context: Context, data: UpdateProfile): Intent =
            Intent(context, UpdateProfileActivity::class.java).apply {
                putExtra(TYPE, data)
            }
    }

    private val viewModel: UpdateProfileViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // if not working correctly need restart device
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left)
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right)
        }
        setContent {
            val model = intent.getParcelableExtra(TYPE, UpdateProfile::class.java)
            model?.let {
                UpdateProfileScreen(
                    onBackClick = {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    },
                    onSaveClick = {
                        setResult(Activity.RESULT_OK, Intent().apply {
                            when(it) {
                                is UpdateEmail -> putExtra(TYPE, getString(R.string.update_email))
                                is UpdatePassword -> putExtra(TYPE, getString(R.string.update_password))
                            }
                        })
                        finish()
                    },
                    viewModel = viewModel,
                    model = it)
            } ?: run {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }
}
sealed class UpdateProfile: Parcelable {
    @Parcelize
    data object UpdateEmail: UpdateProfile()
    @Parcelize
    data object UpdatePassword: UpdateProfile()
}