package com.jetbrains.notes

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.jetbrains.notes.data.core.NotificationScheduler
import com.jetbrains.notes.data.core.TaskReminderManager
import com.jetbrains.notes.ui.auth.AuthServiceImpl
import com.jetbrains.notes.ui.components.AppSearchBar
import com.jetbrains.notes.ui.components.TaskCard
import com.jetbrains.notes.ui.home.HomeViewModel
import com.jetbrains.notes.ui.home.taskDatePicker
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.permissionUtil
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.permissions.PermissionsController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.vinceglb.filekit.core.FileKit
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)

        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
            )
        )


        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()

//        val notifier = NotifierManager.getPushNotifier()

        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                println("onNewToken: $token") //Update user token in the server if needed
            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                println("onPushNotification: $title, $body")
            }
        })

        // Remove the getToken() call as it's a suspend function
        // and should be called from a coroutine

        setContent {
            val dao = getDatabaseBuilder(applicationContext).getDao()
            //For Permission Handling

            val viewModel = HomeViewModel(
                dao,
                PermissionsController(applicationContext),
                authService = AuthServiceImpl(Firebase.auth)
            )
            viewModel.permissionsController.bind(this@MainActivity)
            //For Logging
            Napier.base(DebugAntilog())

            val notificationScheduler = NotificationScheduler(applicationContext)
            TaskReminderManager().scheduleTodayTasksNotifications(
                dao, notificationScheduler
            )
            AppTheme {
                KoinContext {
                    App(
                        dao,
                        notificationScheduler,
                        permissionsController = viewModel.permissionsController
                    )
                }
            }
        }
    }

    fun onRequestButtonClick(@Suppress("UNUSED_PARAMETER") view: View?) {
        // Starts permission providing process.
        viewModel.onPhotoPressed()
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
//    App()
}

@Preview
@Composable
fun AppSearchBarPrev() {
    AppSearchBar(modifier = Modifier.fillMaxWidth(), onEvent = {})

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 600, widthDp = 350)
@Composable
fun datePreev() {
    taskDatePicker(onDateSelected = {}, onDismiss = {}, modifier = Modifier.fillMaxWidth())
}

@Preview(showBackground = true)
@Composable
fun prevTaskCard() {
    TaskCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), title = "Task Title",
        date = "2023-09-12",
        time = "10:00 AM"
    )
}