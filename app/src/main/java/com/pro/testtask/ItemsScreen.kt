package com.pro.testtask

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pro.testtask.components.Error
import com.pro.testtask.components.LoadingBlock

@Composable
fun ItemsScreen(
    itemsViewModel: ItemsViewModel
) {
    Log.d("ItemsScreen", "start")
    val item by remember { itemsViewModel.currentItem }
    val isLoading by remember { itemsViewModel.isItemLoading }
    val error by remember { itemsViewModel.loadItemError }

    // if current item is null, then loading item
    if (itemsViewModel.currentItem.value == null && !isLoading && error.isBlank()) {
        val currentId = itemsViewModel.itemIndexes.value.get(
            itemsViewModel.currentItemPosition
        )
        itemsViewModel.loadItem(currentId)
    }

    if (error.isBlank()) {
        if (!isLoading)
            when (item?.type) {
                "text" -> item!!.payload.text?.let { TextItem(text = it) }
                "webpage" -> item!!.payload.url?.let { WebItem(url = it) }
            }
        else
            LoadingBlock("Item loading...")
    } else
        Error(error) {
            val currentItemPosition = itemsViewModel.currentItemPosition
            val currentId = itemsViewModel.itemIndexes.value[currentItemPosition]
            itemsViewModel.loadItem(currentId)
        }
}

@Composable
fun TextItem(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxSize().padding(16.dp),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun WebItem(url: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        }
    )
}