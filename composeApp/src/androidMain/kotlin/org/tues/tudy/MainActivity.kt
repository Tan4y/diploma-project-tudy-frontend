package org.tues.tudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.tues.tudy.ui.common.ErrorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ErrorScreen(
                "Create Account",
                "Password Change Unsuccessful",
                "There was an error while trying to change your password.",
                onButtonClick = {},
                arrow = true
            ) { }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}