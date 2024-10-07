package com.jetbrains.notes.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetbrains.notes.data.core.BaseViewState
import com.jetbrains.notes.ui.components.EmptyView
import com.jetbrains.notes.ui.components.LoadingView
import com.jetbrains.notes.ui.home.BottomNavItem
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SignUp(
    onLoginSuccess: () -> Unit,
//    viewModel: SignUpViewModel,
    navController: NavController,
) {
    val viewModel = koinViewModel<SignUpViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    SignUpScreenBody(
        uiState = uiState, onEvent = { viewModel.onTriggerEvent(it) }, navController = navController
    )

}

@Composable
fun SignUpScreenBody(
    modifier: Modifier = Modifier,
    uiState: BaseViewState<*>,
    onEvent: (SignUpEvent) -> Unit,
    navController: NavController
) {
    when (uiState) {
        is BaseViewState.Data -> {
            val state = uiState.value as? SignUpUIState

            if (state != null) {
                SignUpScreenContent(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    uiState = state,
                    onEvent = onEvent,
                    navController = navController
                )
            }
        }

        BaseViewState.Empty -> EmptyView()
        is BaseViewState.Error -> Text("Error while Loading the state")
        BaseViewState.Loading -> LoadingView()
    }

}


@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUIState,
    onEvent: (SignUpEvent) -> Unit,
    navController: NavController
) {

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        LoginTitle(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopStart).padding(top = 46.dp),
            title = "Create Account",
            subtitle = "Sign up to get started!"
        )

        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            LoginTextField(
                modifier = Modifier.fillMaxWidth().border(
                    BorderStroke(
                        1.dp, Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ), shape = RoundedCornerShape(8.dp)
                ), value = uiState.email, label = "Email ID", onValueChange = {
                    onEvent(SignUpEvent.onEmailChange(it))
                }, isError = uiState.emailError
            )

            if (uiState.emailError) {
                Text(
                    text = "Please enter a valid email address",
                    color = MaterialTheme.colorScheme.error
                )
            }
            LoginTextField(
                modifier = Modifier.fillMaxWidth().border(
                    BorderStroke(
                        1.dp, Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ), shape = RoundedCornerShape(8.dp)
                ),
                value = uiState.password,
                label = "Password",
                onValueChange = {
                    onEvent(SignUpEvent.onPasswordChange(it))
                },
                isError = uiState.passwordError,
                visualTransformation = PasswordVisualTransformation()
            )
            if (uiState.passwordStrength != 0.0f) {
                Column {
                    Text(
                        text = "Password Strength: ${if (uiState.passwordStrength <= 0.4f) "Weak 😩" else if (uiState.passwordStrength <= 0.6f) "Medium ✅" else "Strong 💪"} ",
                        color = MaterialTheme.colorScheme.primary
                    )
                    LinearProgressIndicator(
                        progress = { uiState.passwordStrength },
                        modifier = Modifier.fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline),
                        color = if (uiState.passwordStrength <= 0.4f) MaterialTheme.colorScheme.errorContainer else if (uiState.passwordStrength <= 0.6f) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        trackColor = Color.Transparent,
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            if (uiState.passwordError) {
                Text(
                    text = "Please enter a valid password", color = MaterialTheme.colorScheme.error
                )
            }


            if (uiState.isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LoginButton(
                    modifier = Modifier.fillMaxWidth(),
                    onButtonClick = {
                        onEvent(SignUpEvent.onSignUp(uiState.email, uiState.password))
//                    onEvent(SignUpEvent.isEmailVerified)
                    },
                    buttonText = "SIGN UP",
                    enabled = if (uiState.passwordStrength >= 0.5f) true else false
                )
            }
            //enabled = !uiState.isEmailSent

//            if (uiState.signUpErrorMsg.isNotEmpty()) {
//                Text(text = uiState.signUpErrorMsg, color = MaterialTheme.colorScheme.error)
//            }

            if (uiState.isSignUpFailed) {
                Text(
                    text = uiState.signUpErrorMsg, color = MaterialTheme.colorScheme.error
                )
            }

            if (uiState.isEmailSent) {
                Text(
                    text = "Please check your email to verify your account",
                    color = MaterialTheme.colorScheme.primary
                )
            }


            if (uiState.isSignUpSuccess) {
                Text(
                    text = uiState.signUpMsg, color = MaterialTheme.colorScheme.primary
                )
            }

            LaunchedEffect(uiState.isSignUpSuccess) {
                if (uiState.isSignUpSuccess) {
                    delay(5000)
                    onEvent(SignUpEvent.resetState)
                    navController.navigate(BottomNavItem.Home.route) {
                        popUpTo(BottomNavItem.Login.route) { inclusive = true }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
//            AnimatedVisibility(uiState.currentUser != null && !uiState.currentUser.isAnonymous && uiState.isSignUpSuccess) {
//                Snackbar {
//                    Text("Sign up successful")
//                }
//                LaunchedEffect(Unit) {
//                    navController.navigate("home/${uiState.currentUser?.email}")
//                }
//            }
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("I'm already a member,", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Sign In", style = MaterialTheme.typography.titleMedium)
            }
        }


    }
}