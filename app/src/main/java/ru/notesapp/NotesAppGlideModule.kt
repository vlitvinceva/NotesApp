package ru.notesapp

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.android.EntryPointAccessors
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class NotesAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calc = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(2f)
            .build()
        builder.setMemoryCache(LruResourceCache(calc.memoryCacheSize.toLong()))

        builder.setDiskCache(
            DiskLruCacheFactory(
                context.cacheDir.absolutePath + "/image_cache",
                250L * 1024 * 1024,        // 250 МБ
            )
        )

        builder.setLogLevel(Log.VERBOSE)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Используем OkHttpClient из Hilt-графа
        val okHttp = EntryPointAccessors
            .fromApplication(context, OkHttpEntryPoint::class.java)
            .okHttp()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttp),
        )
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface OkHttpEntryPoint {
    fun okHttp(): OkHttpClient
}