package ru.notesapp.ui

import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BannerAdView(
    adUnitId: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { ctx ->
            TextView(ctx).apply {
                text = "Реклама: $adUnitId"
                setPadding(16, 16, 16, 16)
                setBackgroundColor(Color.LTGRAY)
                gravity = Gravity.CENTER
            }
        },
        update = { tv ->
            tv.text = "Реклама: $adUnitId"
        },
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
    )
}