package com.kwende.snapcook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold { insets ->
                    Column(Modifier.fillMaxSize().padding(insets).padding(24.dp)) {
                        Text("SnapCook", style = MaterialTheme.typography.headlineLarge)
                        Text("Capture ingredients. Bring the conversation to ChatGPT.")
                        Text("Starter project: scanning, storage, and backup are not implemented yet.",
                            modifier = Modifier.padding(top = 24.dp))
                    }
                }
            }
        }
    }
}
