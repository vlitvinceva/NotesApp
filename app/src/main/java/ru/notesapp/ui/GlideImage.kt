package ru.notesapp.ui

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

@Composable
fun GlideImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    options: RequestOptions = RequestOptions(),
    contentScale: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP,
) {
    AndroidView(
        factory = { ctx ->
            ImageView(ctx).apply {
                scaleType = contentScale
                this.contentDescription = contentDescription
            }
        },
        update = { iv ->
            android.util.Log.d("NotesApp", "Loading image: $model")
            Glide.with(iv)
                .load(model)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade(150))
                .into(iv)
        },
        modifier = modifier,
    )
}