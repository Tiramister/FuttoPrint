package com.example.futtoprint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.futtoprint.post.PostRepository
import com.example.futtoprint.post.PostRepositoryImpl
import com.example.futtoprint.ui.theme.FuttoPrintTheme

class MainActivity : ComponentActivity() {
    private lateinit var postRepository: PostRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        postRepository = PostRepositoryImpl(applicationContext)

        setContent {
            FuttoPrintTheme {
                FuttoPrintApp(postRepository)
            }
        }
    }
}
