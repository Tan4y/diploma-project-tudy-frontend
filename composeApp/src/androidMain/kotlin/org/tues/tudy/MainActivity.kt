package org.tues.tudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.tues.tudy.ui.common.SuccessErrorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            SuccessErrorScreen(
                "Create Account",
                "Password Change Unsuccessful",
                "There was an error while trying to change your password.",
                onButtonClick = {},
                arrow = true,
                success = false,
            ) { }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}