package com.example.emill_p2_ap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emill_p2_ap2.ui.counter.CounterScreen
import com.example.emill_p2_ap2.ui.counter.CounterViewModel
import com.example.emill_p2_ap2.ui.theme.Emill_p2_ap2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Emill_p2_ap2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: CounterViewModel = hiltViewModel()
                    val counter by viewModel.counter.collectAsState(0)
                    CounterScreen(
                        counter = counter,
                        onIncrement = viewModel::increment
                    )
                }
            }
        }
    }
}