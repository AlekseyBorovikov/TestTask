package com.pro.testtask

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pro.testtask.components.Error
import com.pro.testtask.components.LoadingBlock
import com.pro.testtask.ui.theme.TestTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "start")
        setContent {
            TestTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) { Main() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(itemsViewModel: ItemsViewModel = hiltViewModel()) {
    Scaffold(
        Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                itemsViewModel.nextCurrentItemPosition()
                itemsViewModel.updateCurrentItem()
            }) { Text(text = "Next") }
        }
    ) {
        val isLoading by remember { itemsViewModel.isIndexesLoading }
        val error by remember { itemsViewModel.loadIndexesError }

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if (error.isBlank()) {
                if (isLoading) LoadingBlock(text = "Loading indexes...")
                else ItemsScreen(itemsViewModel)
            } else Error(text = error) { itemsViewModel.loadIndexes() }
        }

        it.calculateBottomPadding()
    }
}